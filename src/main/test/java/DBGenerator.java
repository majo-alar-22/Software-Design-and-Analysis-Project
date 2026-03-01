import com.csci2020.backend.Database;
import com.csci2020.backend.Player;
import com.csci2020.backend.Team;

import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args){
        List<String> remainingTeamNames = new ArrayList<>(List.of(teamNames));
        List<Team> teams = new ArrayList<>();
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            String teamName = randomItemFromList(remainingTeamNames);
            Team t = new Team(teamName);
            remainingTeamNames.remove(teamName);
            teams.add(t);
        }
        Database db = new Database();
        for(int i = 0; i < 100; i++){
            Team team = randomItemFromList(teams);
            Player p = new Player(randomItemFromArray(firstNames), randomItemFromArray(lastNames));
            team.addPlayerToRoster(p);
            players.add(p);
        }
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
