package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Scheduling;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UpcomingGamesView extends JPanel {
    private final Database db;
    private final JTable gamesTable;
    private final DefaultTableModel tableModel;

    public UpcomingGamesView(Database db) {
        this.db = db;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Upcoming Matches", SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"Team 1", "Team 2", "Date", "Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gamesTable = new JTable(tableModel);
        gamesTable.setRowHeight(24);

        add(new JScrollPane(gamesTable), BorderLayout.CENTER);

        loadUpcomingGames();
    }

    private void loadUpcomingGames() {
        tableModel.setRowCount(0);

        List<Scheduling> games = db.getAllMatches();

        System.out.println("Total matches from DB: " + games.size());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Scheduling game : games) {
            System.out.println("Match from DB: "
                    + game.getTeamOne().getName() + " vs "
                    + game.getTeamTwo().getName() + " at "
                    + game.getDateTime());

            String teamOneName = game.getTeamOne() == null ? "Unknown" : game.getTeamOne().getName();
            String teamTwoName = game.getTeamTwo() == null ? "Unknown" : game.getTeamTwo().getName();

            tableModel.addRow(new Object[]{
                    teamOneName,
                    teamTwoName,
                    game.getDateTime().format(dateFormatter),
                    game.getDateTime().format(timeFormatter)
            });
        }
    }

    public void refreshGames() {
        loadUpcomingGames();
    }
}