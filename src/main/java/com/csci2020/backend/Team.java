package com.csci2020.backend;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "teams")
public class Team {
    /**
     * Team name used as a unique identifier in the database
     */
    @Id
    @Column(name = "name")
    private String name;

    /**
     * The team's captain responsible for managing players on this team
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "captain_id")
    private Player captain;

    /**
     * A list of players on this team
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Player> roster = new ArrayList<>();

    /**
     * Default constructor. This should never be used, but is required by Hibernate.
     */
    @Deprecated(forRemoval = false)
    public Team(){
        this("Unnamed Team");
    }

    /**
     * Creates a team with the given name. This does not check for duplicate entries in the database and should
     * be validated before saving.
     * @param name
     */
    public Team(String name){
        this.name = name;
    }


    /**
     * Add a player to this team's roster.
     * @param player Player to add
     */
    public void addPlayerToRoster(Player player){
        if(this.roster.contains(player)){
            return;
        }

        //remove player from previous team
        boolean allowed = true;
        if(player.getTeam() != null) {
            allowed = player.getTeam().removePlayer(player);
        }

        if(allowed) {
            this.roster.add(player);
            player.setTeam(this);
            if(this.captain == null){
                this.captain = player;
            }
        }
    }

    /**
     * Attempts to remove a player from this team. If the roster would be empty, returns false and makes no changes.
     * If the player to be removed is the captain of the roster, it makes the second player of the roster
     * the new captain.
     * @param player Player to remove
     * @return <code>true</code> if successfully removed, <code>false</code> otherwise.
     */
    public boolean removePlayer(Player player){
        if(roster.size() <= 1)
            return false;
        this.roster.remove(player);
        player.setTeam(null);
        if(this.captain != null && this.captain == player){
            this.captain = roster.getFirst();
        }
        return true;
    }

    // Getters
    /**
     * Gets the name of this team
     * @return The team's name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the captain of this team
     * @return The captain of this team
     */
    public Player getCaptain(){
        return this.captain;
    }

    /**
     * Gets a list of all players on this team
     * @return A list of all players on this team
     */
    public List<Player> getRoster(){
        return this.roster;
    }

    // Setters

    /**
     * Sets the captain of this team
     * @param captain New captain
     */
    public void setCaptain(Player captain){
        if(captain == null)
            return;
        this.captain = captain;
        if(!this.roster.contains(captain)){
            addPlayerToRoster(captain);
        }
    }

    /**
     * Converts this team to a string.
     * @return This team's name
     */
    @Override
    public String toString(){
        return name;
    }
}
