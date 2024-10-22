package com.example.turistguidedel2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionManager {

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

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private Connection conn;

    public synchronized Connection getConnection() {
        // If a connection is already established, return it
        if (conn != null) {
            return conn;
        }
        // If no connection is established, establish a new connection
        try {
            if ("test".equals(activeProfile)) {
                conn = DriverManager.getConnection(databaseUrl, username, password);
                System.out.println("Connected to the Test database");
            } else if ("prod".equals(activeProfile)) {
                conn = DriverManager.getConnection(prodDatabaseUrl, prodUsername, prodPassword);
                System.out.println("Connected to the PROD database");
            } else {
                System.out.println("No active profile found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database");
        }

        return conn;
    }
}
