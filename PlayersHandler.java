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
		public int state; // 0 - not invested, 1 - invested, 2 - waiting to sell
		public int investment; // amount invested

	}

	private static ArrayList<Player> players;

	public static void initialize() {
		players = new ArrayList<Player>();
		loadAll();
	}

	public static ArrayList<Player> getPlayers() {
		return players;
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

			players.add(p);
			saveAll();

			return true;
		} else
			return false;
	}

	public static void saveAll() {

		try {
			File file = new File("SplitGame.txt");
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
			File file = new File("SplitGame.txt");

			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			while (br.ready()) {
				String line = br.readLine();

				Player p = new Player();

				p.name = line.split(":")[0];

				p.points = Integer.parseInt(line.split(":")[1]);

				p.state = Integer.parseInt(line.split(":")[2]);

				p.investment = Integer.parseInt(line.split(":")[3]);

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
	}

	// Remove points from a specific player by name
	public static void removePoints(String playerName, int removePoints) {
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(playerName)) {
				players.get(i).points -= removePoints;
				break;
			}
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

}
