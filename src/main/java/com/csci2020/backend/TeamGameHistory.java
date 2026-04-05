package com.csci2020.backend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class TeamGameHistory extends JPanel {
    private final Database db;
    private final DefaultTableModel tableModel;
    private final JTable historyTable;

    // Constructor
    public TeamGameHistory(Database db){
        this.db = db;
        tableModel = new DefaultTableModel(
                new String[]{"Against", "Date", "Time"}, 10
        );

        historyTable = new JTable(tableModel);
        setLayout(new BorderLayout(5,5));
    }

    // Function to check if a game has already happened, and add it to the table
    public void addGameHistory(Team team){
        tableModel.setRowCount(0); // clears existing rows
        List<Scheduling> matches = db.getAllMatches();

        for (Scheduling match : matches) {
            if (match.getTeamOne().equals(team) || match.getTeamTwo().equals(team)) {
                if (match.getDateTime().isBefore(LocalDateTime.now())){
                    Team opponent;
                    if (match.getTeamOne().getName().equals(team.getName())) {
                        opponent = match.getTeamTwo();
                    }
                    else{
                        opponent = match.getTeamOne();
                    }

                    LocalDateTime dt = match.getDateTime();

                    tableModel.addRow(new Object[]{
                            opponent.getName(),
                            dt.toLocalDate(),
                            dt.toLocalTime()
                    });


                }
            }
        }
    }
    // Getter
    public JTable getHistoryTable() {
        return historyTable;
    }
}
