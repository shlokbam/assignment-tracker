package com.assignmenttracker.ui;

import com.assignmenttracker.model.Assignment;
import com.assignmenttracker.model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class AssignmentDialog extends JDialog {
    private final JTextField titleField;
    private final JTextField courseField;
    private final JSpinner dateSpinner;
    private final JSpinner timeSpinner;
    private final Consumer<Assignment> onSave;
    private final User user;

    public AssignmentDialog(JFrame parent, User user, Consumer<Assignment> onSave) {
        super(parent, "Add New Assignment", true);
        this.user = user;
        this.onSave = onSave;

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title field
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        add(titleField, gbc);

        // Course field
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Course:"), gbc);

        gbc.gridx = 1;
        courseField = new JTextField(20);
        add(courseField, gbc);

        // Date spinner
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        add(dateSpinner, gbc);

        // Time spinner
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Time:"), gbc);

        gbc.gridx = 1;
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        add(timeSpinner, gbc);

        // Save button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        JButton saveButton = new JButton("Save Assignment");
        saveButton.addActionListener(e -> handleSave());
        add(saveButton, gbc);
    }

    private void handleSave() {
        if (titleField.getText().isEmpty() || courseField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            java.util.Date dateValue = (java.util.Date) dateSpinner.getValue();
            java.util.Date timeValue = (java.util.Date) timeSpinner.getValue();

            // Combine date and time
            LocalDateTime dueDate = LocalDateTime.of(
                    dateValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                    timeValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime()
            );

            Assignment assignment = new Assignment(
                    0, // ID will be set by database
                    user.getId(),
                    titleField.getText(),
                    courseField.getText(),
                    dueDate,
                    Assignment.Status.NOT_STARTED
            );

            onSave.accept(assignment);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving assignment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}