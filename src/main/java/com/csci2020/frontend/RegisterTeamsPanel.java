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

    // Declaration of Fields associated with the account registry
    private final JTextField usernameField;
    private final JTextField lastNameField;
    private final JTextField positionField;
    private final JTextField teamNameField;
    private final JButton addPlayerButton;
    private final JButton saveButton;


    /** Constructor for the login panel
     * @param db: Instance of a Database object, controls access to the database
     * @param window: Window object used for the login panel
     **/
    public RegisterTeamsPanel(Database db, Window window) {
        this.db = db;
        this.window = window;

        this.usernameField = new JTextField(15);
        this.lastNameField = new JTextField(15);
        this.positionField = new JTextField(15);
        this.teamNameField = new JTextField(15);
        this.addPlayerButton = new JButton("Add New Player");
        this.saveButton = new JButton("Save Changes");

        initUI();
        initHandler();
    }

    // Arranges and initializes UI elements when called in the RegisterTeamsPanel() constructor
    private void initUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Theme.getActiveTheme().getBackgroundPrimary());

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(520, 420));
        cardPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());
        cardPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Soccer League Team Registry", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Theme.getActiveTheme().getForegroundPrimary());

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
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
        for(String label : labels){
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
//        lastNameField.setBorder(BorderFactory.createLineBorder(Theme.getActiveTheme().getAccentSecondary()));
//        formPanel.add(lastNameField, gbc);
//        gbc.gridy++;
        positionField.setBorder(BorderFactory.createLineBorder(Theme.getActiveTheme().getAccentSecondary()));
        formPanel.add(positionField, gbc);
        gbc.gridy++;
        teamNameField.setBorder(BorderFactory.createLineBorder(Theme.getActiveTheme().getAccentSecondary()));
        formPanel.add(teamNameField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(Color.WHITE);

        addPlayerButton.setPreferredSize(new Dimension(210, 35));
        saveButton.setPreferredSize(new Dimension(210, 35));
        addPlayerButton.setBackground(Theme.getActiveTheme().getBackgroundTertiary());
        saveButton.setBackground(Theme.getActiveTheme().getBackgroundTertiary());
        buttonPanel.add(addPlayerButton);
        buttonPanel.add(saveButton);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(formPanel, BorderLayout.CENTER);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(cardPanel);
    }

    // Helper function to listen for user actions when logging in
    private void initHandler() {
        addPlayerButton.addActionListener((event) -> {
            try {
                String firstName = usernameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String positionText = positionField.getText().trim().toUpperCase();
                String teamName = teamNameField.getText().trim();

                Player player = db.getPlayerByUsername(firstName);
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
                JOptionPane.showMessageDialog(this, "Invalid position. Use Goalkeeper, Forward, Defender, or Midfielder.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to add player to team.");
            }
        });

        saveButton.addActionListener((event) -> {
            try {
                Team team = new Team(teamNameField.getText().trim());
                db.saveTeam(team);
                JOptionPane.showMessageDialog(this, "Team saved successfully.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to save team.");
            }
        });
    }
}