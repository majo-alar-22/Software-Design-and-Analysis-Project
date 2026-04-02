package com.csci2020.frontend.views;

import com.csci2020.backend.Database;
import com.csci2020.frontend.GameSchedulingView;
import com.csci2020.frontend.TeamStandingsView;
import com.csci2020.frontend.Tournament;
import com.csci2020.frontend.UpcomingGamesView;

import javax.swing.*;
import java.awt.*;

public class LoggedInPanel extends JPanel {
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final Database db;
    private UpcomingGamesView upcomingGamesView;

    public LoggedInPanel(Database db){
        this.db = db;
        this.setLayout(new BorderLayout());
        initComponents();
    }
    private void initComponents(){
        this.tabbedPane.addTab("Players", new AllPlayersPanel(db));
        this.tabbedPane.addTab("Teams", new AllTeamsPanel(db));
        this.tabbedPane.addTab("My Team", new MyTeamPanel(db));
        // this.tabbedPane.addTab("Matches", new JLabel("Not implemented yet"));
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        this.tabbedPane.addTab("Schedule Match", new GameSchedulingView(null,null,db,spinner));

        this.upcomingGamesView = new UpcomingGamesView(db);
        this.tabbedPane.addTab("Upcoming Matches", upcomingGamesView);

        this.tabbedPane.addChangeListener(e -> {
            if (this.tabbedPane.getSelectedComponent() == upcomingGamesView) {
                upcomingGamesView.refreshGames();
            }
        });

        tabbedPane.addTab("Tournament", new Tournament());
        this.add(tabbedPane);
    }
}
