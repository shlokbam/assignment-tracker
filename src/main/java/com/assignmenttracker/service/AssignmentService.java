package com.assignmenttracker.service;

import com.assignmenttracker.database.DatabaseConnection;
import com.assignmenttracker.model.Assignment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssignmentService {
    public Assignment createAssignment(Assignment assignment) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO assignments (user_id, title, course_name, due_date, status) " +
                             "VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, assignment.getUserId());
            stmt.setString(2, assignment.getTitle());
            stmt.setString(3, assignment.getCourseName());
            stmt.setTimestamp(4, Timestamp.valueOf(assignment.getDueDate()));
            stmt.setString(5, assignment.getStatus().name());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                assignment.setId(rs.getInt(1));
                return assignment;
            }
        }
        return null;
    }

    public List<Assignment> getUserAssignments(int userId) throws SQLException {
        List<Assignment> assignments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM assignments WHERE user_id = ? ORDER BY due_date")) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                assignments.add(new Assignment(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getString("course_name"),
                        rs.getTimestamp("due_date").toLocalDateTime(),
                        Assignment.Status.valueOf(rs.getString("status"))
                ));
            }
        }
        return assignments;
    }

    public void updateAssignmentStatus(int assignmentId, Assignment.Status status) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE assignments SET status = ? WHERE id = ?")) {

            stmt.setString(1, status.name());
            stmt.setInt(2, assignmentId);
            stmt.executeUpdate();
        }
    }
}