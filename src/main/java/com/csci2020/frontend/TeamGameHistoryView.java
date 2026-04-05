package com.csci2020.frontend;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;
import com.csci2020.backend.TeamGameHistory;

public class TeamGameHistoryView extends JPanel {
    private final Database db;
    private final JTabbedPane tabbedPane = new  JTabbedPane();
    private final JTable table;
    private final DefaultTableModel tableModel;


    public TeamGameHistoryView(Database db, Team team) {
        this.db = db;
        JPanel panel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Team Game History");
        panel.add(title, BorderLayout.NORTH);
        tableModel = new DefaultTableModel(new String[]{"Opponent", "Date and Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Team Game History",scrollPane);
        this.setLayout(new BorderLayout());
        this.add(tabbedPane,BorderLayout.CENTER);
    }
}
