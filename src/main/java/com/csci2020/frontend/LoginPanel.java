package com.csci2020.frontend;

import com.csci2020.backend.AuthenticationResult;
import com.csci2020.backend.AuthenticationResult.AUTHENTICATION_STATUS;
import com.csci2020.backend.Database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final Database db;
    private final Window window;

    // Declaration of Fields associated with the account registry
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JButton loginButton;
    private final JButton registerButton;

    /** Constructor for the login panel
     * @param db: Instance of a Database object, controls access to the database
     * @param window: Window object used for the login panel
     **/
    public LoginPanel(Database db, Window window) {
        this.db = db;
        this.window = window;

        this.usernameField = new JTextField(15);
        this.passwordField = new JPasswordField(15);
        this.firstNameField = new JTextField(15);
        this.lastNameField = new JTextField(15);
        this.loginButton = new JButton("Login");
        this.registerButton = new JButton("Register");

        initUI();
        initHandler();
    }

    // Arranges and initializes UI elements when called in the LoginPanel() constructor
    private void initUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(235, 242, 250));

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(420, 320));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Soccer League Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitleLabel = new JLabel("Login or create a new account", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.DARK_GRAY);

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        String[] labels = {"Username:", "Password:", "First Name:", "Last Name"};
        for(String label : labels){
            formPanel.add(new JLabel(label), gbc);
            gbc.gridy++;
        }
        gbc.weightx = 1;
        gbc.gridy = 0;
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        gbc.gridy++;
        formPanel.add(passwordField, gbc);
        gbc.gridy++;
        formPanel.add(firstNameField, gbc);
        gbc.gridy++;
        formPanel.add(lastNameField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(Color.WHITE);

        loginButton.setPreferredSize(new Dimension(110, 35));
        registerButton.setPreferredSize(new Dimension(110, 35));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(cardPanel);
    }

    // Helper function to listen for user actions when logging in
    private void initHandler() {
        loginButton.addActionListener((event) -> {
            AuthenticationResult result = db.login(
                    usernameField.getText().trim(),
                    passwordField.getPassword()
            );

            JOptionPane.showMessageDialog(this, result.message());

            if (result.status() == AUTHENTICATION_STATUS.SUCCESS) {
                window.showLoggedInUserTeamRoster();
            }
        });

        registerButton.addActionListener((event) -> {
            AuthenticationResult result = db.createNewAccount(
                    usernameField.getText().trim(),
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    passwordField.getPassword()
            );

            JOptionPane.showMessageDialog(this, result.message());
        });
    }
}