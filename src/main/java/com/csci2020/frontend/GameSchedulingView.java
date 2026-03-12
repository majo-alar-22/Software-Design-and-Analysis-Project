package com.csci2020.frontend;

import com.csci2020.backend.Team;
import com.csci2020.backend.Scheduling;
import jakarta.persistence.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Date;

public class GameSchedulingView extends JPanel {
    private final JScrollPane teamScroller;
    private final JTable rosterTable;
    private final TableModel tableModel;
    private final TeamRosterView rosterView;
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

    // Constructor
    public GameSchedulingView(TableModel tableModel, Team team, JSpinner dateSpinner) {
        this.teamScroller = new JScrollPane();
        this.rosterTable = new JTable();
        this.tableModel = tableModel;
        this.rosterView = new TeamRosterView(team);
        this.team1Label = new JLabel("Team 1:");
        this.team1Field = new JTextField();
        this.team2Label = new JLabel("Team 2:");
        this.team2Field = new JTextField();
        this.dateLabel = new JLabel("Date:");
        this.dateSpinner = dateSpinner;
        this.dateEditor = new JSpinner.DateEditor(dateSpinner, "dd.MM.yyyy");
        this.register = new JButton("Register Game");

        add(teamScroller);
        add(team1Label);
        add(team1Field);
        add(team2Label);
        add(team2Field);
        add(register);
        teamScroller.setViewportView(rosterView);
        add(teamScroller);

        // Listener for when register is pressed
        register.addActionListener(e -> registerGame());
    }

    private void registerGame() {
        String teamOneName = team1Field.getText();
        String teamTwoName = team2Field.getText();
        String date = (String) dateSpinner.getValue();
        if (teamOneName.isEmpty() && teamTwoName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You are missing a team name");
        }
        else {
            // need to fill this in
        }
    }


}
