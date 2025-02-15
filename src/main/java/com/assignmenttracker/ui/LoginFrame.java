package com.assignmenttracker.ui;

import com.assignmenttracker.model.User;
import com.assignmenttracker.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final UserService userService;
    private final JTextField emailField;
    private final JPasswordField passwordField;

    public LoginFrame() {
        userService = new UserService();

        setTitle("Assignment Tracker - Login");
        setSize(400, 300);
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

        // Login button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        panel.add(loginButton, gbc);

        // Register link
        gbc.gridy = 3;
        JButton registerButton = new JButton("Register New Account");
        registerButton.addActionListener(e -> showRegisterFrame());
        panel.add(registerButton, gbc);

        add(panel);
    }

    private void handleLogin() {
        try {
            User user = userService.login(
                    emailField.getText(),
                    new String(passwordField.getPassword())
            );

            if (user != null) {
                new DashboardFrame(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error during login: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegisterFrame() {
        new RegisterFrame().setVisible(true);
        dispose();
    }
}