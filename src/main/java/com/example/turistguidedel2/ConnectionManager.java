package com.example.turistguidedel2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static Connection conn;

    private ConnectionManager() {
    }

    public static Connection getConnection(String prodDatabaseUrl, String prodUsername, String prodPassword) {
        // If a connection is already established, return it
        if (conn != null) return conn;

        // Load MySQL driver (optional in modern versions)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Optional for newer JDBC versions
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Failed to load MySQL driver");
            return null;
        }

        // If no connection is established, establish a new connection
        try {
            conn = DriverManager.getConnection(prodDatabaseUrl, prodUsername, prodPassword);
            System.out.println("Connected to the PROD database");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database");
        }

        return conn;
    }
}
