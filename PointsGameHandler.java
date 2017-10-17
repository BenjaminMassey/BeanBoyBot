package bbb;

import java.util.Vector;

public class PointsGameHandler {
	
	public static class Player
	{
		public String name;
		public int points;
		public int state; // 0 - not invested, 1 - invested, 2 - waiting to sell
		public int investment; // amount invested
		
	}
	
	private static Vector<Player> players;
	
	public static void Initialize()
	{
		System.out.println("Constructor, attempting to load file");
		players = FileHandler.loadAll("SplitGame");
		
		if(players == null)
		{
			System.out.println("no file found, creating one");
			players = new Vector<Player>();
			
			FileHandler.saveAll("SplitGame", players);
		}
		
		
	}
	
	public static boolean addPlayer(String newName) {
		boolean newPlayer = true;
		//int playerNum = FileHandler.getFileLength("SplitGame");
		
		if(players != null)
		{
		
			for(int i = 0; i < players.size(); i++)
			{
				if(players.elementAt(i).name.equals(newName))
				{
					newPlayer = false;
					break;
				}
			}
		}
		
		
		if(newPlayer) 
		{
			Player p = new Player();
			
			p.name = newName;
			p.points = 100;
			p.state = 0;
			p.investment = 0;
			
			players.add(p);
			
			//FileHandler.appendToFile("SplitGame", p.name + ":" + p.points + ":" + p.state + ":" + p.investment + System.getProperty("line.separator") );
			FileHandler.saveAll("SplitGame", players);
			return true;
		}
		else
			return false;
	}
	
	public static int getPoints(String newName) {

		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(newName))
			{
				return players.elementAt(i).points;
			}
		}
		
		return 0;
	}
	
	public static boolean buyRun(String newName) {
		
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(newName))
			{
				if(players.elementAt(i).points - SplitGame.getCost() > 0 && players.elementAt(i).state == 0)
				{
					players.elementAt(i).points -= SplitGame.getCost();
					players.elementAt(i).state = 1;
					players.elementAt(i).investment = SplitGame.getCost(); 
					FileHandler.saveAll("SplitGame", players);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean sellRun(String newName) {
		
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(newName))
			{
				if(players.elementAt(i).state == 1)
				{
					// Right now just going to make it sell immediately, when we do delayed selling it will be set state = 2
					players.elementAt(i).points += SplitGame.getCost();
					players.elementAt(i).state = 0;
					players.elementAt(i).investment = 0;
					
					FileHandler.saveAll("SplitGame", players);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void sellAll() {
		
		for(int i = 0; i < players.size(); i++)
		{
			players.elementAt(i).points += SplitGame.getCost();
			players.elementAt(i).state = 0;
			players.elementAt(i).investment = 0;
		}
		
		FileHandler.saveAll("SplitGame", players);
	}
	

}
