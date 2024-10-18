package com.example.turistguidedel2.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.example.turistguidedel2.Model.TouristAttraction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TouristRepository {
    @Value("${TEST_DATABASE_URL}")
    private String databaseUrl;
    @Value("${TEST_USERNAME}")
    private String username;
    @Value("${TEST_PASSWORD}")
    private String password;
    @Value("${PROD_DATABASE_URL}")
    private String prodDatabaseUrl;
    @Value("${PROD_USERNAME}")
    private String prodUsername;
    @Value("${PROD_PASSWORD}")
    private String prodPassword;

    //profilen bliver loaded her tror jeg.
    @Value( "${spring.profiles.active}")
    private String activeProfile;

    private Connection conn;

    // this is a list of tourist attractions that will be used to store the tourist attractions
    private  ArrayList<TouristAttraction> touristAttractions = new ArrayList<>();


    //trying to implitment the CRUD operations as i understand them:

    public TouristRepository() {
    }

    //read. simply return the list of tourist attractions and print them out
    public List<TouristAttraction> getAllTouristAttractions() {
        connectToDataBase();

        try (Statement statement = conn.createStatement()) {
            String sqlString =
                      "SELECT ta.id, ta.name, ta.description, ta.city, GROUP_CONCAT(t.name SEPARATOR ', ') AS tags "
                    + "FROM TouristAttractions ta "
                    + "LEFT JOIN AttractionTags at ON ta.id = at.tourist_attraction_id "
                    + "LEFT JOIN Tags t ON at.tag_id = t.id "
                    + "GROUP BY ta.id, ta.name, ta.description, ta.city";

            ResultSet resultSet = statement.executeQuery(sqlString);

            touristAttractions.clear();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String city = resultSet.getString("city");
                String tags = resultSet.getString("tags");


                touristAttractions.add(new TouristAttraction(name, description, city, tags));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectFromDataBase();
        }

        return touristAttractions;
    }


    //update. find the tourist attraction by name and update the description of it to the new description given in the parameters
    public void updateTouristAttraction(String name, String newDesc) {
        connectToDataBase();
        String sqlString = "UPDATE touristattractions SET description = ? WHERE name = ?";

        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, newDesc);
            statement.setString(2, name);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("Tourist attraction updated successfully");
            } else {
                System.out.println("Tourist attraction not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectFromDataBase();
        }
    }


    //delete. simply remove the object at the index given
    public void deleteTouristAttraction(String name){
        int index = touristAttractions.indexOf(getTouristAttractionByName(name));
        if (index < 0 || index >= touristAttractions.size()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        touristAttractions.remove(index);
    }

        //get name. get tourist attraction by name and return it if it exists in the list of tourist attractions now with SQL needs tags some
        public TouristAttraction getTouristAttractionByName(String name) {
            connectToDataBase();
            TouristAttraction touristAttraction = null;
            String sqlString =
                    "SELECT TouristAttractions.id, TouristAttractions.name, TouristAttractions.description, TouristAttractions.city, " +
                            "GROUP_CONCAT(Tags.name SEPARATOR ', ') AS tags " +
                            "FROM TouristAttractions " +
                            "LEFT JOIN AttractionTags ON TouristAttractions.id = AttractionTags.tourist_attraction_id " +
                            "LEFT JOIN Tags ON AttractionTags.tag_id = Tags.id " +
                            "WHERE TouristAttractions.name LIKE ? " +
                            "GROUP BY TouristAttractions.id, TouristAttractions.name, TouristAttractions.description, TouristAttractions.city";

            try (PreparedStatement statement = conn.prepareStatement(sqlString)){
                statement.setString(1, name);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String description = resultSet.getString("description");
                    String city = resultSet.getString("city");
                    String tags = resultSet.getString("tags");
                    touristAttraction = new TouristAttraction(name, description, city, tags);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                disconnectFromDataBase();
            }
            return touristAttraction;
        }

    //get tagsList
    public ArrayList<String> getallTags() {
        return new ArrayList<>();
    }

    /*public ArrayList<String> getTags(String name) {
        return getTouristAttractionByName(name).getTags();
    } */

    //updating the database insert
    //create. add a tourist attraction to the list
    public int saveTouristAttractions(TouristAttraction attraction){
        int updatedRows = 1;

        String sqlString = "INSERT INTO touristattractions (name, description, city) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {

            statement.setString(1, attraction.getName());
            statement.setString(2, attraction.getDescription());
            statement.setString(3, "Copenhagen");


            updatedRows = statement.executeUpdate();

            System.out.println("A new tourist attraction was added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add a new tourist attraction");
        }
        System.out.println("Number of rows updated: " + updatedRows);
        return updatedRows;
    }
    private void connectToDataBase() {
            try {
                if("test".equals(activeProfile)){
                    conn = DriverManager.getConnection(databaseUrl, username, password);
                    System.out.println("Connected to the Test database");
                } else if("prod".equals(activeProfile)){
                    conn = DriverManager.getConnection(prodDatabaseUrl, prodUsername, prodPassword);
                    System.out.println("Connected to the PROD database");
                } else {
                    System.out.println("No active profile found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to connect to the  database");
            }
    }

    private void disconnectFromDataBase() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Disconnected from the database");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to disconnect from the database");
            }
        }
    }


}


