package com.csci2020.frontend.components;

import com.csci2020.frontend.NewTheme;

import javax.swing.*;

public class StyledButton extends JButton {
    public StyledButton(String text){
        super(text);
    }
    public void setTheme(NewTheme theme) {
        this.setBackground(theme.getButtonBackground());
        this.setOpaque(true);

        // Apply text color
        this.setForeground(theme.getButtonForeground());

        // Apply border
        if (theme.getBorderPrimary() != null) {
            this.setBorder(javax.swing.BorderFactory.createLineBorder(theme.getBorderPrimary()));
        }

        // Apply hover effects
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                StyledButton.this.setBackground(theme.getButtonHover());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                StyledButton.this.setBackground(theme.getButtonBackground());
            }
        });

        // Apply pressed effect
        this.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (theme.getButtonPressed() != null) {
                    StyledButton.this.setBackground(theme.getButtonPressed());
                }
            }
        });
    }
}
