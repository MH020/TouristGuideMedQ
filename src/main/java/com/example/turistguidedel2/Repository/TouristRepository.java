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

    //read. simply return the list of tourist attractions and print them out
    public List<TouristAttraction> getAllTouristAttractions() {
        List<TouristAttraction> touristAttractions = new ArrayList<>(); // Initialize the list

        try (Statement statement = conn.createStatement()) {
            String sqlString =
                    "SELECT ta.ID, ta.Name, ta.Description, c.Name AS city, " +
                            "GROUP_CONCAT(t.Name SEPARATOR ', ') AS tags " +
                            "FROM Touristattractions ta " +
                            "LEFT JOIN City c ON ta.Postcode = c.Postcode " +  // Join City to get the city name
                            "LEFT JOIN AttractionsTags at ON ta.ID = at.Touristattraction_ID " + // Correct table reference
                            "LEFT JOIN Tags t ON at.Tags_ID = t.ID " + // Correct table reference
                            "GROUP BY ta.ID, ta.Name, ta.Description, c.Name"; // Group by the selected fields

            ResultSet resultSet = statement.executeQuery(sqlString);

            // Clear the existing list before adding new attractions
            touristAttractions.clear();
            while (resultSet.next()) {
                // Retrieve values using the correct column names
                String name = resultSet.getString("Name");
                String description = resultSet.getString("Description");
                String city = resultSet.getString("city");
                String tags = resultSet.getString("tags");

                // Create a new TouristAttraction object and add it to the list
                touristAttractions.add(new TouristAttraction(name, description, city, tags));
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
        int updatedRows;
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
                    String tags = resultSet.getString("tags");
                    touristAttraction = new TouristAttraction(name, description, city, tags);
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

        int updatedRows = 0;

        String insertAttraction = "INSERT INTO touristattractions (name, description, city) VALUES (?, ?, ?)";
        String getTagID = "SELECT id FROM tags WHERE name = ?";
        String insertAttractionTags = "INSERT INTO attractiontags (tourist_attraction_id, tag_id) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            // Insert the attraction into the database
            try (PreparedStatement statement = conn.prepareStatement(insertAttraction, Statement.RETURN_GENERATED_KEYS)) {

                statement.setString(1, attraction.getName());
                statement.setString(2, attraction.getDescription());
                statement.setString(3, attraction.getCity());

                updatedRows = statement.executeUpdate();

                // Get the generated tourist attraction ID
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int touristAttractionId = generatedKeys.getInt(1);

                        // Insert tags associated with the attraction
                        try (PreparedStatement getTagIDStatement = conn.prepareStatement(getTagID);
                             PreparedStatement insertAttractionTagsStatement = conn.prepareStatement(insertAttractionTags)) {

                            for (String tag : attraction.getTags().split(",")) {
                                tag = tag.trim();

                                // Get the tag ID from the tags table
                                getTagIDStatement.setString(1, tag);
                                try (ResultSet resultSet = getTagIDStatement.executeQuery()) {
                                    if (resultSet.next()) {
                                        int tagId = resultSet.getInt("id");

                                        // Insert into attractiontags table
                                        insertAttractionTagsStatement.setInt(1, touristAttractionId);
                                        insertAttractionTagsStatement.setInt(2, tagId);
                                        insertAttractionTagsStatement.executeUpdate();
                                    } else {
                                        System.out.println("Tag not found: " + tag);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            conn.commit();
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


}


