package com.csci2020.frontend.views;

import com.csci2020.backend.Database;
import com.csci2020.backend.Team;
import com.csci2020.frontend.components.TextFieldWithLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyTeamPanel extends JPanel {
    Team team;
    Database db;
    public MyTeamPanel(Database db){
        this.db = db;
        this.team = db.getCurrentUser().getPlayer().getTeam();
        if(this.team == null){
            createTeamSection();
        } else {
            viewTeamSection();
        }
    }

    public void createTeamSection(){
        JPanel section = new JPanel();
        section.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.4;
        section.add(new JLabel("You are not currently in a team. Create one, or ask a captain to add you to theirs."), gbc);
        gbc.weighty = 0.2;
        gbc.gridy++;
        TextFieldWithLabel nameField = new TextFieldWithLabel(20, "Team Name");
        section.add(nameField, gbc);
        gbc.gridy++;
        gbc.weighty = 0.4;
        JButton createButton = new JButton("Create a team");
        section.add(createButton);
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Team team = new Team(nameField.getText());
                team.setCaptain(db.getCurrentUser().getPlayer());
                db.saveTeam(team);
                viewTeamSection();
            }
        });
        this.removeAll();
        this.add(section);
    }

    public void viewTeamSection(){
        JPanel section = new JPanel();
        section.setLayout(new BorderLayout());
        section.add(buildLeftColumn(), BorderLayout.LINE_START);
        section.add(buildRightColumn(), BorderLayout.LINE_END);
        this.removeAll();
        this.add(section);
    }

    private JPanel buildLeftColumn(){
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftColumn.add(new JLabel(team.getName()), gbc);
        gbc.gridy++;
        leftColumn.add(new NewTeamRosterView(db, team), gbc);

        return leftColumn;
    }

    private JPanel buildRightColumn(){
        JPanel rightColumn = new JPanel();
        rightColumn.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightColumn.add(new JLabel("Upcoming Matches"), gbc);
        gbc.gridy++;
        rightColumn.add(new MatchesView(db, team, MatchesView.TYPE.FUTURE));
        return rightColumn;
    }
}
