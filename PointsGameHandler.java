package bbb;

import java.util.Vector;

public class PointsGameHandler {
	
	public static void Initialize()
	{
		PlayersHandler.Initialize();
		
		System.out.println("Constructor, attempting to load file");
		
		if(!FileHandler.loadAll("SplitGame"))
		{
			System.out.println("no file found, creating one");
			
			FileHandler.saveAll("SplitGame");
		}
		
		
	}
	
	public static boolean addPlayer(String newName) {
		
		if(PlayersHandler.playerExists(newName))
		{
			return false;
		}
		
		PlayersHandler.addNewPlayer(newName);
		
		FileHandler.saveAll("SplitGame");
		
		return true;
		
	}
	
	public static int getPoints(String newName) {

		return PlayersHandler.getPoints(newName);
	}
	
	// Adds bonus points for every person invested at the time of callind
	public static void addBonusPoints(int bonus) {
		
		PlayersHandler.addPointsAll(bonus);

		FileHandler.saveAll("SplitGame");
	}
	
	public static boolean buyRun(String newName) {
		
		if(PlayersHandler.getState(newName) == 0)
		{
			PlayersHandler.removePoints(newName, SplitGame.getCost());
			PlayersHandler.setState(newName, 1);
			PlayersHandler.setInvestment(newName,SplitGame.getCost());
			FileHandler.saveAll("SplitGame");
			
			return true;
		}
		
		return false;
		
	}
	
	public static boolean sellRun(String newName) {
		
		if(PlayersHandler.getState(newName) == 1)
		{
			PlayersHandler.addPoints(newName, SplitGame.getCost());
			PlayersHandler.setState(newName, 0);
			PlayersHandler.setInvestment(newName,0);
			FileHandler.saveAll("SplitGame");
			
			return true;
		}
		
		return false;
		
	}
	
	public static void sellAll() {
		
		PlayersHandler.sellAll(SplitGame.getCost());
		FileHandler.saveAll("SplitGame");
		
	}
	

}
