package com.csci2020.backend;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "players")
public class Player {
    /**
     * Enumeration representing the position the player is assigned.
     */
    public enum POSITION{
        GOALKEEPER(1),
        FORWARD(2),
        DEFENDER(3),
        MIDFIELDER(4);
        public final int value;
        POSITION(int i) {
            this.value = i;
        }
    }

    /**
     * Primary key used to identify unique players in the database.
     * This must never be changed manually to avoid duplicate entries in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    /**
     * This player's first name
     */
    @Column(name = "firstName")
    private String firstName;

    /**
     * This player's last name
     */
    @Column(name = "lastName")
    private String lastName;

    /**
     * This player's team
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    /**
     * This player's position
     */
    @Enumerated
    @Column(name = "position")
    private POSITION position;

    /**
     * The number of goals this player has overall
     */
    @Column(name = "goals")
    private int goalCount;

    /**
     * The number of shots this player has overall
     */
    @Column(name = "shots")
    private int shotCount;

    /**
     * The number of saves this player has overall.
     */
    @Column(name = "saves")
    private int saveCount;

    /**
     * The number of penalties this player has overall.
     */
    @Column(name = "penalties")
    private int penaltyCount;

    /**
     * Default constructor. This should not be used, but is required for Hibernate to function.
     */
    @Deprecated(forRemoval = false)
    public Player() {

    }

    /**
     * Create a new player with the given names.
     * @param firstName Player's first name
     * @param lastName Player's last name
     */
    public Player(String firstName, String lastName){
        this(firstName, lastName, 0, 0, 0, 0);
    }

    /**
     * Create a new player with the given names and statistics.
     * @param firstName  Player's first name
     * @param lastName Player's last name
     * @param goals Player's goals
     * @param shots Player's shots
     * @param saves Player's saves
     * @param penalties Player's penalties
     */
    public Player(String firstName, String lastName, int goals, int shots, int saves, int penalties){
        this.firstName = firstName;
        this.lastName = lastName;
        this.goalCount = goals;
        this.shotCount = shots;
        this.saveCount = saves;
        this.penaltyCount = penalties;
    }

    // Getters
    /**
     * Gets this player's primary key
     * @return This player's unique identifier
     */
    public int getID(){
        return this.ID;
    }

    /**
     * Gets this player's first name
     * @return This player's first name
     */
    public String getFirstName(){
        return this.firstName;
    }

    /**
     * Gets this player's last name
     * @return This player's last name
     */
    public String getLastName(){
        return this.lastName;
    }

    /**
     * Gets this player's goals
     * @return This player's goals
     */
    public int getGoalCount(){
        return this.goalCount;
    }

    /**
     * Gets this player's shots
     * @return This player's shots
     */
    public int getShotCount(){
        return this.shotCount;
    }

    /**
     * Gets this player's saves
     * @return This player's saves
     */
    public int getSaveCount(){
        return this.saveCount;
    }

    /**
     * Gets this player's penalties
     * @return This player's penalties
     */
    public int getPenaltyCount(){
        return this.penaltyCount;
    }

    /**
     * Gets the team this player is on
     * @return This player's team
     */
    public Team getTeam(){
        return this.team;
    }

    public POSITION getPosition(){
        return this.position;
    }

    // Setters
    /**
     * Sets this player's first name
     * @param firstName New first name
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    /**
     * Sets this player's last name
     * @param lastName New last name
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    /**
     * Sets this player's goals
     * @param goals New goal count
     */
    public void setGoalCount(int goals){
        this.goalCount = goals;
    }

    /**
     * Sets this player's shots
     * @param shots New shot count
     */
    public void setShotCount(int shots){
        this.shotCount = shots;
    }

    /**
     * Sets this player's saves
     * @param saves New save count
     */
    public void setSaveCount(int saves){
        this.saveCount = saves;
    }

    /**
     * Sets this player's penalties
     * @param penalties New penalty count
     */
    public void setPenaltyCount(int penalties){
        this.penaltyCount = penalties;
    }

    /**
     * Sets this player's team.
     * @see Team#addPlayerToRoster(Player)
     * @param team New team
     */
    public void setTeam(Team team){
        this.team = team;
    }

    /**
     * Sets this player's position
     * @param position New position
     */
    public void setPosition(POSITION position){
        this.position = position;
    }

    /**
     * Returns this player formatted as a String
     * @return (ID: firstName lastName, Team)
     */
    @Override
    public String toString(){
        return String.format("(%d: %s %s, %s)", ID, firstName, lastName, team == null ? "No team" : team.getName());
    }

    public boolean equals(Player other){
        return Objects.equals(this.ID, other.ID);
    }
}