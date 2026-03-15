package com.csci2020.frontend.views;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AllTeamsPanel extends JPanel {
    private JTable teamsTable = new JTable();
    private Database db;
    private JScrollPane teamsContainer = new JScrollPane(teamsTable);
    private NewTeamRosterView selectedTeamView;
    public AllTeamsPanel(Database db){
        this.db = db;
        this.setLayout(new BorderLayout());
        teamsTable.setModel(new AllTeamsTableModel(db.getAllTeams()));
        teamsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Team selectedTeam = ((AllTeamsTableModel)teamsTable.getModel()).getTeamAtRow(teamsTable.rowAtPoint(e.getPoint()));
                displayTeamRoster(selectedTeam);
            }
        });
        this.add(teamsContainer, BorderLayout.LINE_START);
    }

    public void displayTeamRoster(Team team){
        this.removeAll();
        this.add(teamsContainer, BorderLayout.LINE_START);
        this.selectedTeamView = new NewTeamRosterView(db, team);
        this.add(selectedTeamView, BorderLayout.LINE_END);
    }

    static class AllTeamsTableModel extends AbstractTableModel {
        private static final String[] HEADERS = {"Name", "Captain", "Player Count"};
        private final List<Team> teams;

        public AllTeamsTableModel(List<Team> teams) {
            this.teams = teams;
        }

        // Getters and setters below
        @Override
        public int getColumnCount() {
            return HEADERS.length;
        }

        @Override
        public int getRowCount() {
            return teams.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            Team team = teams.get(row);

            return switch (col) {
                case 0 -> team.getName();
                case 1 -> team.getCaptain().getFirstName() + " " + team.getCaptain().getLastName();
                case 2 -> team.getRoster().size();
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return HEADERS[column];
        }

        @Override
        public void setValueAt(Object aValue, int row, int col) {
            return;
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        public Team getTeamAtRow(int row){
            return teams.get(row);
        }

    }
}
