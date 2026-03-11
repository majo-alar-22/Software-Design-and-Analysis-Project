package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TeamStandingsView extends JPanel {
    private final Database db;
    private final JTable standingsTable;
    private final JScrollPane standingsScroller;
    private final StandingsTableModel tableModel;
    private final JButton backButton;

    private static final String[] HEADERS = {
            "Team", "W", "D", "L", "GF", "GA", "GD", "Pts"
    };

    public TeamStandingsView(Database db) {
        this.db = db;
        this.tableModel = new StandingsTableModel(db.getAllTeams());
        this.standingsTable = new JTable(tableModel);
        this.standingsScroller = new JScrollPane(standingsTable);
        this.backButton = new JButton("Back");

        initUI();
        initHandler();
    }

    private void initUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Theme.getActiveTheme().getBackgroundPrimary());

        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(850, 540));
        cardPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());
        cardPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("Team Standings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Theme.getActiveTheme().getForegroundPrimary());

        JLabel subtitleLabel = new JLabel("League Table", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.getActiveTheme().getForegroundSecondary());

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        standingsTable.setRowHeight(28);
        standingsTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        standingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        standingsTable.getTableHeader().setReorderingAllowed(false);
        standingsTable.setBackground(Theme.getActiveTheme().getBackgroundSecondary());
        standingsTable.setForeground(Theme.getActiveTheme().getForegroundPrimary());
        standingsTable.setSelectionBackground(Theme.getActiveTheme().getBackgroundPrimary());
        standingsTable.setSelectionForeground(Theme.getActiveTheme().getForegroundSecondary());

        JTableHeader tableHeader = standingsTable.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
        tableHeader.setBackground(Theme.getActiveTheme().getBackgroundTertiary());
        tableHeader.setForeground(Theme.getActiveTheme().getForegroundPrimary());

        standingsScroller.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bottomPanel.setBackground(Theme.getActiveTheme().getBackgroundSecondary());

        backButton.setPreferredSize(new Dimension(140, 35));
        backButton.setBackground(Theme.getActiveTheme().getBackgroundTertiary());
        backButton.setForeground(Theme.getActiveTheme().getForegroundPrimary());

        bottomPanel.add(backButton);

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(standingsScroller, BorderLayout.CENTER);
        cardPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.add(cardPanel);
    }

    private void initHandler() {
        backButton.addActionListener((event) -> {
            Window window = (Window) SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.showLoggedInUserTeamRoster();
            }
        });
    }

    class StandingsTableModel extends AbstractTableModel {
        private final List<Team> teams;

        public StandingsTableModel(List<Team> teams) {
            this.teams = new ArrayList<>(teams);
            this.teams.sort(
                    Comparator.comparingInt(Team::getPoints).reversed()
                            .thenComparingInt(Team::getGoalDifference).reversed()
                            .thenComparingInt(Team::getGoalsFor).reversed()
                            .thenComparing(Team::getName)
            );
        }

        @Override
        public int getRowCount() {
            return teams.size();
        }

        @Override
        public int getColumnCount() {
            return HEADERS.length;
        }

        @Override
        public String getColumnName(int column) {
            return HEADERS[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Team team = teams.get(rowIndex);

            return switch (columnIndex) {
                case 0 -> team.getName();
                case 1 -> team.getWins();
                case 2 -> team.getDraws();
                case 3 -> team.getLosses();
                case 4 -> team.getGoalsFor();
                case 5 -> team.getGoalsAgainst();
                case 6 -> team.getGoalDifference();
                case 7 -> team.getPoints();
                default -> null;
            };
        }
    }
}