package com.csci2020.backend;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {
    enum POSITION{
        GOALKEEPER(1),
        FORWARD(2),
        DEFENDER(3);
        public final int value;
        POSITION(int i) {
            this.value = i;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer ID;
    public String username;
    @Column(name = "firstName")
    public String firstName;
    @Column(name = "lastName")
    public String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = true)
    Team team;

    @Column(name = "goals")
    int goalCount;
    @Column(name = "shots")
    int shotCount;
    @Column(name = "saves")
    int saveCount;
    @Column(name = "penalties")
    int penaltyCount;

    public Player() {

    }
    public Player(String firstName, String lastName){
        this(firstName, lastName, 0, 0, 0, 0);
    }
    public Player(String firstName, String lastName, int goals, int shots, int saves, int penalties){
        this.firstName = firstName;
        this.lastName = lastName;
        this.goalCount = goals;
        this.shotCount = shots;
        this.saveCount = saves;
        this.penaltyCount = penalties;
    }

    public int getID(){
        return this.ID;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public int getGoalCount(){
        return this.goalCount;
    }
    public int getShotCount(){
        return this.shotCount;
    }
    public int getSaveCount(){
        return this.saveCount;
    }
    public int getPenaltyCount(){
        return this.penaltyCount;
    }
    public Team getTeam(){
        return this.team;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setGoalCount(int goals){
        this.goalCount = goals;
    }
    public void setShotCount(int shots){
        this.shotCount = shots;
    }
    public void setSaveCount(int saves){
        this.saveCount = saves;
    }
    public void setPenaltyCount(int penalties){
        this.penaltyCount = penalties;
    }

    public void setTeam(Team team){
        this.team = team;
    }
    @Override
    public String toString(){
        return String.format("(%d: %s %s, %s)", ID, firstName, lastName, team.getName());
    }
}