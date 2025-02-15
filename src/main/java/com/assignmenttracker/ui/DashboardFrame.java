package com.assignmenttracker.ui;

import com.assignmenttracker.model.Assignment;
import com.assignmenttracker.model.User;
import com.assignmenttracker.service.AssignmentService;
import com.assignmenttracker.service.EmailService;
import com.assignmenttracker.ui.AppColors;
import com.assignmenttracker.ui.components.StyledButton;
import com.assignmenttracker.ui.components.AssignmentCard;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final User user;
    private final AssignmentService assignmentService;
    private final EmailService emailService;
    private final JPanel assignmentsPanel;
    private final JPanel upcomingPanel;
    private final JPanel statsPanel;

    public DashboardFrame(User user) {
        this.user = user;
        this.assignmentService = new AssignmentService();
        this.emailService = new EmailService();

        setTitle("Assignment Tracker - Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebarPanel = createSidebarPanel();
        add(sidebarPanel, BorderLayout.WEST);

        // Main content
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        assignmentsPanel = new JPanel();
        upcomingPanel = new JPanel();
        statsPanel = new JPanel();

        mainPanel.add(new JScrollPane(assignmentsPanel));
        mainPanel.add(new JScrollPane(upcomingPanel));
        mainPanel.add(statsPanel);

        add(mainPanel, BorderLayout.CENTER);
        // Apply consistent background color
        assignmentsPanel.setBackground(AppColors.getPanelBackground());
        upcomingPanel.setBackground(AppColors.getPanelBackground());
        statsPanel.setBackground(AppColors.getPanelBackground());

// Add some padding
        assignmentsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        upcomingPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

// Change layout for better spacing
        assignmentsPanel.setLayout(new BoxLayout(assignmentsPanel, BoxLayout.Y_AXIS));
        upcomingPanel.setLayout(new BoxLayout(upcomingPanel, BoxLayout.Y_AXIS));

        refreshDashboard();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppColors.getPrimary());
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        JLabel titleLabel = new JLabel("Assignment Tracker");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        // Add a small icon for the user
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        iconLabel.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel(user.getEmail());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 20));

        userPanel.add(iconLabel);
        userPanel.add(userLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        sidebarPanel.setBackground(AppColors.getSidebarBackground());
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(218, 220, 224)));

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 12));
        menuLabel.setForeground(new Color(95, 99, 104));
        menuLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLabel.setBorder(new EmptyBorder(20, 20, 10, 20));

        StyledButton dashboardButton = new StyledButton("Dashboard", false);
        dashboardButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        dashboardButton.setMaximumSize(new Dimension(180, 40));
        dashboardButton.setBorder(new EmptyBorder(8, 20, 8, 20));

        StyledButton addAssignmentButton = new StyledButton("+ Add Assignment", true);
        addAssignmentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addAssignmentButton.setMaximumSize(new Dimension(180, 40));
        addAssignmentButton.addActionListener(e -> showAddAssignmentDialog());
        addAssignmentButton.setBorder(new EmptyBorder(8, 20, 8, 20));

        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(menuLabel);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(dashboardButton);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(addAssignmentButton);
        sidebarPanel.add(Box.createVerticalGlue());

        return sidebarPanel;
    }

    private void showAddAssignmentDialog() {
        new AssignmentDialog(this, user, assignment -> {
            try {
                assignmentService.createAssignment(assignment);
                emailService.sendEmail(
                        user.getEmail(),
                        "New Assignment Created",
                        "You've created a new assignment: " + assignment.getTitle()
                );
                refreshDashboard();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error creating assignment: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).setVisible(true);
    }

    private void refreshDashboard() {
        try {
            List<Assignment> assignments = assignmentService.getUserAssignments(user.getId());
            updateAssignmentsPanel(assignments);
            updateUpcomingPanel(assignments);
            updateStatsPanel(assignments);

            // Set panel titles and styling
            ((JScrollPane)assignmentsPanel.getParent().getParent()).setBorder(null);
            ((JScrollPane)upcomingPanel.getParent().getParent()).setBorder(null);

            // Add titles to the main content sections
            JPanel mainPanel = (JPanel)getContentPane().getComponent(2);
            for (int i = 0; i < mainPanel.getComponentCount(); i++) {
                Component comp = mainPanel.getComponent(i);
                if (comp instanceof JScrollPane) {
                    ((JScrollPane)comp).setBorder(
                            BorderFactory.createTitledBorder(
                                    BorderFactory.createEmptyBorder(),
                                    i == 0 ? "TODAY'S ASSIGNMENTS" : "UPCOMING DEADLINES",
                                    javax.swing.border.TitledBorder.CENTER,
                                    javax.swing.border.TitledBorder.TOP,
                                    new Font("Arial", Font.BOLD, 14),
                                    AppColors.getPrimary()
                            )
                    );
                } else if (i == 2) { // Stats panel
                    ((JPanel)comp).setBorder(
                            BorderFactory.createTitledBorder(
                                    BorderFactory.createEmptyBorder(),
                                    "QUICK STATISTICS",
                                    javax.swing.border.TitledBorder.CENTER,
                                    javax.swing.border.TitledBorder.TOP,
                                    new Font("Arial", Font.BOLD, 14),
                                    AppColors.getPrimary()
                            )
                    );
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error refreshing dashboard: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAssignmentsPanel(List<Assignment> assignments) {
        assignmentsPanel.removeAll();
        assignmentsPanel.setLayout(new BoxLayout(assignmentsPanel, BoxLayout.Y_AXIS));

//        JLabel titleLabel = new JLabel("Today's Assignments");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        assignmentsPanel.add(titleLabel);

        LocalDateTime now = LocalDateTime.now();
        assignments.stream()
                .filter(a -> a.getDueDate().toLocalDate().equals(now.toLocalDate()))
                .forEach(a -> assignmentsPanel.add(createAssignmentCard(a)));

        assignmentsPanel.revalidate();
        assignmentsPanel.repaint();
    }

    private void updateUpcomingPanel(List<Assignment> assignments) {
        upcomingPanel.removeAll();
        upcomingPanel.setLayout(new BoxLayout(upcomingPanel, BoxLayout.Y_AXIS));

//        JLabel titleLabel = new JLabel("Upcoming Deadlines (7 days)");
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
//        upcomingPanel.add(titleLabel);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekLater = now.plusDays(7);

        assignments.stream()
                .filter(a -> a.getDueDate().isAfter(now) && a.getDueDate().isBefore(weekLater))
                .forEach(a -> upcomingPanel.add(createAssignmentCard(a)));

        upcomingPanel.revalidate();
        upcomingPanel.repaint();
    }

    private void updateStatsPanel(List<Assignment> assignments) {
        statsPanel.removeAll();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Total assignments
        JPanel totalPanel = createStatCard("Total", String.valueOf(assignments.size()));

        // Pending assignments
        long pending = assignments.stream()
                .filter(a -> a.getStatus() != Assignment.Status.COMPLETED)
                .count();
        JPanel pendingPanel = createStatCard("Pending", String.valueOf(pending));

        // Completed assignments
        long completed = assignments.stream()
                .filter(a -> a.getStatus() == Assignment.Status.COMPLETED)
                .count();
        JPanel completedPanel = createStatCard("Completed", String.valueOf(completed));

        statsPanel.add(totalPanel);
        statsPanel.add(pendingPanel);
        statsPanel.add(completedPanel);

        statsPanel.revalidate();
        statsPanel.repaint();
    }

    private JPanel createStatCard(String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 220, 224), 1),
                BorderFactory.createEmptyBorder(15, 30, 15, 30)
        ));
        panel.setBackground(Color.WHITE);

        // Add an icon based on title
        String icon = "📊";
        if (title.equals("Pending")) icon = "⏳";
        if (title.equals("Completed")) icon = "✅";

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(95, 99, 104));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 28));
        valueLabel.setForeground(AppColors.getTextPrimary());
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(valueLabel);

        return panel;
    }

    private JPanel createAssignmentCard(Assignment assignment) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Title and course
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(new JLabel(assignment.getTitle()), BorderLayout.NORTH);
        headerPanel.add(new JLabel("Course: " + assignment.getCourseName()), BorderLayout.CENTER);

        // Due date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        headerPanel.add(new JLabel("Due: " + assignment.getDueDate().format(formatter)),
                BorderLayout.SOUTH);

//        //status 1.0
        JButton statusButton = new JButton(assignment.getStatus().toString());
        statusButton.addActionListener(e -> updateAssignmentStatus(assignment));

        Color statusColor;
        switch (assignment.getStatus()) {
            case NOT_STARTED:
                statusColor = new Color(255, 0, 0); // Red for NOT_STARTED
                break;
            case IN_PROGRESS:
                statusColor = new Color(255, 230, 0); // Yellow for IN_PROGRESS
                break;
            case COMPLETED:
                statusColor = new Color(0, 128, 0); // Green for COMPLETED
                break;
            default:
                statusColor = Color.GRAY; // Default color
        }

        statusButton.setBackground(statusColor);
        card.add(headerPanel, BorderLayout.CENTER);
        card.add(statusButton, BorderLayout.EAST);
        statusButton.setOpaque(true); // Ensure the background color is shown
        statusButton.setBorderPainted(true); // Keep button borders for visual clarity
        statusButton.setBorder(BorderFactory.createEmptyBorder()); // Removes the border
        statusButton.setMargin(new Insets(5, 15, 5, 15));
        statusButton.setBorder(BorderFactory.createLineBorder(statusColor, 2, true));

        return card;
    }



    private void updateAssignmentStatus(Assignment assignment) {
        Assignment.Status currentStatus = assignment.getStatus();
        Assignment.Status newStatus;

        switch (currentStatus) {
            case NOT_STARTED:
                newStatus = Assignment.Status.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                newStatus = Assignment.Status.COMPLETED;
                break;
            default:
                newStatus = Assignment.Status.NOT_STARTED;
        }

        try {
            assignmentService.updateAssignmentStatus(assignment.getId(), newStatus);
            if (newStatus == Assignment.Status.COMPLETED) {
                emailService.sendEmail(
                        user.getEmail(),
                        "Assignment Completed",
                        "Congratulations! You've completed the assignment: " + assignment.getTitle()
                );
            }
            refreshDashboard();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error updating assignment status: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


}