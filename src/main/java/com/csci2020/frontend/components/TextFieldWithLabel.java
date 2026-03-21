package com.csci2020.frontend.components;

import com.csci2020.frontend.NewTheme;

import javax.swing.*;
import java.awt.*;

public class TextFieldWithLabel extends JPanel {
    public JLabel label;
    public StyledTextField textField;
    private final BorderLayout layout;

    public TextFieldWithLabel(int columns, String label) {
        this.label = new JLabel(label);
        this.textField = new StyledTextField(columns);
        this.layout = new BorderLayout(0, 4);
        initComponents();
    }

    private void initComponents() {
        this.setOpaque(false);
        this.setLayout(layout);

        this.label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        this.add(label, BorderLayout.NORTH);
        this.add(textField, BorderLayout.CENTER);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        this.textField.setText(text);
    }

    public String getLabel() {
        return this.label.getText();
    }

    public void setLabel(String text) {
        this.label.setText(text);
    }

    public JTextField getField() {
        return textField;
    }

    public void setTheme(NewTheme theme) {
        this.label.setForeground(theme.getForegroundPrimary());
        this.textField.setTheme(theme);
    }
}