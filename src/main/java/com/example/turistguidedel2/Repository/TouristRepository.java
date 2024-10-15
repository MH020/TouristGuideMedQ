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
    private Connection conn;

    // this is a list of tourist attractions that will be used to store the tourist attractions
    private  ArrayList<TouristAttraction> touristAttractions = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<String>();
    //trying to implitment the CRUD operations as i understand them:

    public TouristRepository() {
        populateAttractions();
    }

    private void populateAttractions() {
        tags.add("Historical");
        tags.add("Museum");
        tags.add("Art");
        tags.add("Culture");
        tags.add("Sightseeing");
        tags.add("Nature");
        TouristAttraction attraction1 = new TouristAttraction("ARoS", "Art museum.", "Aarhus", tags);
        touristAttractions.add(attraction1);
        TouristAttraction attraction2 = new TouristAttraction("Bakken", "The oldest amusement park in the world.", "Klampenborg", tags);
        touristAttractions.add(attraction2);
        TouristAttraction attraction3 = new TouristAttraction("Rundetårn", "Europe's oldest observatory.", "Copenhagen", tags);
        touristAttractions.add(attraction3);
        TouristAttraction attraction4 = new TouristAttraction("Tivoli Gardens", "The 2nd oldest amusement park in the world.", "Copenhagen", tags);
        touristAttractions.add(attraction4);
    }

    //create. add a tourist attraction to the list

    //why are you here you big stupid jellyfish! you dont do anything!
    public int addTouristAttraction(TouristAttraction attraction){
        int updatedRows = 1;
        connectToDataBase();

        String insertSql = "INSERT INTO touristattractions (name, description, city) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(insertSql)) {

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


    //read. simply return the list of tourist attractions and print them out
    public List<TouristAttraction>  getAllTouristAttractions() {
        connectToDataBase();
        List<TouristAttraction> SqlTouristAttraction = new ArrayList<>();
        try (Statement statement = conn.createStatement()){
        String sqlString = "SELECT * FROM touristattractions";
        ResultSet resultSet = statement.executeQuery(sqlString);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            String city = resultSet.getString("city");
            SqlTouristAttraction.add(new TouristAttraction(name, description, city, tags));
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SqlTouristAttraction;
    }

    //update. find the tourist attraction by name and update the description of it to the new description given in the parameters
    public void updateTouristAttraction(String name, String newDesc){
        for (TouristAttraction attraction : touristAttractions) {
            if (attraction.getName().equals(name)) {
                attraction.setDescription(newDesc);
            }
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
        //get name. get tourist attraction by name and return it if it exists in the list of tourist attractions
    public TouristAttraction getTouristAttractionByName(String name){
        for (TouristAttraction attraction : touristAttractions) {
            if (attraction.getName().equals(name)) {
                return attraction;
            }
        }
        return new TouristAttraction("null", "null", "null", new ArrayList<String>());
    }
    //get tagsList
    public ArrayList<String> getallTags() {
        return new ArrayList<>(tags);
    }

    public ArrayList<String> getTags(String name) {
        return getTouristAttractionByName(name).getTags();
    }

    //updating the database insert
    public int saveTouristAttractions(TouristAttraction attraction){
        int updatedRows = 1;
        connectToDataBase();

        String insertSql = "INSERT INTO touristattractions (name, description, city) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(insertSql)) {

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
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(databaseUrl, username, password);
                System.out.println("Connected to the database");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to connect to the database");
            }
        }
    }
}


