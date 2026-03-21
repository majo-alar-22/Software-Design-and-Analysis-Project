package com.csci2020.frontend.components;

import com.csci2020.frontend.NewTheme;

import javax.swing.*;
import java.awt.*;

public class PasswordFieldWithLabel extends JPanel {
    public JLabel label;
    public StyledPasswordField passwordField;
    private final BorderLayout layout;

    public PasswordFieldWithLabel(int columns, String label) {
        this.label = new JLabel(label);
        this.passwordField = new StyledPasswordField(columns);
        this.layout = new BorderLayout(0, 4);
        initComponents();
    }

    private void initComponents() {
        this.setOpaque(false);
        this.setLayout(layout);

        this.label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        this.add(label, BorderLayout.NORTH);
        this.add(passwordField, BorderLayout.CENTER);
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public String getLabel() {
        return this.label.getText();
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }

    public JPasswordField getField() {
        return passwordField;
    }

    public void setTheme(NewTheme theme) {
        this.label.setForeground(theme.getForegroundPrimary());
        this.passwordField.setTheme(theme);
    }
}