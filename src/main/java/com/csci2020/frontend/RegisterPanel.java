package com.csci2020.frontend;

import com.csci2020.backend.AuthenticationResult;
import com.csci2020.frontend.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterPanel extends JPanel {
    private NewTheme theme;
    private final JLabel title = new JLabel("Soccer League Registration");

    private final TextFieldWithLabel usernameField = new TextFieldWithLabel(20, "Username:");

    private final PasswordFieldWithLabel passwordField = new PasswordFieldWithLabel(20, "Password:");
    private final PasswordFieldWithLabel passwordConfirmField = new PasswordFieldWithLabel(20, "Confirm Password:");

    private final TextFieldWithLabel firstNameField = new TextFieldWithLabel(20, "First Name:");
    private final TextFieldWithLabel lastNameField = new TextFieldWithLabel(20, "Last Name:");

    private final StyledButton registerButton = new StyledButton("Register");
    // Don't remove the space from the label, or it will move the UI when updated
    private final JLabel message = new JLabel(" ");

    private final JLabel loginPrompt = new JLabel("Already have an account?");
    private final StyledButton loginButton = new StyledButton("Login");
    List<ActionListener> loginListeners = new ArrayList<>();
    List<RegisterListener> registerListeners = new ArrayList<>();

    public RegisterPanel() {
        initComponents();
        initHandlers();
        this.setTheme(NewTheme.getActiveTheme());
    }

    private void initComponents(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Title
        gbc.weighty = 0.5;
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(title, gbc);

        // Username field
        gbc.weighty = 0;
        gbc.gridy++;
        add(usernameField, gbc);

        // Password Fields
        gbc.gridy++;
        add(passwordField, gbc);

        gbc.gridy++;
        add(passwordConfirmField, gbc);

        gbc.gridy++;
        add(firstNameField, gbc);

        gbc.gridy++;
        add(lastNameField, gbc);
        gbc.gridy++;
        add(registerButton, gbc);

        // Login response message
        gbc.gridy++;
        add(message, gbc);

        // Spacer
        gbc.gridy++;
        gbc.weighty = 0.5;
        add(Box.createGlue(), gbc);
        gbc.weighty = 0;
        gbc.gridy++;

        // Switch to register message/button
        gbc.gridwidth = 1;
        add(loginPrompt, gbc);
        gbc.gridx++;
        add(loginButton, gbc);
    }

    private void initHandlers(){
        this.loginButton.addActionListener(actionEvent -> {
            onLoginPressed();
        });
        this.registerButton.addActionListener(event -> {
            onRegisterPressed();
        });
    }

    public void addLoginListener(ActionListener listener){
        loginListeners.add(listener);
    }

    public void removeLoginListener(ActionListener listener){
        loginListeners.remove(listener);
    }

    public void addRegisterClickedListener(RegisterListener listener){
        registerListeners.add(listener);
    }

    public void removeRegisterClickedListener(RegisterListener listener){
        registerListeners.remove(listener);
    }

    private void onLoginPressed(){
        for(ActionListener listener : loginListeners){
            listener.actionPerformed(new ActionEvent(registerButton, ActionEvent.ACTION_PERFORMED, "LoginPressed"));
        }
    }

    private void onRegisterPressed(){
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        char[] confirmPassword = passwordConfirmField.getPassword();
        if(!Arrays.equals(password, confirmPassword)){
            setMessage("Passwords don't match", AuthenticationResult.AUTHENTICATION_STATUS.ERROR);
            return;
        }
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        for(RegisterListener listener : registerListeners){
            listener.registerRequested(username, password, firstName, lastName);
        }
    }

    public void setMessage(String message, AuthenticationResult.AUTHENTICATION_STATUS status){
        this.message.setText(message);
        this.message.setForeground(status == AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS ? theme.getSuccess() : theme.getError());
    }

    public void setMessage(AuthenticationResult result){
        this.message.setText(result.message());
        this.message.setForeground(result.status() == AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS ? theme.getSuccess() : theme.getError());
    }

    public void setTheme(NewTheme theme) {
        this.theme = theme;
        this.setBackground(theme.getBackgroundPrimary());

        title.setForeground(theme.getForegroundPrimary());

        usernameField.setTheme(theme);

        passwordField.setTheme(theme);
        passwordConfirmField.setTheme(theme);

        firstNameField.setTheme(theme);
        lastNameField.setTheme(theme);

        registerButton.setTheme(theme);
        loginPrompt.setForeground(theme.getForegroundSecondary());
        loginButton.setTheme(theme);

    }
}
