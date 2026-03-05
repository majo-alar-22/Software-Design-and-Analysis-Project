package front_end;

import back_end.Database;
import back_end.Team;
import back_end.Player;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class TeamRosterView extends JPanel {
    private final JScrollPane rosterScroller;
    private final JTable roster;
    private final TeamRosterTableModel tableModel;
    private final JLabel teamNameLabel;
    private static final String[] HEADERS = {"ID", "First Name", "Last Name", "Position", "Goals", "Penalties"};
    public TeamRosterView(Team team){
        this.tableModel = new TeamRosterTableModel(team);
        this.roster = new JTable(tableModel);
        roster.setRowHeight(25);
        roster.setFillsViewportHeight(true);
        roster.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        roster.getTableHeader().setFont(
                roster.getTableHeader().getFont().deriveFont(Font.BOLD, 14f)
        );

        roster.setFont(new Font("Georgia", Font.PLAIN, 13));


        this.rosterScroller = new JScrollPane(roster);
        this.teamNameLabel = new JLabel(team.getName());
        teamNameLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        teamNameLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        init();
        roster.setAutoCreateRowSorter(true);
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
    static class TeamRosterTableModel extends AbstractTableModel{
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
                case 3 -> {
                    Player.POSITION pos = player.getPosition();
                    if (pos == null) yield "";

                    String name = pos.name().toLowerCase();
                    yield name.substring(0,1).toUpperCase() + name.substring(1);
                }
                case 4 -> player.getGoalCount();
                case 5 -> player.getPenaltyCount();
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
            Database db = new Database();
            db.savePlayer(p);
            fireTableCellUpdated(row, col);
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col != 0;
        }
    }
}