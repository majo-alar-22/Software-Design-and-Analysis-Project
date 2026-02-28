package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private Database() {}

    // Store DB in user home so it's writable for everyone
    public static final String DB_URL =
            "jdbc:sqlite:" + System.getProperty("user.home") + "/soccer.db";

    private static volatile boolean initialized = false;

    public static void init() {
        if (initialized) return;
        synchronized (Database.class) {
            if (initialized) return;

            try (Connection conn = getConnection();
                 Statement st = conn.createStatement()) {

                st.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        email TEXT NOT NULL UNIQUE,
                        password_hash TEXT NOT NULL,
                        salt TEXT NOT NULL,
                        created_at TEXT NOT NULL
                    )
                """);

                initialized = true;
            } catch (SQLException e) {
                throw new RuntimeException("DB init failed: " + e.getMessage(), e);
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
