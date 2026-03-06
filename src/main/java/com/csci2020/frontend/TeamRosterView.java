package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Player;
import com.csci2020.backend.Team;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class TeamRosterView extends JPanel {
    private final JScrollPane rosterScroller;
    private final JTable roster;
    private final TeamRosterTableModel tableModel;
    private final JLabel teamNameLabel;
    private final Database db;
    private static final String[] HEADERS = {"ID", "First Name", "Last Name"};
    public TeamRosterView(Database db, Team team){
        this.tableModel = new TeamRosterTableModel(team);
        this.db = db;
        this.roster = new JTable(tableModel);
        this.rosterScroller = new JScrollPane(roster);
        this.teamNameLabel = new JLabel(team.getName());
        init();
    }
    private void init(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        teamNameLabel.setHorizontalAlignment(JLabel.CENTER);
        teamNameLabel.setVerticalAlignment(JLabel.BOTTOM);
        teamNameLabel.setFont(teamNameLabel.getFont().deriveFont(20.0F));
        this.add(teamNameLabel, gbc);
        gbc.gridy++;
        gbc.weighty = 1.0;
        this.add(rosterScroller,gbc);
    }
    class TeamRosterTableModel extends AbstractTableModel{
        private final Team team;
        public TeamRosterTableModel(Team team){
            this.team = team;
        }
        @Override
        public int getColumnCount() {
            return HEADERS.length;
        }

        @Override
        public int getRowCount() {
            return team.getRoster().size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            Player player = team.getRoster().get(row);
            return switch (col) {
                case 0 -> player.getID();
                case 1 -> player.getFirstName();
                case 2 -> player.getLastName();
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return HEADERS[column];
        }

        @Override
        public void setValueAt(Object aValue, int row, int col) {
            Player p = team.getRoster().get(row);
            switch(col){
                case 0:
                    throw new UnsupportedOperationException("Cannot change ID");
                case 1:
                    p.setFirstName(aValue.toString().trim());
                    break;
                case 2:
                    p.setLastName(aValue.toString().trim());
                    break;
                default:
                    break;
            }
            db.savePlayer(p);
            fireTableCellUpdated(row, col);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col != 0;
        }
    }
}
