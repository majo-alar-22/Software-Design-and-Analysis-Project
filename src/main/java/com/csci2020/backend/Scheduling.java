package com.csci2020.backend;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
public class Scheduling {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifies each scheduling instance

    @OneToOne
    @JoinColumn(name = "team_one_id", nullable = false)
    private Team teamOne; // One of the teams playing in the match

    @OneToOne
    @JoinColumn(name = "team_two_id", nullable = false)
    private Team teamTwo; // Other team that's playing in the match

    private LocalDateTime dateTime;

    public Scheduling() {} // Default constructor here

    // Constructor
    public Scheduling(Team teamOne, Team teamTwo, LocalDateTime dateTime) {
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.dateTime = dateTime;
    }

    // Getters and setters are below

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(Team teamOne) {
        this.teamOne = teamOne;
    }

    public Team getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(Team teamTwo) {
        this.teamTwo = teamTwo;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
