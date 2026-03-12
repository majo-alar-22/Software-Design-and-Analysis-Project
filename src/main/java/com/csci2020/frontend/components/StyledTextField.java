package com.csci2020.frontend.components;

import com.csci2020.frontend.NewTheme;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StyledTextField extends JTextField{
    public StyledTextField(int columns){
        super(columns);
    }

    public void setTheme(NewTheme style){
        this.setBackground(style.getInputBackground());
        this.setForeground(style.getInputForeground());
        this.setBorder(BorderFactory.createLineBorder(NewTheme.getActiveTheme().getBorderPrimary()));
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                StyledTextField.this.setBorder(BorderFactory.createLineBorder(NewTheme.getActiveTheme().getBorderFocused()));
            }

            @Override
            public void focusLost(FocusEvent e) {
                StyledTextField.this.setBorder(BorderFactory.createLineBorder(NewTheme.getActiveTheme().getBorderPrimary()));
            }
        });
    }
}
