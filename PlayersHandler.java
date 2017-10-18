package bbb;

import java.util.Vector;

public class PlayersHandler {
	
	private static class Player
	{
		public String name;
		public int points;
		public int state; // 0 - not invested, 1 - invested, 2 - waiting to sell
		public int investment; // amount invested
		
	}
	
	private static Vector<Player> players;
	
	public static void Initialize()
	{
		players = new Vector<Player>();
	}
	
	// Adds a player with premade stats (used for loading players from file)
	public static void addPlayer(String playerName, int playerPoints, int playerState, int playerInvestment)
	{
		
		Player p = new Player();
		
		p.name = playerName;
		p.points = playerPoints;
		p.state = playerState;
		p.investment = playerInvestment;
		
		players.add(p);
		
	}
	
	// Returns true if player already exists
	public static boolean playerExists(String playerName)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	// Adds a new player
	public static void addNewPlayer(String playerName)
	{
		
		Player p = new Player();
		
		p.name = playerName;
		p.points = 100;
		p.state = 0;
		p.investment = 0;
		
		players.add(p);
		
	}
	
	// Removes a player (currently unused)
	public static void removePlayer(String playerName)
	{
		
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				players.remove(i);
				break;
			}
		}
		
	}
	
	// Add points to a specific player by name
	public static void addPoints(String playerName, int addPoints)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				players.elementAt(i).points += addPoints;
				break;
			}
		}
	}
	
	
	// Remove points from a specific player by name
	public static void removePoints(String playerName, int removePoints)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				players.elementAt(i).points -= removePoints;
				break;
			}
		}
	}
	
	// Get the points of a specific player
	public static int getPoints(String playerName)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				return players.elementAt(i).points;
				
			}
		}
		
		return -1;
	}
	
	// Get the state of a specific player
	public static int getState(String playerName)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				return players.elementAt(i).state;
				
			}
		}
		
		return -1;
	}
	
	// Set the state of a specific player
	public static void setState(String playerName, int newState)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				players.elementAt(i).state = newState;
				
				break;
			}
		}

	}
	
	// Gets the amount of money this player has invested (currently unused, but usable for if the player wants to request this info)
	public static int getInvestment(String playerName)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				return players.elementAt(i).investment;
				
			}
		}
		
		return -1;
	}
	
	// Sets the amount of money this player has invested (currently unused, see above)
	public static void setInvestment(String playerName, int newInvestment)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).name.equals(playerName))
			{
				players.elementAt(i).investment = newInvestment;
				
				break;
			}
		}

	}
	
	// Force all players who have invested to sell at the specified cost
	public static void sellAll(int cost)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).state == 1)
			{
				players.elementAt(i).points += cost;
				players.elementAt(i).state = 0;
				players.elementAt(i).investment = 0;
			}
		}
	}
	
	// Add bonus points to all players who have currently invested
	public static void addPointsAll(int addPoints)
	{
		for(int i = 0; i < players.size(); i++)
		{
			if(players.elementAt(i).state == 1)
			{
				players.elementAt(i).points += addPoints;
			}
		}
	}
	
	
	// Iteration handling methods, used from the file handler to iterate through the vector externally,
	// Each method will return the specific data a the index location specified
	public static int getSize() {
		return players.size();
	}
	
	public static String getNameAt(int index)
	{
		return players.elementAt(index).name;
	}
	
	public static int getPointsAt(int index)
	{
		return players.elementAt(index).points;
	}
	
	public static int getStateAt(int index)
	{
		return players.elementAt(index).state;
	}
	
	public static int getInvestmentAt(int index)
	{
		return players.elementAt(index).investment;
	}

}
