package com.example.turistguidedel2.Repository;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;

public class ConnectionManager {
    static Connection connection;

    @Value("${TEST_DATABASE_URL}")
    private String databaseUrl;
    @Value("${TEST_USERNAME}")
    private String username;
    @Value("${TEST_PASSWORD}")
    private String password;

    private ConnectionManager(){
        instantiateConnection();
    }

    private void instantiateConnection(){
        if(connection != null) {
            try {
                connection = DriverManager.getConnection(databaseUrl, username, password);
                System.out.println("Connected to the database");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to connect to the database");
            }
        }
    }
}
