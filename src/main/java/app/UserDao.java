package app;

import java.sql.*;

public final class UserDao {

    public boolean emailExists(String email) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void createUser(String name, String email, String password) throws SQLException {
        String salt = Auth.generateSalt();
        String hash = Auth.sha256Base64(salt + password);

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 INSERT INTO users(name, email, password_hash, salt, created_at)
                 VALUES(?, ?, ?, ?, datetime('now'))
             """)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hash);
            ps.setString(4, salt);
            ps.executeUpdate();
        }
    }

    public UserRecord findByEmail(String email) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("""
                 SELECT id, name, email, password_hash, salt
                 FROM users WHERE email = ?
             """)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new UserRecord(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("salt")
                );
            }
        }
    }

    public record UserRecord(int id, String name, String email, String passwordHash, String salt) {}
}
