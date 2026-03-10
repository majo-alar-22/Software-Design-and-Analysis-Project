package com.csci2020.frontend;

import com.csci2020.backend.Database;
import com.csci2020.backend.Player;
import com.csci2020.backend.Team;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class TeamRosterView extends JPanel {
    private final JScrollPane rosterScroller;
    private final JTable roster;
    private final TeamRosterTableModel tableModel;
    private final JLabel teamNameLabel;
    private final Database db;

    private static final String[] HEADERS = {"ID", "First Name", "Last Name"};

    /** Constructor for Team Roster component
     * @param db: Instance of a Database object, controls access to the database
     * @param team: Team which you are viewing the roster of
     **/
    public TeamRosterView(Database db, Team team) {
        this.db = db;
        this.tableModel = new TeamRosterTableModel(team);
        this.roster = new JTable(tableModel);
        this.rosterScroller = new JScrollPane(roster);
        this.teamNameLabel = new JLabel(team.getName());

        init();
    }

    // Function organizing and initializing elements of the roster view
    private void init() {
        this.setLayout(new GridBagLayout());
        this.setBackground(new Color(235, 242, 250));

        // Panel for roster or "card" panel of a team with size, color and border
        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setPreferredSize(new Dimension(700, 450));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // setts font and size for the label of the team name
        teamNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        teamNameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        JLabel subtitleLabel = new JLabel("Team Roster", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.DARK_GRAY);

        // Panel for the header when viewing a team's roster
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(teamNameLabel);
        headerPanel.add(subtitleLabel);

        // Organizing rows of the roster view, with row height, font, and size
        roster.setRowHeight(28);
        roster.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roster.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roster.getTableHeader().setReorderingAllowed(false);

        JTableHeader tableHeader = roster.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Scroller for the roster view to see all players
        rosterScroller.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        cardPanel.add(headerPanel, BorderLayout.NORTH);
        cardPanel.add(rosterScroller, BorderLayout.CENTER);

        this.add(cardPanel);

        // Radio buttons to select theme page (to be completed later: Awrron)
//        JRadioButton lightCLR = new JRadioButton("Light");
//        JRadioButton darkCLR = new JRadioButton("Dark");
//        JRadioButton dracCLR = new JRadioButton("Dracula");
//        lightCLR.setBackground(Color.WHITE);
//        darkCLR.setBackground(Color.WHITE);
//        dracCLR.setBackground(Color.WHITE);
//        lightCLR.setSelected(true);
//        this.add(lightCLR);
//        this.add(dracCLR, BorderLayout.CENTER);
//        this.add(lightCLR, BorderLayout.SOUTH);
    }

    class TeamRosterTableModel extends AbstractTableModel {
        private final Team team;

        public TeamRosterTableModel(Team team) {
            this.team = team;
        }

        // Getters and setters below
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

            switch (col) {
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