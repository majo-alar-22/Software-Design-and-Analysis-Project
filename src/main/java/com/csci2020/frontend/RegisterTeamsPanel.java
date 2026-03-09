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
    private final JTextField firstNameField;
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

        this.firstNameField = new JTextField(15);
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
        this.setBackground(new Color(235, 242, 250));

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(520, 420));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Soccer League Team Registry", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(titleLabel);


        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Position:"), gbc);

        gbc.gridx = 1;
        formPanel.add(positionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Team Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(teamNameField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(Color.WHITE);

        addPlayerButton.setPreferredSize(new Dimension(110, 35));
        saveButton.setPreferredSize(new Dimension(110, 35));

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
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String positionText = positionField.getText().trim().toUpperCase();
                String teamName = teamNameField.getText().trim();

                Player player = new Player(firstName, lastName);
                player.setPosition(Player.POSITION.valueOf(positionText));

                Team team = db.getTeamByName(teamName);
                if (team == null) {
                    team = new Team(teamName);
                }

                team.addPlayerToRoster(player);
                db.saveTeam(team);

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