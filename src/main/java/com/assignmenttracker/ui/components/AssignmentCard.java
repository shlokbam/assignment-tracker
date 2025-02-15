// Add this as a new file: src/main/java/com/assignmenttracker/ui/components/AssignmentCard.java
package com.assignmenttracker.ui.components;

import com.assignmenttracker.model.Assignment;
import com.assignmenttracker.ui.AppColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

/**
 * Custom card component for assignments.
 * Demonstrates OOP composition (contains other components) and encapsulation.
 */
public class AssignmentCard extends JPanel {
    private final Assignment assignment;
    private final JButton statusButton;

    public AssignmentCard(Assignment assignment, ActionListener statusUpdateListener) {
        this.assignment = assignment;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 220, 224), 1),
                new EmptyBorder(12, 15, 12, 15)
        ));
        setBackground(Color.WHITE);

        // Left content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel(assignment.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(AppColors.getTextPrimary());
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        // Course
        JLabel courseLabel = new JLabel("Course: " + assignment.getCourseName());
        courseLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        courseLabel.setForeground(new Color(95, 99, 104));
        contentPanel.add(courseLabel);
        contentPanel.add(Box.createVerticalStrut(5));

        // Due date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        JLabel dueLabel = new JLabel("Due: " + assignment.getDueDate().format(formatter));
        dueLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dueLabel.setForeground(new Color(95, 99, 104));
        contentPanel.add(dueLabel);

        // Status button
        statusButton = new JButton(assignment.getStatus().toString().replace("_", " "));
        statusButton.setOpaque(true);
        statusButton.setBorderPainted(false);
        statusButton.setFont(new Font("Arial", Font.BOLD, 12));
        statusButton.setForeground(Color.WHITE);
        statusButton.setBackground(AppColors.getStatusColor(assignment.getStatus().toString()));
        statusButton.addActionListener(statusUpdateListener);

        add(contentPanel, BorderLayout.CENTER);
        add(statusButton, BorderLayout.EAST);
    }

    // Getter - part of encapsulation
    public Assignment getAssignment() {
        return assignment;
    }

    // Method to update button appearance based on status - demonstrates encapsulation
    public void updateStatusButton() {
        statusButton.setText(assignment.getStatus().toString().replace("_", " "));
        statusButton.setBackground(AppColors.getStatusColor(assignment.getStatus().toString()));
    }
}