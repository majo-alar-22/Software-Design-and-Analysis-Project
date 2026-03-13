package com.csci2020.frontend;

import com.csci2020.backend.Team;
import com.csci2020.backend.Scheduling;
import jakarta.persistence.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.Date;

public class GameSchedulingView extends JPanel {
    private final JScrollPane teamScroller;
    private final JTable rosterTable;
    private final TableModel tableModel;
    private final TeamRosterView rosterView;
    // UI labels and text fields below
    private final JLabel team1Label;
    private final JTextField team1Field;
    private final JLabel team2Label;
    private final JTextField team2Field;
    private final JLabel dateLabel;
    private final JSpinner dateSpinner;
    JSpinner.DateEditor dateEditor;
    // Button to confirm a scheduled game
    private final JButton register;

    // Constructor
    public GameSchedulingView(TableModel tableModel, Team team, JSpinner dateSpinner) {
        this.teamScroller = new JScrollPane();
        this.rosterTable = new JTable();
        this.tableModel = tableModel;
        this.rosterView = new TeamRosterView(team);
        this.team1Label = new JLabel("Team 1:");
        this.team1Field = new JTextField();
        this.team2Label = new JLabel("Team 2:");
        this.team2Field = new JTextField();
        this.dateLabel = new JLabel("Date:");
        this.dateSpinner = dateSpinner;
        this.dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        this.register = new JButton("Register Game");

        add(team1Label);
        add(team1Field);
        add(team2Label);
        add(team2Field);
        add(register);
        teamScroller.setViewportView(rosterView);
        add(teamScroller);

        // Listener for when register is pressed
        register.addActionListener(e -> registerGame());
    }

    // Function called in register action listener, confirms the date for the game
    private void registerGame() {
        String teamOneName = team1Field.getText();
        String teamTwoName = team2Field.getText();

        // Checks if either of the name fields are empty and gives an error message if so
        if (teamOneName.isEmpty() || teamTwoName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "You are missing one or both team names");
            return;
        }

        // EntityManagerFactory to retrieve data from the database
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("csci2020.backend");
        EntityManager em = emf.createEntityManager();
        // Creates query to search for the given team in the text field and assign it to team one
        Team teamOne = em.createQuery(
                "SELECT t FROM Team t WHERE t.name = :name", Team.class
        ).setParameter("name", Team.class).getSingleResult();

        // Creates query to search for the given team in the text field and assign it to team two
        Team teamTwo = em.createQuery(
                "SELECT t FROM t WHERE t.class = :name", Team.class
        ).setParameter("name", Team.class).getSingleResult();

        Date date = (Date) dateSpinner.getValue();

        LocalDateTime dateTime = date.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();

        Scheduling scheduling = new Scheduling(teamOne, teamTwo, dateTime);

        em.getTransaction().begin();
        em.persist(scheduling);
        em.getTransaction().commit();

        JOptionPane.showMessageDialog(null, "Game registered successfully!");
    }
}
