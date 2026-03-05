package front_end;

import back_end.Database;
import back_end.Team;

import javax.swing.*;
import java.util.List;

public class Window extends JFrame {
    TeamRosterView rosterView;
    public Window(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
