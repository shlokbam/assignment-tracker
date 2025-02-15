package com.assignmenttracker.ui;

import com.assignmenttracker.model.User;
import com.assignmenttracker.service.UserService;
import com.assignmenttracker.service.EmailService;

import javax.swing.*;
        import java.awt.*;

public class RegisterFrame extends JFrame {
    private final UserService userService;
    private final EmailService emailService;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    public RegisterFrame() {
        userService = new UserService();
        emailService = new EmailService();

        setTitle("Assignment Tracker - Register");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Email field
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Confirm Password field
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Confirm Password:"), gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        panel.add(confirmPasswordField, gbc);

        // Register button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        panel.add(registerButton, gbc);

        // Back to login link
        gbc.gridy = 4;
        JButton loginButton = new JButton("Back to Login");
        loginButton.addActionListener(e -> showLoginFrame());
        panel.add(loginButton, gbc);

        add(panel);
    }

    private void handleRegister() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = userService.register(emailField.getText(), password);
            if (user != null) {
                emailService.sendEmail(
                        user.getEmail(),
                        "Welcome to Assignment Tracker",
                        "Thank you for registering with Assignment Tracker!"
                );

                JOptionPane.showMessageDialog(this,
                        "Registration successful! Please login.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                showLoginFrame();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error during registration: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLoginFrame() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}