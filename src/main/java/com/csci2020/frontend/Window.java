package com.csci2020.frontend;

import com.csci2020.backend.AuthenticationResult;
import com.csci2020.backend.Database;
import com.csci2020.backend.Player;
import com.csci2020.backend.Team;
import com.csci2020.frontend.views.LoggedInPanel;
import com.csci2020.frontend.views.NewLoginPanel;
import com.csci2020.frontend.views.RegisterPanel;

import javax.swing.*;

import java.awt.event.ActionEvent;

import static com.csci2020.backend.AuthenticationResult.AUTHENTICATION_STATUS;

public class Window extends JFrame {
    private final Database db;

    public Window(Database db) {
        this.db = db;
        this.setTitle("CSCI2020 App");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);
        if(db.isNewDatabase()) {
            showRegisterPane();
        } else {
            showLoginPane();
        }
    }


    public void showLoginPane(){
        NewLoginPanel panel = new NewLoginPanel();
        panel.addLoginListener((username, password)->{
            AuthenticationResult result = db.login(username, password);
            panel.setMessage(result);
            if(result.status() == AUTHENTICATION_STATUS.SUCCESS){
                showLoggedInPane();
            }
        });
        panel.addRegisterClickedListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showRegisterPane();
            }
        });
        this.setContentPane(panel);
        revalidate();
        repaint();
    }

    public void showLoggedInPane(){this.setContentPane(new LoggedInPanel(db));}

    public void showRegisterPane(){
        RegisterPanel panel = new RegisterPanel();
        panel.addLoginListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showLoginPane();
            }
        });
        panel.addRegisterClickedListener((username, password, firstName, lastName) -> {
            AuthenticationResult result = db.createNewAccount(username, firstName, lastName, password);
            panel.setMessage(result);
        });
        this.setContentPane(panel);
        revalidate();
        repaint();
    }

    public void showLoggedInUserTeamRoster() {
        if (db.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(this, "No user is currently logged in.");
            return;
        }

        int adminChoice = JOptionPane.showConfirmDialog(
                this,
                "Are you an admin?",
                "Admin Login",
                JOptionPane.YES_NO_OPTION
        );

        if (adminChoice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Admin access granted.");

            this.setContentPane(new RegisterTeamsPanel(db, this));
            this.revalidate();
            this.repaint();
            return;
        }

        Player player = db.getCurrentUser().getPlayer();

        if (player == null) {
            JOptionPane.showMessageDialog(this, "No player is linked to this account.");
            return;
        }

        Team team = player.getTeam();

        if (team == null) {
            JOptionPane.showMessageDialog(this, "This player is not assigned to a team.");
            return;
        }

        this.setContentPane(new TeamRosterView(db, team));
        this.revalidate();
        this.repaint();
    }

    public void showGameOutcomePanel() {
        this.setContentPane(new GameOutcomePanel(db, this));
        this.revalidate();
        this.repaint();
    }

    public void showStandingsView() {
        this.setContentPane(new TeamStandingsView(db));
        this.revalidate();
        this.repaint();
    }

    public void showTeamGameHistory() {
        Player player =  db.getCurrentUser().getPlayer();
        Team team = player.getTeam();
        this.setContentPane(new TeamGameHistoryView(db, team));
        this.revalidate();
        this.repaint();
    }

}