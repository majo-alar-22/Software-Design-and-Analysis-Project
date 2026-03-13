package com.csci2020.frontend.views;

import com.csci2020.backend.Database;
import com.csci2020.frontend.TeamStandingsView;

import javax.swing.*;
import java.awt.*;

public class LoggedInPanel extends JPanel {
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final Database db;
    public LoggedInPanel(Database db){
        this.db = db;
        this.setLayout(new BorderLayout());
        initComponents();
    }
    private void initComponents(){
        this.tabbedPane.addTab("Players", new AllPlayersPanel(db));
        this.tabbedPane.addTab("Teams", new AllTeamsPanel(db));
        this.tabbedPane.addTab("My Team", new MyTeamPanel(db));
        this.tabbedPane.addTab("Matches", new JLabel("Not implemented yet"));
        this.add(tabbedPane);
    }
}
