package com.csci2020.frontend;

import com.csci2020.backend.Authentication;
import com.csci2020.backend.AuthenticationResult;
import com.csci2020.backend.Database;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private static final String[] LABELS = {
            "Username",
            "Password",
            "First Name",
            "Last Name"
    };

    private final Database db;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton registerButton;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    public LoginPanel(Database db){
        this.db = db;
        this.usernameField = new JTextField();
        this.passwordField = new JPasswordField();
        this.firstNameField = new JTextField();
        this.lastNameField = new JTextField();
        this.loginButton = new JButton("Login");
        this.registerButton = new JButton("Register");
        initUI();
        initHandler();
    }
    public void initUI(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 5;
        gbc.ipady = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        for(String label : LABELS){
            this.add(new JLabel(label), gbc);
            gbc.gridy++;
        }
        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        this.add(usernameField, gbc);
        gbc.gridy++;
        this.add(passwordField, gbc);
        gbc.gridy++;
        this.add(firstNameField, gbc);
        gbc.gridy++;
        this.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        this.add(loginButton, gbc);
        gbc.gridx++;
        this.add(registerButton, gbc);
    }

    public void initHandler(){
        loginButton.addActionListener((event)->{
            AuthenticationResult result = db.login(usernameField.getText(), passwordField.getPassword());
            System.out.println(result.message());
        });
        registerButton.addActionListener((event)->{
            AuthenticationResult result = db.createNewAccount(usernameField.getText(), firstNameField.getText(), lastNameField.getText(), passwordField.getPassword());
            System.out.println(result.message());
        });
    }
}