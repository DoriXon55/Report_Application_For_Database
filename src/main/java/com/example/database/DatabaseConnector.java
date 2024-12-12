package com.example.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    private static HikariDataSource dataSource;

    static {
        try {
            // Wczytaj konfigurację
            Properties properties = new Properties();
            InputStream is = DatabaseConnector.class.getClassLoader().getResourceAsStream("db.properties");
            if (is == null) {
                throw new RuntimeException("Plik db.properties nie został znaleziony w katalogu resources.");
            }
            properties.load(is);

            // Konfiguracja HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password", "")); // Jeśli brak hasła, ustaw pusty ciąg

            // Konfiguracja puli połączeń
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000); // 30 sekund

            // Tworzenie źródła danych
            dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            throw new RuntimeException("Błąd inicjalizacji połączenia z bazą danych: " + e.getMessage(), e);
        }
    }

    /**
     * Pobierz połączenie z bazą danych
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Zamknij pulę połączeń
     */
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
