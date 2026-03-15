package com.csci2020.frontend.views;

import com.csci2020.backend.Database;
import com.csci2020.backend.Scheduling;
import com.csci2020.backend.Team;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MatchesView extends JPanel {
    public enum TYPE {
        ALL,
        HISTORY,
        FUTURE
    }
    private JTable matches;
    private Database db;
    private Team team;
    private TYPE type;
    public MatchesView(Database db, Team team, TYPE type){
        this.db = db;
        this.team = team;
        this.type = type;
        matches =new JTable();
        List<Scheduling> matchesToShow = db.getAllMatches().stream().filter(match->{
            return match.getTeamOne().equals(team) || match.getTeamTwo().equals(team);
        }).toList();
        matches.setModel(new MatchesTableModel(matchesToShow));
        this.add(new JScrollPane(matches));
    }

    private class MatchesTableModel extends AbstractTableModel{
        private List<Scheduling> matches = new ArrayList<>();
        MatchesTableModel(List<Scheduling> matches){
            this.matches = matches;
        }
        final static String[] columns = {"Against", "Date"};

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public int getRowCount() {
            return matches.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            Scheduling match = matches.get(row);
            return switch(col){
                case 0 -> match.getTeamOne().equals(team) ? match.getTeamTwo() : match.getTeamOne();
                case 1 -> match.getDateTime().toString();
                default -> throw new IllegalArgumentException("Unexpected value: " + col);
            };
        }
    }
}
