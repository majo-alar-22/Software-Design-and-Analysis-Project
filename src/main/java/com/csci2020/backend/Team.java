package com.csci2020.backend;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "captain_id")
    private Player captain;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Player> roster = new ArrayList<>();

    @Column(name = "wins")
    private Integer wins = 0;

    @Column(name = "draws")
    private Integer draws = 0;

    @Column(name = "losses")
    private Integer losses = 0;

    @Column(name = "goalsFor")
    private Integer goalsFor = 0;

    @Column(name = "goalsAgainst")
    private Integer goalsAgainst = 0;

    @Column(name = "points")
    private Integer points = 0;

    @Deprecated(forRemoval = false)
    public Team() {
        this("Unnamed Team");
    }

    public Team(String name) {
        this.name = name;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.points = 0;
    }

    public void addPlayerToRoster(Player player) {
        if (this.roster.contains(player)) {
            return;
        }

        boolean allowed = true;
        if (player.getTeam() != null) {
            allowed = player.getTeam().removePlayer(player);
        }

        if (allowed) {
            this.roster.add(player);
            player.setTeam(this);
            if (this.captain == null) {
                this.captain = player;
            }
        }
    }

    public boolean removePlayer(Player player) {
        if (roster.size() <= 1) {
            return false;
        }

        this.roster.remove(player);
        player.setTeam(null);

        if (this.captain != null && this.captain == player) {
            this.captain = roster.getFirst();
        }

        return true;
    }

    public void recordMatch(int goalsScored, int goalsAllowed) {
        this.goalsFor = getGoalsFor() + goalsScored;
        this.goalsAgainst = getGoalsAgainst() + goalsAllowed;

        if (goalsScored > goalsAllowed) {
            this.wins = getWins() + 1;
            this.points = getPoints() + 3;
        } else if (goalsScored == goalsAllowed) {
            this.draws = getDraws() + 1;
            this.points = getPoints() + 1;
        } else {
            this.losses = getLosses() + 1;
        }
    }

    public int getGoalDifference() {
        return getGoalsFor() - getGoalsAgainst();
    }

    public String getName() {
        return this.name;
    }

    public Player getCaptain() {
        return this.captain;
    }

    public List<Player> getRoster() {
        return this.roster;
    }

    public int getWins() {
        return wins == null ? 0 : wins;
    }

    public int getDraws() {
        return draws == null ? 0 : draws;
    }

    public int getLosses() {
        return losses == null ? 0 : losses;
    }

    public int getGoalsFor() {
        return goalsFor == null ? 0 : goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst == null ? 0 : goalsAgainst;
    }

    public int getPoints() {
        return points == null ? 0 : points;
    }

    public void setCaptain(Player captain) {
        if (captain == null) {
            return;
        }

        this.captain = captain;

        if (!this.roster.contains(captain)) {
            addPlayerToRoster(captain);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}