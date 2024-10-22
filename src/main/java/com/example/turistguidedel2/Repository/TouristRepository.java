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
    private  final ArrayList<TouristAttraction>  touristAttractions = new ArrayList<>();


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
        int updatedRows = 0;
        connectToDataBase();
        String deleteAtTags = "DELETE FROM attractiontags WHERE tourist_attraction_id = (SELECT id FROM touristattractions WHERE name = ?)";

        String deleteAt = "DELETE FROM touristattractions WHERE name = ?";

        try {
            conn.setAutoCommit(false);



        try (PreparedStatement statement = conn.prepareStatement(deleteAtTags)) {
            statement.setString(1, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement statement = conn.prepareStatement(deleteAt)) {
            statement.setString(1, name);
            updatedRows = statement.executeUpdate();
            // Commit the transaction
            conn.commit();

            // Check if attraction was deleted
            if (updatedRows == 1) {
                System.out.println("Tourist attraction and associated tags deleted successfully");
            } else {
                System.out.println("Tourist attraction not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // Rollback in case det ikke virker og der er fejl
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            disconnectFromDataBase();
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public ArrayList<String> getAllTags() {
        List <String> tags = new ArrayList<>();
        connectToDataBase();
        String sqlString = "SELECT name FROM TAGS";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlString);
            while (resultSet.next()) {
                tags.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (ArrayList<String>) tags;
    }

    
    //create. add a tourist attraction to the list
    public int saveTouristAttractions(TouristAttraction attraction) {
        connectToDataBase();
        int updatedRows = 0;//??

        //SQL strings
        String insertAttractionSQLString = "INSERT INTO touristattractions (name, description, city) VALUES (?, ?, ?)";
        String selectTagIDSQLString = "SELECT id FROM tags WHERE name = ?";
        String insertAttractionTagsSQLString = "INSERT INTO attractiontags (tourist_attraction_id, tag_id) VALUES (?, ?)";

        int touristAttractionId;
        try{
        conn.setAutoCommit(false);
        }catch (SQLException e) {
            System.out.println("fejl: " + e);
        }

        // Insert the attraction into the database
        try (PreparedStatement statement = conn.prepareStatement(insertAttractionSQLString, Statement.RETURN_GENERATED_KEYS)) {
            //setting name, desc, and city values.
            statement.setString(1, attraction.getName());
            statement.setString(2, attraction.getDescription());
            statement.setString(3, attraction.getCity());

            // Get auto-generated tourist attraction ID
            touristAttractionId = getAutoGeneratedKeys(statement);

            updatedRows = statement.executeUpdate();

            // Insert tags associated with the attraction
            //handling tag table and attractiontags table
            addTagsToTagsTable(attraction, selectTagIDSQLString, insertAttractionTagsSQLString, touristAttractionId);

            //conn.commit();
            System.out.println("A new tourist attraction was added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add a new tourist attraction");
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }

        disconnectFromDataBase();

        return updatedRows;
    }

    //save metode
    public int getAutoGeneratedKeys(PreparedStatement statement) {
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {//if gen.key not empty:
                return generatedKeys.getInt(1);
            }
        }catch(SQLException e) {
            System.out.println("fejl: " + e);
        }
        return -1;
    }

    //save metode
    public void addTagsToTagsTable(TouristAttraction attraction, String tagIDSQLString, String attractionTagsSQLString, int touristAttractionId){
        try (PreparedStatement tagIDStatement = conn.prepareStatement(tagIDSQLString);
             PreparedStatement attractionTagsStatement = conn.prepareStatement(attractionTagsSQLString)) {

            //adding every tag to attractiontags-table:
            for (String tag : attraction.getTags().split(",")) {
                setTagInTagIDStatement(tag, tagIDStatement);
                createTagResultSet(tag, tagIDStatement, attractionTagsStatement, touristAttractionId);
            }

        }catch(SQLException e) {//end of try prepared statement.
            System.out.println("fejl: " + e);
        }
    }

    //save metode
    public void setTagInTagIDStatement(String tag, PreparedStatement tagIDStatement) throws SQLException{
        tag = tag.trim(); // Trim to remove any leading/trailing spaces

        // Get the tag ID from the tags table
        tagIDStatement.setString(1, tag);
    }

    //save metode
    public void createTagResultSet (String tag, PreparedStatement tagIDStatement, PreparedStatement attractionTagsStatement, int touristAttractionId){
        try (ResultSet resultSet = tagIDStatement.executeQuery()) {
            if (resultSet.next()) {//if resultset not empty:
                int tagId = resultSet.getInt("id");//get generated key?

                // Insert tag into attractiontags table
                attractionTagsStatement.setInt(1, touristAttractionId);
                attractionTagsStatement.setInt(2, tagId);

                attractionTagsStatement.executeUpdate();

            } else {
                System.out.println("Tag not found: " + tag);
            }
        }catch(SQLException e) {
            System.out.println("fejl: " + e);
        }//end of try catch resultset
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
