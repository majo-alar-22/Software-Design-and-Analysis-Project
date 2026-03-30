package com.csci2020.database.unit;
import com.csci2020.backend.Player;
import com.csci2020.backend.Team;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    public void testPlayerCreation(){
        Player p = new Player("firstName","lastName");
        assertEquals("firstName",p.getFirstName());
        assertEquals("lastName",p.getLastName());
        assertEquals(0, p.getGoalCount());
        assertEquals(0, p.getShotCount());
        assertEquals(0, p.getSaveCount());
        assertEquals(0, p.getPenaltyCount());
    }

    @Test
    public void testPlayerCreationWithStats(){
        Player p = new Player("firstName","lastName", 2, 4, 8, 16);
        assertEquals("firstName",p.getFirstName());
        assertEquals("lastName",p.getLastName());
        assertEquals(2, p.getGoalCount());
        assertEquals(4, p.getShotCount());
        assertEquals(8, p.getSaveCount());
        assertEquals(16, p.getPenaltyCount());
    }
}
