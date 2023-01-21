package bbb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class PlayersHandler {

	public static class Player {
		public String name;
		public int points;
		public int state; // 0 - not invested, 1 - invested, 2 - waiting to sell, 3 - shorting a stock
		public int investment; // amount invested
		public int beginSplit; // The split in which this player was invested when the split began. To be updated at each split.
	}

	private static ArrayList<Player> players;

	public static void initialize() {
		players = new ArrayList<Player>();
		loadAll();
	}

	public static ArrayList<Player> getPlayers() {
		return players;
	}
	
	public static boolean playing(String name) {
		// Returns if the given person is playing the game
		String playerNames = "";
		for(int i = 0; i < players.size(); i++)
			playerNames += players.get(i).name;
		playerNames = playerNames.toLowerCase();
		name = name.toLowerCase();
		if(playerNames.contains(name))
			return true;
		else
			return false;
	}

	public static boolean addPlayer(String newName) {
		boolean newPlayer = true;

		if (players != null) {
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).name.equals(newName)) {
					newPlayer = false;
					break;
				}
			}
		}

		if (newPlayer) {
			Player p = new Player();

			p.name = newName;
			p.points = 100;
			p.state = 0;
			p.investment = 0;
			p.beginSplit = -1;
			
			players.add(p);
			saveAll();

			return true;
		} else
			return false;
	}
	public static int getNumActivePlayers() {
		int numActivePlayers = 0;
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).state != 0) {
				numActivePlayers++;
			}
		}
		return numActivePlayers;
	}
	public static void saveAll() {

		try {
			File file = new File("Players.txt");
			FileWriter fw = new FileWriter(file, false);
			for (int i = 0; i < players.size(); i++) {
				fw.write(players.get(i).name + ":" + players.get(i).points + ":" + players.get(i).state + ":"
						+ players.get(i).investment + System.getProperty("line.separator"));
			}

			fw.flush();
			fw.close();
		} catch (Exception e) {
			System.err.println("Oops: " + e);
		}

	}

	public static void loadAll() {

		try {
			File file = new File("Players.txt");

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			while (br.ready()) {
				String line = br.readLine();

				Player p = new Player();

				p.name = line.split(":")[0];

				p.points = Integer.parseInt(line.split(":")[1]);

				p.state = Integer.parseInt(line.split(":")[2]);

				p.investment = Integer.parseInt(line.split(":")[3]);

				p.beginSplit = -1; // Initialize beginSplit value
				
				players.add(p);
			}

			br.close();
			fr.close();
		} catch (Exception e) {
			System.err.println("Oops: " + e);
		}
	}

	public static int getPoints(String newName) {

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(newName)) {
				return players.get(i).points;
			}
		}

		return 0;
	}

	// Add points to a specific player by name
	public static void addPoints(String playerName, int addPoints) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(playerName)) {
				players.get(i).points += addPoints;
				break;
			}
		}
		checkRankUp(playerName);
	}
	
	private static void checkRankUp(String name) {
		int myPlace = PlayersHandler.getPlacement(name);
		if(myPlace == 0)
			return;
		int upPlace = myPlace - 1;
		int myPoints = PlayersHandler.getPointsAt(myPlace);
		int upPoints = PlayersHandler.getPointsAt(upPlace);
		if(myPoints > upPoints) {
			Player me = players.get(myPlace);
			Player up = players.get(upPlace);
			players.set(upPlace, me);
			players.set(myPlace, up);
			checkRankUp(name);
		}
	}

	// Remove points from a specific player by name
	public static void removePoints(String playerName, int removePoints) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(playerName)) {
				players.get(i).points -= removePoints;
				break;
			}
		}
		handleLow(playerName);
		checkRankDown(playerName);
	}
	private static void checkRankDown(String name) {
		int myPlace = PlayersHandler.getPlacement(name);
		if(myPlace == players.size() - 1)
			return;
		int downPlace = myPlace + 1;
		int myPoints = PlayersHandler.getPointsAt(myPlace);
		int downPoints = PlayersHandler.getPointsAt(downPlace);
		if(myPoints < downPoints) {
			Player me = players.get(myPlace);
			Player down = players.get(downPlace);
			players.set(downPlace, me);
			players.set(myPlace, down);
			checkRankDown(name);
		}
	}

	// Get the state of a specific player
	public static int getState(String playerName) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(playerName)) {
				return players.get(i).state;

			}
		}

		return -1;
	}

	// Set the state of a specific player
	public static void setState(String playerName, int newState) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(playerName)) {
				players.get(i).state = newState;
				break;
			}
		}

	}

	// Gets the amount of money this player has invested (currently unused, but
	// usable for if the player wants to request this info)
	public static int getInvestment(String playerName) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(playerName)) {
				return players.get(i).investment;

			}
		}

		return -1;
	}
	
	public static int getInvestorCount() {
		int count = 0;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).state > 0)
				count++;
		}
		return count;
	}

	// Sets the amount of money this player has invested (currently unused, see
	// above)
	public static void setInvestment(String playerName, int newInvestment) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(playerName)) {
				players.get(i).investment = newInvestment;
				break;
			}
		}

	}

	// Add bonus points to all players who have currently invested
	public static void addPointsAll(int addPoints) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).state == 1) {
				players.get(i).points += addPoints;
			}
		}
	}

	// Set the split in which this player was invested when the split began
	public static void setBeginSplit(int split) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).state == 1) {
				players.get(i).beginSplit = split;
			}
		}
	}
	
	// Iteration handling methods, used from the file handler to iterate through the
	// vector externally,
	// Each method will return the specific data a the index location specified
	public static int getSize() {
		return players.size();
	}

	public static String getNameAt(int index) {
		return players.get(index).name;
	}

	public static int getPointsAt(int index) {
		return players.get(index).points;
	}

	public static int getStateAt(int index) {
		return players.get(index).state;
	}

	public static int getInvestmentAt(int index) {
		return players.get(index).investment;
	}
	
	public static void handleLow(String playerName) {
		// Gives someone who hits below an amount that amount (should be a dynamic amount -- will fix later)
		if (getPoints(playerName) < 30) 
			TwitchChat.outsidePM(playerName, playerName + ", looks like "+
					"you're low on points beanssMS . Luckily you "+
					"get 3 points for every minute watching, so "+
					"you'll be back in it in no time!");
	}
	
	
	public static boolean orderAllPlayers() {
		// Order the player list by number of points (returns whether ordered)
		
		ArrayList<Player> list = getPlayers(); // Temp list of players
		// Check if already ordered
		if(checkOrder(list))
			return true;
		// Order the temp player list
		for (int i = 0; i < list.size() - 1; i++)
        {
			int minIndex = i;
            int index = i;
            for (int j = i + 1; j < list.size(); j++) {
            	int a = list.get(j).points + list.get(j).investment;
            	int b = list.get(minIndex).points + list.get(minIndex).investment;
            	if (b < a)
            		minIndex = j;
            }
            Player smallerElement = list.get(index);
            list.set(index, list.get(minIndex));
            list.set(minIndex, smallerElement);
        }
		// Set players to our now ordered list
		boolean safe = checkOrder(list);
		if(safe) {
			players = list;
			return true;
		}
		else
			return false;
	}
	private static boolean checkOrder(ArrayList<Player> list) {
		// Confirm the order of the player list
		Player prevEntry = new Player(); // Temp player for the first check
		prevEntry.name = "x";
		prevEntry.points = 9999999;
		prevEntry.investment = 9999999;
		for (Player currEntry: list) {
    		int a = currEntry.points + currEntry.investment;
    		int b = prevEntry.points + prevEntry.investment;
    	    if (b < a) {
    	    	System.out.println("UHOH " + prevEntry.name + " lower than " + currEntry.name);
    	        return false;
    	    }
    	    prevEntry = currEntry;
    	}
		return true;
	}
	public static int getPlacement(String player) {
		if(orderAllPlayers()) {
			for(int i = 0; i < getSize(); i++) { // First place 0, but that's me so good
				if(players.get(i).name.equals(player))
					return i;
			}
			return 42069; // Error code for didn't find that player
		}
		else
			return 666123; // Error code for players not ordered
	}
	public static String getLeaderBoard() {
		if(orderAllPlayers()) {
			if(getSize() >= 5) {
				String lb = "";
				for(int i = 1; i < 5; i++)
					lb += "#" + i + ": " + players.get(i).name + " | ";
				lb += "#5: " + players.get(5).name;
				return lb;
			}
			else
				return "Need more players FeelsBadMan";
		}
		else
			return "Sorry but failed to order the players D:";
	}
}