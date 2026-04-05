package com.csci2020.frontend;

import javax.swing.*;
import java.awt.*;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;
import com.csci2020.backend.TeamGameHistory;

public class TeamGameHistoryView extends JPanel {
    private final Database db;
    private final JTabbedPane tabbedPane = new  JTabbedPane();

    public TeamGameHistoryView(Database db, Team team) {
        System.out.println("TeamGameHistoryView() created");
        this.db = db;
        this.setLayout(new BorderLayout());
        TeamGameHistory historyPanel = new TeamGameHistory(db);
        historyPanel.addGameHistory(team);
        this.add(historyPanel, BorderLayout.CENTER);
    }
}
