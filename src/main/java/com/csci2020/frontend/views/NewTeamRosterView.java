package com.csci2020.frontend.views;

import com.csci2020.backend.Player;
import com.csci2020.backend.Team;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class NewTeamRosterView extends JPanel{
    private JTable playersTable = new JTable();
    private Team team;
    private JScrollPane playersContainer = new JScrollPane(playersTable);
    public NewTeamRosterView(Team team){
        this.team = team;
        playersTable.setModel(new TeamPlayerTableModel(team.getRoster()));
        this.add(playersContainer);
    }

    class TeamPlayerTableModel extends AbstractTableModel {

        private static final String[] HEADERS = {"ID", "First Name", "Last Name"};
        private final List<Player> players;

        public TeamPlayerTableModel(List<Player> players) {
            this.players = players;
        }

        // Getters and setters below
        @Override
        public int getColumnCount() {
            return HEADERS.length;
        }

        @Override
        public int getRowCount() {
            return players.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            Player player = players.get(row);
            return switch (col) {
                case 0 -> player.getID();
                case 1 -> player.getFirstName();
                case 2 -> player.getLastName();
                case 3 -> player.getPosition();
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return HEADERS[column];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }
}