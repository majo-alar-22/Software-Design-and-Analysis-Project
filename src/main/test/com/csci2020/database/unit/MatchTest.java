package com.csci2020.database.unit;

import com.csci2020.backend.Scheduling;
import com.csci2020.backend.Team;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MatchTest {
    @Test
    public void testMatchCreation(){
        Team firstTeam = new Team("First Team");
        Team secondTeam = new Team("Second Team");
        LocalDateTime date = LocalDateTime.now();
        Scheduling match = new Scheduling(firstTeam, secondTeam, date);
        assertEquals(date, match.getDateTime());
        assertEquals(firstTeam, match.getTeamOne());
        assertEquals(secondTeam, match.getTeamTwo());
    }
}
