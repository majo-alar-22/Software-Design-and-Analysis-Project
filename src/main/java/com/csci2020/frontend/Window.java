package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Player;
import com.csci2020.backend.Team;

import javax.swing.*;

public class Window extends JFrame {
    private final Database db;

    /** Constructor for the window
     * @param db: instance of a Database object, controls access to the database
     **/
    public Window(Database db) {
        this.db = db;

        this.setTitle("CSCI2020 App");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);

        this.setContentPane(new LoginPanel(db, this));
    }

    // Function to display error messages for login or account issues
    public void showLoggedInUserTeamRoster() {
        if (db.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(this, "No user is currently logged in.");
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
}