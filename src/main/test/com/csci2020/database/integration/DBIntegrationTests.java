package com.csci2020.database.integration;

import com.csci2020.backend.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DBIntegrationTests {
    public Database db;
    public static final Path dbPath = Path.of(".","test");

    @BeforeEach
    public void setupTests(){
        System.out.println("Setting up...");
        db = new Database(dbPath, true);
    }

    @AfterEach
    public void cleanupTests(){
        db.getFactory().close();
        File dbFile = new File(dbPath+".mv.db");
        System.out.println(dbFile);
        assertTrue(dbFile.delete());
    }

    @Test
    public void createPlayer(){
        db.createNewAccount("username", "FirstName", "LastName", "password".toCharArray(), true);
        Player byUsername = db.getPlayerByUsername("username");
        Player byName = db.getPlayersByFirstName("FirstName").getFirst();
        assertEquals(1, byUsername.getID());
        assertEquals(1, byName.getID());
        //return String.format("(%d: %s %s, %s)", ID, firstName, lastName, team == null ? "No team" : team.getName());
        assertEquals("(1: FirstName LastName, No team)", byUsername.toString());
    }

    @Test
    public void createTeam(){
        db.createTeam("Test Team", null);
        assertEquals("Test Team", db.getAllTeams().getFirst().toString());
    }

    @Test
    public void addPlayersToTeam(){
        db.createNewAccount("username", "FirstName", "LastName", "password".toCharArray());
        db.createNewAccount("playertwo", "Second", "Player", "password".toCharArray());
        Player p = db.getPlayerByUsername("username");
        Player two = db.getPlayerByUsername("playertwo");
        Team team = new Team("Test Team");
        team.addPlayerToRoster(p);
        db.saveTeam(team);
        Team managedTeam = db.getTeamByName("Test Team");
        assertEquals(1, managedTeam.getRoster().size());
        managedTeam.addPlayerToRoster(two);
        db.saveTeam(managedTeam);
        assertEquals(2, db.getTeamByName("Test Team").getRoster().size());
        assertTrue(db.getTeamByName("Test Team").getCaptain().equals(p));
    }

    @Test
    public void testLogin(){
        db.createNewAccount("admin", "Admin", "Account", "password".toCharArray(), true);
        Account admin = db.getAccount("admin");
        assertEquals(AuthenticationResult.AUTHENTICATION_STATUS.INVALID_CREDENTIALS, db.login("admin", "notPassword".toCharArray()).status());
        assertNull(db.getCurrentUser());
        assertEquals(AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS, db.login("admin", "password".toCharArray()).status());
        Account test = db.getCurrentUser();
        System.out.println(test.toString());
        System.out.println(admin.toString());
        assertEquals(admin, db.getCurrentUser());
    }

    @Test
    public void testAdmin(){
        db.createNewAccount("admin", "Admin", "Account", "password".toCharArray(), true);
        assertTrue(db.getAccount("admin").isAdmin());
        // Next line shouldn't create an admin account despite admin being set to true, since the database is not new
        db.createNewAccount("notadmin", "NotAdmin", "Account", "password".toCharArray(), false);
        assertFalse(db.getAccount("notadmin").isAdmin());

        db.login("admin", "password".toCharArray());

        // Should create admin account since the current user is admin
        db.createNewAccount("adminAgain", "Admin", "Again", "password".toCharArray(), true);
        assertTrue(db.getAccount("adminAgain").isAdmin());

        db.login("notadmin", "password".toCharArray());

        // Should be false since logged-in user is not an admin and db is not new
        db.createNewAccount("notAdminAgain", "NotAdmin", "Again", "password".toCharArray(), true);

        assertFalse(db.getAccount("notAdminAgain").isAdmin());
    }

    @Test
    public void testDuplicateUsername(){
        assertEquals(AuthenticationResult.AUTHENTICATION_STATUS.SUCCESS, db.createNewAccount("username", "FirstName", "LastName", "password".toCharArray()).status());
        assertEquals(AuthenticationResult.AUTHENTICATION_STATUS.INVALID_CREDENTIALS, db.createNewAccount("username", "FirstName", "LastName", "password".toCharArray()).status());
    }
}
