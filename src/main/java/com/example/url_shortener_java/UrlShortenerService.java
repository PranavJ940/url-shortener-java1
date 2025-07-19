package com.example.url_shortener_java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Random;

public class UrlShortenerService {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerService.class);
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/urlshortenerdb";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "Pranavjadhav@940"; 
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS urls (code VARCHAR(10) PRIMARY KEY, url VARCHAR(2048))";
    private static final String INSERT_URL = "INSERT INTO urls (code, url) VALUES (?, ?)";
    private static final String SELECT_URL = "SELECT url FROM urls WHERE code = ?";

    public UrlShortenerService() throws SQLException {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE);
        }
    }

    public String shorten(String url) {
        String code = generateCode();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(INSERT_URL)) {
            ps.setString(1, code);
            ps.setString(2, url);
            ps.executeUpdate();
            logger.info("Shortened {} to {}", url, code);
        } catch (SQLException e) {
            logger.error("Error shortening URL", e);
        }
        return code;
    }

    public String getOriginalUrl(String code) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(SELECT_URL)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("url");
            }
        } catch (SQLException e) {
            logger.error("Error retrieving original URL", e);
        }
        return null;
    }

    private String generateCode() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
