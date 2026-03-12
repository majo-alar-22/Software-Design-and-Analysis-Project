package com.csci2020.frontend.components;

import com.csci2020.frontend.NewTheme;

import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StyledPasswordField extends JPasswordField {
    public StyledPasswordField(int columns){
        super(columns);
    }
    public void setTheme(NewTheme theme) {
        this.setBackground(theme.getInputBackground());
        this.setForeground(theme.getInputForeground());
        this.setBorder(BorderFactory.createLineBorder(NewTheme.getActiveTheme().getBorderPrimary()));
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                StyledPasswordField.this.setBorder(BorderFactory.createLineBorder(NewTheme.getActiveTheme().getBorderFocused()));
            }

            @Override
            public void focusLost(FocusEvent e) {
                StyledPasswordField.this.setBorder(BorderFactory.createLineBorder(NewTheme.getActiveTheme().getBorderPrimary()));
            }
        });
    }
}
