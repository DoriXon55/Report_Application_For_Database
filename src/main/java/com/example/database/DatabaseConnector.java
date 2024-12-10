package com.example.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    private static HikariDataSource dataSource;

    static {
        try {
            // Load configuration from file
            Properties properties = new Properties();
            properties.load(com.example.database.DatabaseConnector.class.getClassLoader().getResourceAsStream("db.properties"));

            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));

            // Pooling configuration
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000); // 30 seconds

            // Create DataSource
            dataSource = new HikariDataSource(config);

        } catch (IOException e) {
            throw new RuntimeException("Cannot load database configuration", e);
        }
    }

    /**
     * Get a connection to the database.
     *
     * @return Connection to the database
     * @throws SQLException If a connection error occurs
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Close the DataSource when the application shuts down.
     */
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}