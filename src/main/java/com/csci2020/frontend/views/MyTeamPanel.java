package com.csci2020.frontend.views;

import com.csci2020.backend.Database;
import com.csci2020.backend.Player;
import com.csci2020.backend.Team;
import com.csci2020.frontend.Window;
import com.csci2020.frontend.components.TextFieldWithLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyTeamPanel extends JPanel {
    private Team team;
    private final Database db;

    public MyTeamPanel(Database db) {
        this.db = db;
        this.team = db.getCurrentUser().getPlayer().getTeam();
        this.setLayout(new BorderLayout());

        if (this.team == null) {
            createTeamSection();
        } else {
            viewTeamSection();
        }
    }

    public void createTeamSection() {
        JPanel section = new JPanel();
        section.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.4;
        gbc.insets = new Insets(10, 10, 10, 10);

        section.add(new JLabel("You are not currently in a team. Create one, or ask a captain to add you to theirs."), gbc);

        gbc.weighty = 0.2;
        gbc.gridy++;
        TextFieldWithLabel nameField = new TextFieldWithLabel(20, "Team Name");
        section.add(nameField, gbc);

        gbc.gridy++;
        gbc.weighty = 0.4;
        JButton createButton = new JButton("Create a team");
        section.add(createButton, gbc);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String teamName = nameField.getText().trim();

                if (teamName.isEmpty()) {
                    JOptionPane.showMessageDialog(MyTeamPanel.this, "Please enter a team name.");
                    return;
                }

                Team newTeam = new Team(teamName);
                newTeam.setCaptain(db.getCurrentUser().getPlayer());
                db.saveTeam(newTeam);

                team = newTeam;
                viewTeamSection();
            }
        });

        this.removeAll();
        this.add(section, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    public void viewTeamSection() {
        this.team = db.getCurrentUser().getPlayer().getTeam();

        JPanel section = new JPanel();
        section.setLayout(new BorderLayout());

        section.add(buildLeftColumn(), BorderLayout.LINE_START);
        section.add(buildRightColumn(), BorderLayout.CENTER);

        this.removeAll();
        this.add(section, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    private JPanel buildLeftColumn() {
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel teamNameLabel = new JLabel(team.getName(), SwingConstants.CENTER);
        teamNameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        leftColumn.add(teamNameLabel, gbc);

        gbc.gridy++;
        leftColumn.add(new NewTeamRosterView(db, team), gbc);

        return leftColumn;
    }

    private JPanel buildRightColumn() {
        JPanel rightColumn = new JPanel();
        rightColumn.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel upcomingLabel = new JLabel("Upcoming Matches");
        upcomingLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        rightColumn.add(upcomingLabel, gbc);

        gbc.gridy++;
        rightColumn.add(new MatchesView(db, team, MatchesView.TYPE.FUTURE), gbc);

        if (canEnterGameOutcomes()) {
            gbc.gridy++;
            rightColumn.add(buildGameOutcomePanel(), gbc);
        }

        return rightColumn;
    }

    private boolean canEnterGameOutcomes() {
        if (db.getCurrentUser() == null) {
            return false;
        }

        if (db.getCurrentUser().isAdmin()) {
            return true;
        }

        Player currentPlayer = db.getCurrentUser().getPlayer();
        if (currentPlayer == null || team == null || team.getCaptain() == null) {
            return false;
        }

        return team.getCaptain().getID() == currentPlayer.getID();
    }

    private JPanel buildGameOutcomePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Enter Game Outcome"));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Team> allTeams = db.getAllTeams();
        JComboBox<Team> opponentBox = new JComboBox<>();

        for (Team otherTeam : allTeams) {
            if (!otherTeam.getName().equals(team.getName())) {
                opponentBox.addItem(otherTeam);
            }
        }

        JSpinner myGoalsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        JSpinner opponentGoalsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
        JButton saveButton = new JButton("Save Result");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("My Team:"), gbc);

        gbc.gridx = 1;
        panel.add(new JLabel(team.getName()), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Opponent:"), gbc);

        gbc.gridx = 1;
        panel.add(opponentBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("My Goals:"), gbc);

        gbc.gridx = 1;
        panel.add(myGoalsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Opponent Goals:"), gbc);

        gbc.gridx = 1;
        panel.add(opponentGoalsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Team opponent = (Team) opponentBox.getSelectedItem();

                if (opponent == null) {
                    JOptionPane.showMessageDialog(MyTeamPanel.this, "Please choose an opponent team.");
                    return;
                }

                int myGoals = (Integer) myGoalsSpinner.getValue();
                int opponentGoals = (Integer) opponentGoalsSpinner.getValue();

                team.recordMatch(myGoals, opponentGoals);
                opponent.recordMatch(opponentGoals, myGoals);

                db.saveTeam(team);
                db.saveTeam(opponent);

                JOptionPane.showMessageDialog(MyTeamPanel.this, "Game result saved successfully.");

                Window window = (Window) SwingUtilities.getWindowAncestor(MyTeamPanel.this);
                if (window != null) {
                    window.showLoggedInPane();
                }
            }
        });

        return panel;
    }
}