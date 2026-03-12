package com.csci2020.frontend;

import com.csci2020.backend.Team;
import jakarta.persistence.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.time.LocalDateTime;
import java.sql.Timestamp;

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
    // Button to confirm a scheduled game
    private final JButton register;

    // Constructor
    public GameSchedulingView(TableModel tableModel, Team team) {
        this.teamScroller = new JScrollPane();
        this.rosterTable = new JTable();
        this.tableModel = tableModel;
        this.rosterView = new TeamRosterView(team);
        this.team1Label = new JLabel("Team 1:");
        this.team1Field = new JTextField();
        this.team2Label = new JLabel("Team 2:");
        this.team2Field = new JTextField();
        this.register = new JButton("Register Game");
    }


}
