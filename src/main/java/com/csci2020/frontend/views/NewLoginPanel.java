package com.csci2020.frontend.views;

import com.csci2020.backend.AuthenticationResult;
import com.csci2020.frontend.LoginListener;
import com.csci2020.frontend.NewTheme;
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
import java.util.List;

public class NewLoginPanel extends JPanel {
    private NewTheme theme;

    private final JLabel title = new JLabel("Soccer League Login");
    private final JLabel subtitle = new JLabel("Sign in to manage teams and matches");

    private final TextFieldWithLabel usernameField = new TextFieldWithLabel(20, "Username:");
    private final PasswordFieldWithLabel passwordField = new PasswordFieldWithLabel(20, "Password:");

    private final StyledButton loginButton = new StyledButton("Login");
    private final JLabel message = new JLabel(" ");

    private final JLabel registerPrompt = new JLabel("Don't have an account?");
    private final StyledButton registerButton = new StyledButton("Register");

    private final JPanel cardPanel = new JPanel();
    private final JPanel headerPanel = new JPanel();
    private final JPanel formPanel = new JPanel();
    private final JPanel footerPanel = new JPanel();

    private final List<LoginListener> loginListeners = new ArrayList<>();
    private final List<ActionListener> registerListeners = new ArrayList<>();

    public NewLoginPanel() {
        initComponents();
        initHandlers();
        this.setTheme(NewTheme.getActiveTheme());
    }

    private void initComponents() {
        setLayout(new GridBagLayout());

        cardPanel.setLayout(new BorderLayout(0, 18));
        cardPanel.setPreferredSize(new Dimension(460, 400));
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
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 12, 0);

        formPanel.add(usernameField, gbc);
        gbc.gridy++;
        formPanel.add(passwordField, gbc);
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
//        gbc.weighty = 1.0;
        loginButton.setPreferredSize(new Dimension(140, 40));
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.setOpaque(false);
        buttonRow.add(loginButton);
        gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalBox());
        gbc.weighty = 0.0;
        formPanel.add(buttonRow, gbc);
        gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalBox());
        gbc.weighty = 0.0;

        gbc.gridy++;
        gbc.insets = new Insets(4, 0, 0, 0);
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 13));
        message.setPreferredSize(new Dimension(300, 20));
        formPanel.add(message, gbc);

        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        footerPanel.add(registerPrompt);
        footerPanel.add(registerButton);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(footerPanel, BorderLayout.SOUTH);

        add(cardPanel);
    }

    private void initHandlers() {
        this.loginButton.addActionListener(actionEvent -> onLoginPressed());
        this.registerButton.addActionListener(event -> onRegisterPressed());

        Action loginAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoginPressed();
            }
        };

        usernameField.getField().addActionListener(loginAction);
        passwordField.getField().addActionListener(loginAction);
    }

    public void addLoginListener(LoginListener listener) {
        loginListeners.add(listener);
    }

    public void removeLoginListener(LoginListener listener) {
        loginListeners.remove(listener);
    }

    public void addRegisterClickedListener(ActionListener listener) {
        registerListeners.add(listener);
    }

    public void removeRegisterClickedListener(ActionListener listener) {
        registerListeners.remove(listener);
    }

    private void onLoginPressed() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        for (LoginListener listener : loginListeners) {
            listener.loginRequested(username, password);
        }
    }

    private void onRegisterPressed() {
        for (ActionListener listener : registerListeners) {
            listener.actionPerformed(new ActionEvent(registerButton, ActionEvent.ACTION_PERFORMED, "RegisterPressed"));
        }
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

        loginButton.setTheme(theme);
        registerButton.setTheme(theme);

        registerPrompt.setForeground(theme.getForegroundSecondary());

        if (" ".equals(message.getText())) {
            message.setForeground(theme.getForegroundSecondary());
        }
    }
}