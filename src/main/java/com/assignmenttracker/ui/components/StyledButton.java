// Add this as a new file: src/main/java/com/assignmenttracker/ui/components/StyledButton.java
package com.assignmenttracker.ui.components;

import com.assignmenttracker.ui.AppColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Custom button with consistent styling.
 * Demonstrates OOP inheritance (extends JButton) and polymorphism (overrides methods).
 */
public class StyledButton extends JButton {
    private boolean isPrimary;

    public StyledButton(String text, boolean isPrimary) {
        super(text);
        this.isPrimary = isPrimary;

        setOpaque(true);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(true);
        setFont(new Font("Arial", Font.BOLD, 14));
        setBorder(new EmptyBorder(8, 16, 8, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isPrimary) {
            setBackground(AppColors.getPrimary());
            setForeground(Color.WHITE);
        } else {
            setBackground(AppColors.getPanelBackground());
            setForeground(AppColors.getTextPrimary());
        }

        // Hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isPrimary) {
                    setBackground(AppColors.getPrimaryDark());
                } else {
                    setBackground(Color.LIGHT_GRAY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isPrimary) {
                    setBackground(AppColors.getPrimary());
                } else {
                    setBackground(AppColors.getPanelBackground());
                }
            }
        });
    }

    // Constructor overloading - OOP concept
    public StyledButton(String text) {
        this(text, true); // Default to primary style
    }

    // Method overriding - OOP concept
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

        super.paintComponent(g);
        g2.dispose();
    }
}