package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;

import javax.swing.*;
import java.util.List;

public class Window extends JFrame {
    TeamRosterView rosterView;
    private final Database db;
    public Window(Database db){
        this.db = db;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200,800);
        this.setLocationRelativeTo(null);
//        List<Team> allTeams = db.getAllTeams();
//        if(!allTeams.isEmpty()) {
//            Team randomTeam = allTeams.get((int) Math.floor(Math.random() * allTeams.size()));
//            this.rosterView = new TeamRosterView(db, randomTeam);
//            this.add(rosterView);
//        } else {
//            throw new UnsupportedOperationException("Generate sample database first using DBGenerator.java");
//        }
        LoginPanel loginPanel =new LoginPanel(db);
        this.add(loginPanel);
    }
}
