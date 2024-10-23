package com.example.turistguidedel2.Repository;
import com.example.turistguidedel2.ConnectionManager;
import org.springframework.stereotype.Repository;
import com.example.turistguidedel2.Model.TouristAttraction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TouristRepository {
    private final Connection conn;

    // this is a list of tourist attractions that will be used to store the tourist attractions
    private  final ArrayList<TouristAttraction>  touristAttractions = new ArrayList<>();


    //trying to implitment the CRUD operations as i understand them:

    public TouristRepository(ConnectionManager connectionManager) {
        //this is where the connection is made to the database
        this.conn = connectionManager.getConnection();
    }

    //read. simply return the list of tourist attractions and print them out ddscz
    public List<TouristAttraction> getAllTouristAttractions() {
        List<TouristAttraction> touristAttractions = new ArrayList<>(); // Initialize the list

        try (Statement statement = conn.createStatement()) {
            String sqlString =
                    "SELECT Touristattractions.ID, Touristattractions.Name, Touristattractions.Description, Touristattractions.Postcode, City.Name, " +
                            "       GROUP_CONCAT(Tags.Name SEPARATOR ', ') AS tags " +
                            "FROM Touristattractions " +
                            "LEFT JOIN City ON Touristattractions.Postcode = City.Postcode " +
                            "LEFT JOIN AttractionsTags ON Touristattractions.ID = AttractionsTags.Touristattraction_ID " +
                            "LEFT JOIN Tags ON AttractionsTags.Tags_ID = Tags.ID " +
                            "GROUP BY Touristattractions.ID, Touristattractions.Name, Touristattractions.Description, Touristattractions.Postcode, City.Name";

            ResultSet resultSet = statement.executeQuery(sqlString);

            // Clear the existing list before adding new attractions
            touristAttractions.clear();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String city = resultSet.getString("name");
                int postcode = resultSet.getInt("postcode");
                String tags = resultSet.getString("tags");
                touristAttractions.add(new TouristAttraction(name, description, city,postcode, tags));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return touristAttractions; // Return the list of tourist attractions
    }


    //update. find the tourist attraction by name and update the description of it to the new description given in the parameters
    public void updateTouristAttraction(String name, String newDesc) {

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
        }
    }

    //delete. simply remove the object at the index given
    public void deleteTouristAttraction(String name){
        String deleteAtTags = "DELETE FROM attractionstags WHERE Touristattraction_ID = (SELECT id FROM touristattractions WHERE name = ?)";
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
            statement.executeUpdate();
            // Commit the transaction
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); // Rollback in case det ikke virker og der er fejl
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        //get name. get tourist attraction by name and return it if it exists in the list of tourist attractions now with SQL needs tags some
        public TouristAttraction getTouristAttractionByName(String name) {

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
                    int postcode = resultSet.getInt("postcode");
                    String tags = resultSet.getString("tags");
                    touristAttraction = new TouristAttraction(name, description,city, postcode, tags);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return touristAttraction;
        }

    //get tagsList
    public ArrayList<String> getAllTags() {
        List <String> tags = new ArrayList<>();

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
        int updatedRows = 0;//??

        //SQL strings
        String insertAttractionSQLString = "INSERT INTO touristattractions (name, description, postcode) VALUES (?, ?, ?)";//create new touristattraction
        String insertOrUpdatePostcodeInCitySQLString = "INSERT INTO city (name, postcode) VALUES (?, ?) ON DUPLICATE KEY UPDATE postcode = VALUES(postcode)";//make new city if no duplicates.

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
            statement.setInt(3, attraction.getPostcode());

            // Get auto-generated tourist attraction ID
            touristAttractionId = getAutoGeneratedKeys(statement);

            updatedRows = statement.executeUpdate();

            // Insert tags associated with the attraction
            //handling tag table and attractiontags table
            addTagsToTagsTable(attraction, touristAttractionId);

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
        return updatedRows;
    }

    //save metode
    public int getAutoGeneratedKeys(PreparedStatement statement) throws SQLException{
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
    public void addTagsToTagsTable(TouristAttraction attraction, int touristAttractionId){
        String selectTagIDSQLString = "SELECT id FROM tags WHERE name = ?"; //get id from tag table
        String insertAttractionTagsSQLString = "INSERT INTO attractionstags (tourist_attraction_id, tag_id) VALUES (?, ?)";//connect tags with touristattraction
        try (PreparedStatement tagIDStatement = conn.prepareStatement(selectTagIDSQLString);
             PreparedStatement attractionTagsStatement = conn.prepareStatement(insertAttractionTagsSQLString)) {

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

    public void cityHandler(String name, int postcode) {
        //inserting or updating the city
        String insertOrUpdatePostcodeInCitySQLString =
                "INSERT INTO city (name, postcode) VALUES (?, ?) ON DUPLICATE KEY UPDATE postcode = VALUES(postcode)";

        try (PreparedStatement statement = conn.prepareStatement(insertOrUpdatePostcodeInCitySQLString)) {
            /
            statement.setString(1, name);
            statement.setInt(2, postcode);

            // Execute the update
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

}


