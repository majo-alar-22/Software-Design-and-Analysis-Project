package com.csci2020.frontend.views;

import com.csci2020.backend.AuthenticationResult;
import com.csci2020.frontend.NewTheme;
import com.csci2020.frontend.RegisterListener;
import com.csci2020.frontend.components.PasswordFieldWithLabel;
import com.csci2020.frontend.components.StyledButton;
import com.csci2020.frontend.components.TextFieldWithLabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterPanel extends JPanel {
    private NewTheme theme;

    private final JLabel title = new JLabel("Soccer League Registration");
    private final JLabel subtitle = new JLabel("Create an account to join the league");

    private final TextFieldWithLabel usernameField = new TextFieldWithLabel(20, "Username:");
    private final PasswordFieldWithLabel passwordField = new PasswordFieldWithLabel(20, "Password:");
    private final PasswordFieldWithLabel passwordConfirmField = new PasswordFieldWithLabel(20, "Confirm Password:");
    private final TextFieldWithLabel firstNameField = new TextFieldWithLabel(20, "First Name:");
    private final TextFieldWithLabel lastNameField = new TextFieldWithLabel(20, "Last Name:");

    private final StyledButton registerButton = new StyledButton("Register");
    private final JLabel message = new JLabel(" ");

    private final JLabel loginPrompt = new JLabel("Already have an account?");
    private final StyledButton loginButton = new StyledButton("Login");

    private final JPanel cardPanel = new JPanel();
    private final JPanel headerPanel = new JPanel();
    private final JPanel formPanel = new JPanel();
    private final JPanel footerPanel = new JPanel();

    private final List<ActionListener> loginListeners = new ArrayList<>();
    private final List<RegisterListener> registerListeners = new ArrayList<>();

    public RegisterPanel() {
        initComponents();
        initHandlers();
        this.setTheme(NewTheme.getActiveTheme());
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        cardPanel.setLayout(new BorderLayout(0, 18));
        cardPanel.setPreferredSize(new Dimension(500, 520));
        cardPanel.setBorder(new EmptyBorder(28, 30, 24, 30));

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        headerPanel.setLayout(new GridLayout(2, 1, 0, 6));
        headerPanel.add(title);
        headerPanel.add(subtitle);

        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);

        formPanel.add(usernameField, gbc);
        gbc.gridy++;
        formPanel.add(passwordField, gbc);
        gbc.gridy++;
        formPanel.add(passwordConfirmField, gbc);
        gbc.gridy++;
        formPanel.add(firstNameField, gbc);
        gbc.gridy++;
        formPanel.add(lastNameField, gbc);
        gbc.gridy++;

        registerButton.setPreferredSize(new Dimension(150, 40));
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(registerButton);
        formPanel.add(buttonRow, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(4, 0, 0, 0);
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 13));
        message.setPreferredSize(new Dimension(320, 20));
        formPanel.add(message, gbc);

        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        footerPanel.add(loginPrompt);
        footerPanel.add(loginButton);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(footerPanel, BorderLayout.SOUTH);

        add(cardPanel);
    }

    private void initHandlers() {
        this.loginButton.addActionListener(actionEvent -> onLoginPressed());
        this.registerButton.addActionListener(event -> onRegisterPressed());

        Action registerAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRegisterPressed();
            }
        };

        usernameField.getField().addActionListener(registerAction);
        passwordField.getField().addActionListener(registerAction);
        passwordConfirmField.getField().addActionListener(registerAction);
        firstNameField.getField().addActionListener(registerAction);
        lastNameField.getField().addActionListener(registerAction);
    }

    public void addLoginListener(ActionListener listener) {
        loginListeners.add(listener);
    }

    public void removeLoginListener(ActionListener listener) {
        loginListeners.remove(listener);
    }

    public void addRegisterClickedListener(RegisterListener listener) {
        registerListeners.add(listener);
    }

    public void removeRegisterClickedListener(RegisterListener listener) {
        registerListeners.remove(listener);
    }

    private void onLoginPressed() {
        for (ActionListener listener : loginListeners) {
            listener.actionPerformed(new ActionEvent(registerButton, ActionEvent.ACTION_PERFORMED, "LoginPressed"));
        }
    }

    private void onRegisterPressed() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();
        char[] confirmPassword = passwordConfirmField.getPassword();

        if (!Arrays.equals(password, confirmPassword)) {
            setMessage("Passwords don't match", AuthenticationResult.AUTHENTICATION_STATUS.ERROR);
            return;
        }

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();

        for (RegisterListener listener : registerListeners) {
            listener.registerRequested(username, password, firstName, lastName);
        }
    }

    public void setMessage(String message, AuthenticationResult.AUTHENTICATION_STATUS status) {
        this.message.setText(message);
        this.message.setForeground(
                status == AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS
                        ? theme.getSuccess()
                        : theme.getError()
        );
    }

    public void setMessage(AuthenticationResult result) {
        this.message.setText(result.message());
        this.message.setForeground(
                result.status() == AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS
                        ? theme.getSuccess()
                        : theme.getError()
        );
    }

    public void setTheme(NewTheme theme) {
        this.theme = theme;

        this.setBackground(theme.getBackgroundPrimary());

        cardPanel.setBackground(theme.getSurfacePrimary());
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(theme.getBorderPrimary(), 1, true),
                new EmptyBorder(28, 30, 24, 30)
        ));

        headerPanel.setBackground(theme.getSurfacePrimary());
        formPanel.setBackground(theme.getSurfacePrimary());
        footerPanel.setBackground(theme.getSurfacePrimary());

        title.setForeground(theme.getForegroundPrimary());
        subtitle.setForeground(theme.getForegroundSecondary());

        usernameField.setTheme(theme);
        passwordField.setTheme(theme);
        passwordConfirmField.setTheme(theme);
        firstNameField.setTheme(theme);
        lastNameField.setTheme(theme);

        registerButton.setTheme(theme);
        loginButton.setTheme(theme);

        loginPrompt.setForeground(theme.getForegroundSecondary());

        if (" ".equals(message.getText())) {
            message.setForeground(theme.getForegroundSecondary());
        }
    }
}