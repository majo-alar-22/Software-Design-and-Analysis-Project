package com.csci2020.frontend;

import com.csci2020.backend.AuthenticationResult;
import com.csci2020.frontend.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class NewLoginPanel extends JPanel {
    private NewTheme theme;
    private final JLabel title = new JLabel("Soccer League Login");
    private final TextFieldWithLabel usernameField = new TextFieldWithLabel(20, "Username:");
    private final PasswordFieldWithLabel passwordField = new PasswordFieldWithLabel(20, "Password:");
    private final StyledButton loginButton = new StyledButton("Login");
    // Don't remove the space from the label, or it will move the UI when updated
    private final JLabel message = new JLabel(" ");

    private final JLabel registerPrompt = new JLabel("Don't have an account?");
    private final StyledButton registerButton = new StyledButton("Register");
    List<LoginListener> loginListeners = new ArrayList<>();
    List<ActionListener> registerListeners = new ArrayList<>();

    public NewLoginPanel() {
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
        gbc.gridy++;
        add(passwordField, gbc);
        gbc.gridy++;
        add(loginButton, gbc);

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
        add(registerPrompt, gbc);
        gbc.gridx++;
        add(registerButton, gbc);
    }

    private void initHandlers(){
        this.loginButton.addActionListener(actionEvent -> {
            onLoginPressed();
        });
        this.registerButton.addActionListener(event -> {
            onRegisterPressed();
        });
    }

    public void addLoginListener(LoginListener listener){
        loginListeners.add(listener);
    }

    public void removeLoginListener(LoginListener listener){
        loginListeners.remove(listener);
    }

    public void addRegisterClickedListener(ActionListener listener){
        registerListeners.add(listener);
    }

    public void removeRegisterClickedListener(ActionListener listener){
        registerListeners.remove(listener);
    }

    private void onLoginPressed(){
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        for(LoginListener listener : loginListeners){
            listener.loginRequested(username, password);
        }
    }

    private void onRegisterPressed(){
        for(ActionListener listener  : registerListeners){
            listener.actionPerformed(new ActionEvent(registerButton, ActionEvent.ACTION_PERFORMED, "RegisterPressed"));
        }
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
        loginButton.setTheme(theme);
        registerPrompt.setForeground(theme.getForegroundSecondary());
        registerButton.setTheme(theme);

    }
}
