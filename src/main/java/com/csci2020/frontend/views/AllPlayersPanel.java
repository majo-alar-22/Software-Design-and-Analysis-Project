package com.csci2020.frontend.views;

import com.csci2020.backend.Database;
import com.csci2020.backend.Player;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

//TODO add more columns
//TODO make it look nice
public class AllPlayersPanel extends JPanel {
    private JTable playersTable = new JTable();
    private Database db;
    private JScrollPane playersContainer = new JScrollPane(playersTable);
    public AllPlayersPanel(Database db){
        this.db = db;
        playersTable.setModel(new AllPlayerTableModel(db.getAllPlayers()));
        this.add(playersContainer);
    }

    class AllPlayerTableModel extends AbstractTableModel {

        private static final String[] HEADERS = {"ID", "First Name", "Last Name"};
        private final List<Player> players;

        public AllPlayerTableModel(List<Player> players) {
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
                default -> null;
            };
        }

        @Override
        public String getColumnName(int column) {
            return HEADERS[column];
        }

        @Override
        public void setValueAt(Object aValue, int row, int col) {
            Player p = players.get(row);

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
            return col != 0 && db.getCurrentUser().isAdmin();
        }
    }
}
