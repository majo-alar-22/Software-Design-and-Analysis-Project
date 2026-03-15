package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GameOutcomePanel extends JPanel {
    private final Database db;
    private final Window window;

    private final JTextField teamOneField;
    private final JTextField teamTwoField;
    private final JTextField teamOneGoalsField;
    private final JTextField teamTwoGoalsField;
    private final JButton saveResultButton;
    private final JButton viewStandingsButton;

    public GameOutcomePanel(Database db, Window window) {
        this.db = db;
        this.window = window;

        this.teamOneField = new JTextField(15);
        this.teamTwoField = new JTextField(15);
        this.teamOneGoalsField = new JTextField(15);
        this.teamTwoGoalsField = new JTextField(15);
        this.saveResultButton = new JButton("Save Result");
        this.viewStandingsButton = new JButton("View Standings");

        initUI();
        initHandler();
    }

    private void initUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Theme.getActiveTheme().getBackgroundPrimary());

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(520, 420));
        cardPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());
        cardPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Enter Game Outcome", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Theme.getActiveTheme().getForegroundPrimary());

        JPanel headerPanel = new JPanel(new GridLayout(1, 1));
        headerPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());
        headerPanel.add(titleLabel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] labels = {"Team 1:", "Team 2:", "Team 1 Goals:", "Team 2 Goals:"};

        gbc.gridx = 0;
        gbc.gridy = 0;
        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setForeground(Theme.getActiveTheme().getForegroundPrimary());
            formPanel.add(jLabel, gbc);
            gbc.gridy++;
        }

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        formPanel.add(teamOneField, gbc);
        gbc.gridy++;
        formPanel.add(teamTwoField, gbc);
        gbc.gridy++;
        formPanel.add(teamOneGoalsField, gbc);
        gbc.gridy++;
        formPanel.add(teamTwoGoalsField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());

        saveResultButton.setPreferredSize(new Dimension(180, 35));
        viewStandingsButton.setPreferredSize(new Dimension(180, 35));

        saveResultButton.setBackground(Theme.getActiveTheme().getBackgroundTertiary());
        viewStandingsButton.setBackground(Theme.getActiveTheme().getBackgroundTertiary());

        buttonPanel.add(saveResultButton);
        buttonPanel.add(viewStandingsButton);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(cardPanel);
    }

    private void initHandler() {
        saveResultButton.addActionListener((event) -> {
            try {
                String teamOneName = teamOneField.getText().trim();
                String teamTwoName = teamTwoField.getText().trim();

                int teamOneGoals = Integer.parseInt(teamOneGoalsField.getText().trim());
                int teamTwoGoals = Integer.parseInt(teamTwoGoalsField.getText().trim());

                if (teamOneName.equalsIgnoreCase(teamTwoName)) {
                    JOptionPane.showMessageDialog(this, "A team cannot play against itself.");
                    return;
                }

                Team teamOne = db.getTeamByName(teamOneName);
                Team teamTwo = db.getTeamByName(teamTwoName);

                if (teamOne == null || teamTwo == null) {
                    JOptionPane.showMessageDialog(this, "One or both teams do not exist.");
                    return;
                }

                teamOne.recordMatch(teamOneGoals, teamTwoGoals);
                teamTwo.recordMatch(teamTwoGoals, teamOneGoals);

                db.saveTeam(teamOne);
                db.saveTeam(teamTwo);

                JOptionPane.showMessageDialog(this, "Game result saved successfully.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Goals must be whole numbers.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to save game result.");
            }
        });

        viewStandingsButton.addActionListener((event) -> window.showStandingsView());
    }
}