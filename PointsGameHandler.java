package bbb;

public class PointsGameHandler {
	
	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	private static String nl = System.getProperty("line.separator");
	
	public static boolean addPlayer(String name) {
		boolean newPlayer = true;
		int playerNum = FileHandler.getFileLength("SplitGame");
		for (int i = 0; i < playerNum; i++) {
			System.out.println(i + ": " + FileHandler.readFromFile("SplitGame", i).split(":")[0] + ", " + name);
			if (FileHandler.readFromFile("SplitGame", i).split(":")[0].equals(name))
				newPlayer = false;
		}
		if(newPlayer) {
			FileHandler.appendToFile("SplitGame", name + ":100:false" + nl);
			return true;
		}
		else
			return false;
	}
	
	public static int getPoints(String name) {
		try {
			int lineNum = fetchPlayerLineNum(name);
			return Integer.parseInt(FileHandler.readFromFile("SplitGame", lineNum).split(":")[1]);
		}catch(Exception e) {
			return 0;
		}
	}
	
	public static boolean buyRun(String name) {
		int originalLine = fetchPlayerLineNum(name);
		String[] playerInfo = FileHandler.readFromFile("SplitGame", originalLine).split(":");
		if(playerInfo.length != 3)
			return false;
		if(playerInfo[2].equals("true"))
			return false;
		playerInfo[1] = String.valueOf((int) (Integer.parseInt(playerInfo[1]) - SplitGame.getCost()));
		playerInfo[2] = "true";
		FileHandler.deleteLineFromFile("SplitGame", originalLine);
		FileHandler.appendToFile("SplitGame", nl + playerInfo[0] + ":" + playerInfo[1] + ":" + playerInfo[2] + nl);
		return true;
	}
	
	public static boolean sellRun(String name) {
		int originalLine = fetchPlayerLineNum(name);
		String[] playerInfo = FileHandler.readFromFile("SplitGame", originalLine).split(":");
		if(playerInfo.length != 3)
			return false;
		if(playerInfo[2].equals("false"))
			return false;
		playerInfo[1] = String.valueOf((int) (Double.parseDouble(playerInfo[1]) + SplitGame.getCost()));
		playerInfo[2] = "false";
		FileHandler.deleteLineFromFile("SplitGame", originalLine);
		FileHandler.appendToFile("SplitGame", nl + playerInfo[0] + ":" + playerInfo[1] + ":" + playerInfo[2] + nl);
		return true;
	}
	
	public static void sellAll() {
		int numPlayers = FileHandler.getFileLength("SplitGame");
		for(int i = 0; i < numPlayers; i++) {
			String[] playerInfo = FileHandler.readFromFile("SplitGame", 0).split(":");
			sellRun(playerInfo[0]);
		}
	}
	
	public static int fetchPlayerLineNum(String name) {
		int playerNum = FileHandler.getFileLength("SplitGame");
		int lineNum = 0;
		for (int i = 0; i < playerNum; i++) {
			if (FileHandler.readFromFile("SplitGame", i).split(":")[0].equals(name))
				lineNum = i;
		}
		return lineNum;
	}
	
}
