package com.csci2020.database.unit;

import com.csci2020.backend.Player;
import com.csci2020.backend.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TeamTest {
    @Test
    public void testTeamCreation(){
        Team team = new Team("Test Team");
        assertEquals("Test Team", team.getName());
        assertEquals(0, team.getRoster().size());
        assertNull(team.getCaptain());
    }

    @Test
    public void testAddToTeam(){
        Team team = new Team("Test Team");
        Player p = new Player("firstName", "lastName");
        team.addPlayerToRoster(p);
        assertEquals(team, p.getTeam());
        assertEquals(p, team.getRoster().getFirst());
        assertEquals(p, team.getCaptain());
    }

    @Test
    public void testMatchResults(){
        Team firstTeam = new Team("First Team");
        Team secondTeam = new Team("Second Team");
        firstTeam.recordMatch(5, 3);
        secondTeam.recordMatch(3, 5);
        assertEquals(2, firstTeam.getGoalDifference());
        assertEquals(-2, secondTeam.getGoalDifference());
    }
}
