package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;

import javax.swing.*;
import java.util.List;

public class Window extends JFrame {
    TeamRosterView rosterView;
    public Window(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200,800);
        this.setLocationRelativeTo(null);
        Database db = new Database();
        List<Team> allTeams = db.getAllTeams();
        if(!allTeams.isEmpty()) {
            Team randomTeam = allTeams.get((int) Math.floor(Math.random() * allTeams.size()));
            this.rosterView = new TeamRosterView(randomTeam);
            this.add(rosterView);
        } else {
            throw new UnsupportedOperationException("Generate sample database first using DBGenerator.java");
        }
    }
}
