package com.assignmenttracker.service;

import com.assignmenttracker.database.DatabaseConnection;
import com.assignmenttracker.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserService {
    public User register(String email, String password) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (email, password_hash) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return new User(rs.getInt(1), email, hashedPassword);
            }
        }
        return null;
    }

    public User login(String email, String password) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE email = ?")) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && BCrypt.checkpw(password, rs.getString("password_hash"))) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password_hash")
                );
            }
        }
        return null;
    }
}