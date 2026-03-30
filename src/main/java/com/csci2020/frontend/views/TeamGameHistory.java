package com.csci2020.frontend.views;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;
import com.csci2020.frontend.UpcomingGamesView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
    public void addGameHistory(){

    }
    // Getter
    public JTable getHistoryTable() {
        return historyTable;
    }
}
