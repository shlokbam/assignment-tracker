// Add this as a new file: src/main/java/com/assignmenttracker/ui/AppColors.java
package com.assignmenttracker.ui;

import java.awt.Color;

/**
 * Centralized color scheme for the application.
 * Demonstrates the use of OOP principles: encapsulation by providing
 * static final fields that are accessed through getters.
 */
public class AppColors {
    // Primary colors
    private static final Color PRIMARY = new Color(26, 115, 232);      // Deep Blue
    private static final Color PRIMARY_DARK = new Color(13, 71, 161);  // Darker blue for hover/selected
    private static final Color BACKGROUND = new Color(255, 255, 255);  // White
    private static final Color TEXT_PRIMARY = new Color(32, 33, 36);   // Dark Gray

    // Status colors
    private static final Color STATUS_NOT_STARTED = new Color(255, 153, 0);  // Orange
    private static final Color STATUS_IN_PROGRESS = new Color(33, 150, 243); // Blue
    private static final Color STATUS_COMPLETED = new Color(52, 168, 83);    // Green

    // Panel backgrounds
    private static final Color PANEL_BACKGROUND = new Color(248, 249, 250);  // Light Gray
    private static final Color SIDEBAR_BACKGROUND = new Color(241, 243, 244); // Slight Gray

    // Static getters - demonstrates encapsulation
    public static Color getPrimary() { return PRIMARY; }
    public static Color getPrimaryDark() { return PRIMARY_DARK; }
    public static Color getBackground() { return BACKGROUND; }
    public static Color getTextPrimary() { return TEXT_PRIMARY; }

    public static Color getStatusColor(String status) {
        switch (status) {
            case "NOT_STARTED": return STATUS_NOT_STARTED;
            case "IN_PROGRESS": return STATUS_IN_PROGRESS;
            case "COMPLETED": return STATUS_COMPLETED;
            default: return TEXT_PRIMARY;
        }
    }

    public static Color getPanelBackground() { return PANEL_BACKGROUND; }
    public static Color getSidebarBackground() { return SIDEBAR_BACKGROUND; }
}
