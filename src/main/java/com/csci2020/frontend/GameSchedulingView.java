package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;
import com.csci2020.backend.Scheduling;
import jakarta.persistence.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;
import java.time.LocalDateTime;
import java.sql.Timestamp;
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

    /** Constructor
     * @param tableModel
     * @param team
     * @param dateSpinner
     * **/
    public GameSchedulingView(TableModel tableModel, Team team, Database db, JSpinner dateSpinner) {
        this.teamScroller = new JScrollPane();
        this.rosterTable = new JTable();
        this.tableModel = tableModel;
        this.db = db;
        //      this.rosterView = new TeamRosterView(team);
        this.team1Label = new JLabel("Team 1:");
        this.team1Field = new JTextField(15);
        this.team2Label = new JLabel("Team 2:");
        this.team2Field = new JTextField(15);
        this.dateLabel = new JLabel("Date:");
        this.dateSpinner = dateSpinner;
        this.dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy HH:mm");
        dateSpinner.setEditor(dateEditor);
        this.register = new JButton("Register Game");

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
        String teamOneName = team1Field.getText().trim();
        String teamTwoName = team2Field.getText().trim();

        if (teamOneName.isEmpty() || teamTwoName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You are missing one or both teams");
            return;
        }

        if (teamOneName.equalsIgnoreCase(teamTwoName)) {
            JOptionPane.showMessageDialog(null, "A team cannot play against itself");
            return;
        }

        Team teamOne = db.getTeamByName(teamOneName);
        Team teamTwo = db.getTeamByName(teamTwoName);

        if (teamOne == null || teamTwo == null) {
            JOptionPane.showMessageDialog(null, "Team(s) not found");
            return;
        }

        Date date = (Date) dateSpinner.getValue();
        LocalDateTime dateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        System.out.println("Saving: " + teamOne.getName() + " vs " + teamTwo.getName() + " at " + dateTime);

        db.saveMatch(teamOne, teamTwo, dateTime);

        JOptionPane.showMessageDialog(null, "Game registered successfully!");

        team1Field.setText("");
        team2Field.setText("");
    }
}
