package com.csci2020.frontend;

import com.csci2020.backend.Player;
import com.csci2020.backend.Team;
import com.csci2020.backend.Database;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterTeamsPanel extends JPanel {
    private final Database db;
    private final Window window;

    private final JTextField usernameField;
    private final JTextField positionField;
    private final JTextField teamNameField;
    private final JButton addPlayerButton;
    private final JButton viewTeamButton;
    private final JButton enterGameButton;

    public RegisterTeamsPanel(Database db, Window window) {
        this.db = db;
        this.window = window;

        this.usernameField = new JTextField(15);
        this.positionField = new JTextField(15);
        this.teamNameField = new JTextField(15);
        this.addPlayerButton = new JButton("Add Player");
        this.viewTeamButton = new JButton("View Team");
        this.enterGameButton = new JButton("Enter Game Outcome");

        initUI();
        initHandler();
    }

    private void initUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Theme.getActiveTheme().getBackgroundPrimary());

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(520, 460));
        cardPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());
        cardPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Soccer League Team Registry", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Theme.getActiveTheme().getForegroundPrimary());

        JPanel headerPanel = new JPanel(new GridLayout(1, 1, 0, 5));
        headerPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());
        headerPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        String[] labels = {"Username:", "Position:", "Team Name:"};
        for (String label : labels) {
            JLabel jlabel = new JLabel(label);
            jlabel.setForeground(Theme.getActiveTheme().getForegroundPrimary());
            formPanel.add(jlabel, gbc);
            gbc.gridy++;
        }

        gbc.weightx = 1;
        gbc.gridy = 0;
        gbc.gridx = 1;

        usernameField.setBorder(BorderFactory.createLineBorder(Theme.getActiveTheme().getAccentSecondary()));
        formPanel.add(usernameField, gbc);
        gbc.gridy++;

        positionField.setBorder(BorderFactory.createLineBorder(Theme.getActiveTheme().getAccentSecondary()));
        formPanel.add(positionField, gbc);
        gbc.gridy++;

        teamNameField.setBorder(BorderFactory.createLineBorder(Theme.getActiveTheme().getAccentSecondary()));
        formPanel.add(teamNameField, gbc);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());

        addPlayerButton.setPreferredSize(new Dimension(210, 35));
        viewTeamButton.setPreferredSize(new Dimension(210, 35));
        enterGameButton.setPreferredSize(new Dimension(210, 35));

        addPlayerButton.setBackground(Theme.getActiveTheme().getBackgroundTertiary());
        viewTeamButton.setBackground(Theme.getActiveTheme().getBackgroundTertiary());
        enterGameButton.setBackground(Theme.getActiveTheme().getBackgroundTertiary());

        buttonPanel.add(addPlayerButton);
        buttonPanel.add(viewTeamButton);
        buttonPanel.add(enterGameButton);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(cardPanel);
    }

    private void initHandler() {
        addPlayerButton.addActionListener((event) -> {
            try {
                String username = usernameField.getText().trim();
                String positionText = positionField.getText().trim().toUpperCase();
                String teamName = teamNameField.getText().trim();

                Player player = db.getPlayerByUsername(username);

                if (player == null) {
                    JOptionPane.showMessageDialog(this, "Player account not found.");
                    return;
                }

                player.setPosition(Player.POSITION.valueOf(positionText));

                Team team = db.getTeamByName(teamName);
                if (team == null) {
                    team = new Team(teamName);
                }

                team.addPlayerToRoster(player);
                db.saveTeam(team);
                db.savePlayer(player);

                JOptionPane.showMessageDialog(this, "Player added to team successfully.");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Invalid position. Use GOALKEEPER, FORWARD, DEFENDER, or MIDFIELDER.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to add player to team.");
            }
        });

        viewTeamButton.addActionListener((event) -> {
            window.showLoggedInUserTeamRoster();
        });

        enterGameButton.addActionListener((event) -> {
            window.showGameOutcomePanel();
        });
    }
}