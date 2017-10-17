package bbb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class PlayersHandler {
	
	public static class Player
	{
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
		//int playerNum = FileHandler.getFileLength("SplitGame");
		
		if(players != null) {
			for(int i = 0; i < players.size(); i++) {
				if(players.get(i).name.equals(newName)) {
					newPlayer = false;
					break;
				}
			}
		}
		
		
		if(newPlayer) {
			Player p = new Player();
			
			p.name = newName;
			p.points = 100;
			p.state = 0;
			p.investment = 0;
			
			players.add(p);
			saveAll();
			
			return true;
		}
		else
			return false;
	}
	
	public static void saveAll() {
		
		try {
			File file = new File("SplitGame.txt");
			FileWriter fw = new FileWriter(file, false);
			for(int i = 0 ; i < players.size(); i++)
			{
				fw.write(players.get(i).name + ":" + players.get(i).points + ":"  + players.get(i).state + ":" + players.get(i).investment + System.getProperty("line.separator"));
			}
			
			fw.flush();
			fw.close();
		}
		catch (Exception e) {
			System.err.println("Oops: " + e);
		}
		
	}
	
	public static void loadAll() {
		
		try {
			File file = new File("SplitGame.txt");
			
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			while(br.ready()) {
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
		}
		catch (Exception e) {
			System.err.println("Oops: " + e);
		}
	}
	
	
}
