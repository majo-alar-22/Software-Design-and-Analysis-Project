package com.csci2020.database;

import com.csci2020.backend.*;

import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class DBGenerator {
    public static final String[] firstNames = {
            "Liam", "Olivia", "Noah", "Emma", "Oliver",
            "Ava", "Elijah", "Sophia", "William", "Isabella",
            "James", "Mia", "Benjamin", "Charlotte", "Lucas",
            "Amelia", "Henry", "Harper", "Alexander", "Evelyn",
            "Michael", "Abigail", "Daniel", "Emily", "Matthew",
            "Ella", "Jackson", "Elizabeth", "Sebastian", "Camila",
            "Aiden", "Luna", "David", "Sofia", "Joseph",
            "Avery", "Samuel", "Mila", "Carter", "Aria",
            "Owen", "Scarlett", "Wyatt", "Penelope", "John",
            "Layla", "Jack", "Chloe", "Luke", "Victoria"
    };
    public static final String[] lastNames = {
            "Smith", "Johnson", "Williams", "Brown", "Jones",
            "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
            "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
            "Thomas", "Taylor", "Moore", "Jackson", "Martin",
            "Lee", "Perez", "Thompson", "White", "Harris",
            "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
            "Walker", "Young", "Allen", "King", "Wright",
            "Scott", "Torres", "Nguyen", "Hill", "Flores",
            "Green", "Adams", "Nelson", "Baker", "Hall",
            "Rivera", "Campbell", "Mitchell", "Carter", "Roberts"
    };
    public static String[] teamNames = {
            "Thunder Hawks", "Crimson Wolves", "Iron Titans", "Silver Spartans", "Shadow Panthers",
            "Blazing Falcons", "Golden Dragons", "Rapid Raptors", "Electric Eagles", "Midnight Marauders",
            "Phantom Knights", "Storm Breakers", "Fierce Lions", "Vortex Vipers", "Atomic Tigers",
            "Mystic Owls", "Iron Giants", "Raging Bulls", "Savage Sharks", "Blizzard Bears",
            "Lightning Leopards", "Crimson Cobras", "Obsidian Owls", "Steel Stallions", "Inferno Foxes",
            "Thunder Wolves", "Crystal Falcons", "Shadow Hawks", "Blaze Wolves", "Titan Terrors",
            "Silver Serpents", "Phantom Wolves", "Scarlet Sabers", "Frost Falcons", "Iron Wolves",
            "Viper Venom", "Storm Wolves", "Golden Griffins", "Night Stalkers", "Fire Falcons",
            "Venom Vultures", "Blizzard Wolves", "Cosmic Coyotes", "Lunar Lions", "Steel Raptors",
            "Savage Eagles", "Thunder Tigers", "Crimson Panthers", "Obsidian Wolves", "Mystic Wolves"
    };
    //Names above were generated with AI
    static Database db = new Database(Path.of(".","database"));
    public static void main(String[] args){
        List<String> remainingTeamNames = new ArrayList<>(List.of(teamNames));
        List<Team> teams = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();


        for(int i = 0; i < 10; i++){
            String teamName = randomItemFromList(remainingTeamNames);
            Team t = new Team(teamName);
            remainingTeamNames.remove(teamName);
            teams.add(t);
        }
        for(int i = 0; i < 1000; i++){
            Team team = randomItemFromList(teams);
            String firstName = randomItemFromArray(firstNames);
            String lastName = randomItemFromArray(lastNames);
            //TODO not this
            String password = firstName + lastName;
            Account acc = db.createNewAccount(firstName, lastName, password);
            accounts.add(acc);
            Player p = acc.getPlayer();
            team.addPlayerToRoster(p);
            players.add(p);
        }
        Account admin = db.createAdminAccount("Admin", "Account", "password");
        db.saveTeams(teams);
        db.savePlayers(players);
        for(Team t : db.getAllTeams()){
            if(t.getCaptain() != null) {
                System.out.printf("%s: %s %s%n", t.getName(), t.getCaptain().getFirstName(), t.getCaptain().getLastName());
            } else {
                System.out.printf("%s%n", t.getName());
            }
            for(Player p : t.getRoster()){
                System.out.printf("\t%s %s%n", p.getFirstName(), p.getLastName());
            }
        }

        db.login("Admin.Account", "password");

//        Arrays.stream(Player.class.getDeclaredFields()).forEach(field -> {
//            try {
//                field.setAccessible(true);
//                System.out.printf("%s: %s%n", field.getName(), field.get(db.getPlayersByFirstName("Steve").getFirst()));
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        db.deleteTeam(db.getPlayersByFirstName("Steve").getFirst().getTeam());
//        Arrays.stream(Player.class.getDeclaredFields()).forEach(field -> {
//            try {
//                field.setAccessible(true);
//                System.out.printf("%s: %s%n", field.getName(), field.get(db.getPlayersByFirstName("Steve").getFirst()));
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        });
        testLogger();
    }

    public static void testLogger(){
//        try{
//            throw new Exception("This is an exception!");
//        } catch (Exception e){
//            Logging.createLogger("DBGenerator", Path.of(".","generator.log")).log(Level.SEVERE, "Failed to perform some operation", e);
//        }
    }
    public static <T> T randomItemFromArray(T[] array){
        int index = (int) Math.floor(Math.random()*array.length);
        return array[index];
    }
    public static <T> T randomItemFromList(List<T> list){
        int index = (int) Math.floor(Math.random()*list.size());
        return list.get(index);
    }
}
