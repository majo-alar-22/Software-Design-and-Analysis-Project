package com.csci2020.backend;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "teams")
public class Team {

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Integer ID;

    @Id
    @Column(name = "name")
    String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "captain_id")
    Player captain;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<Player> roster = new ArrayList<>();

    public Team(){
        this("Unnamed Team");
    }
    public Team(String name){
        this.name = name;
    }

    public void setCaptain(Player captain){
        if(captain == null)
            return;
        this.captain = captain;
        if(!this.roster.contains(captain)){
            addPlayerToRoster(captain);
        }
    }

    /**
     * Add a player to this team's roster.
     * @param player
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

    public String getName(){
        return this.name;
    }

    public Player getCaptain(){
        return this.captain;
    }

    public List<Player> getRoster(){
        return this.roster;
    }

//    public Integer getID(){
//        return this.ID;
//    }

    @Override
    public String toString(){
        return name;
    }
}
