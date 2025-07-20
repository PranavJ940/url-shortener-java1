package com.example.url_shortener_java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/urlshortenerdb";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "Pranavjadhav@940"; 
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users (username VARCHAR(100) PRIMARY KEY, password VARCHAR(100))";
    private static final String INSERT_USER = "INSERT INTO users (username, password) VALUES (?, ?)";
    private static final String SELECT_USER = "SELECT password FROM users WHERE username = ?";

    public UserService() throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS); Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE);
        }
    }

    public boolean register(String username, String password) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
                PreparedStatement ps = conn.prepareStatement(INSERT_USER)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            logger.info("Registered user: {}", username);
            return true;
        } catch (SQLException e) {
            logger.error("Error registering user", e);
            return false;
        }
    }

    public boolean login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
                PreparedStatement ps = conn.prepareStatement(SELECT_USER)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }
        } catch (SQLException e) {
            logger.error("Error logging in user", e);
        }
        return false;
    }
}
