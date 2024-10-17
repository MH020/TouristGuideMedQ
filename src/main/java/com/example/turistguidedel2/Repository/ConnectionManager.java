package com.example.turistguidedel2.Repository;
import org.springframework.beans.factory.annotation.Value;
import java.sql.*;


public class ConnectionManager {
    static public Connection connection;

    @Value("${TEST_DATABASE_URL}")
    private String TEST_DATABASE_URL;
    @Value("${TEST_USERNAME}")
    private String TEST_USERNAME;
    @Value("${TEST_PASSWORD}")
    private String TEST_PASSWORD;

    @Value("${PROD_DATABASE_URL}")
    private String PROD_DATABASE_URL;
    @Value("${PROD_USERNAME}")
    private String PROD_USERNAME;
    @Value("${PROD_PASSWORD}")
    private String PROD_PASSWORD;

    private ConnectionManager(){
        connection = instantiateConnection();
    }

    private Connection instantiateConnection(){
        if (connection != null) {
            return connection;
        }

        try {
                connection = DriverManager.getConnection(PROD_DATABASE_URL, PROD_USERNAME, PROD_PASSWORD);
                System.out.println("Connected to the database");
        } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to connect to the ProdDataBase");
        }
        return connection;
    }

    public void instantiateTestConnection(){
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(TEST_DATABASE_URL, TEST_USERNAME, TEST_PASSWORD);
                System.out.println("Connected to the TestDataBase");
            } catch(SQLException e){
                e.printStackTrace();
                System.out.println("Failed to connect to the database");
            }
        }
    }

    static public Connection getConnection(){
        return connection;
    }
}
