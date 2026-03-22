package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;
import com.csci2020.backend.Scheduling;
import jakarta.persistence.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class GameSchedulingView extends JPanel {
    private final JScrollPane teamScroller;
    private final JTable rosterTable;
    private final TableModel tableModel;
    private final Database db;
//    private final TeamRosterView rosterView;

    // UI labels and text fields below
    private final JLabel team1Label;
    private final JTextField team1Field;
    private final JLabel team2Label;
    private final JTextField team2Field;
    private final JLabel dateLabel;
    private final JSpinner dateSpinner;
    JSpinner.DateEditor dateEditor;
    // Button to confirm a scheduled game
    private final JButton register;

    /**
     * Constructor
     *
     * @param tableModel
     * @param team
     * @param dateSpinner
     *
     **/
    public GameSchedulingView(TableModel tableModel, Team team, Database db, JSpinner dateSpinner) {
        this.teamScroller = new JScrollPane();
        this.rosterTable = new JTable();
        this.tableModel = tableModel;
        this.db = db;
        //      this.rosterView = new TeamRosterView(team);
        this.team1Label = new JLabel("Team 1:");
        this.team1Field = new JTextField();
        this.team1Field.setColumns(10);
        this.team2Label = new JLabel("Team 2:");
        this.team2Field = new JTextField();
        this.team2Field.setColumns(10);
        this.dateLabel = new JLabel("Date:");
        this.dateSpinner = dateSpinner;
        this.dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy HH:mm");
        dateSpinner.setEditor(dateEditor);
        this.register = new JButton("Register Game");
        this.setBackground(Theme.getActiveTheme().getBackgroundPrimary());

        add(team1Label);
        add(team1Field);
        add(team2Label);
        add(team2Field);
        add(register);
        // teamScroller.setViewportView(rosterView);
        add(teamScroller);
        add(dateLabel);
        add(dateSpinner);

        // Listener for when register is pressed
        register.addActionListener(e -> registerGame());
    }

    // Function called in register action listener, confirms the date for the game
    private void registerGame() {
        String teamOneName = team1Field.getText();
        String teamTwoName = team2Field.getText();

        // Checks if either of the name fields are empty and gives an error message if so
        if (teamOneName == null || teamTwoName == null) {
            JOptionPane.showMessageDialog(null, "You are missing one or both teams");
            return;
        }

        Team teamOne = db.getTeamByName(teamOneName);
        Team teamTwo = db.getTeamByName(teamTwoName);

        Date date = (Date) dateSpinner.getValue();

        LocalDateTime dateTime = date.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();

        db.scheduleGame(teamOne, teamTwo, dateTime);

        JOptionPane.showMessageDialog(null, "Game registered successfully!");
    }

    /****/
    public boolean recordExists(Connection connection, Team teamName) throws SQLException {
        String sql = "SELECT 1 FROM db WHERE teamName = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, teamName.getName());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
