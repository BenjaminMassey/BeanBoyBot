package bbb;

import java.io.File;

public class ConfigValues {
	// Class to hold and manage all configuration settings
	
	public static boolean stocksOn;
	public static double scoreMultiplier;
	public static double dividendRate;
	public static boolean cheekyEmotes;

	public static double chokeRate;

	public static int goldPayout;
	public static double resetPayout;
	public static double pbPayout;
	
	public static void writeValues() {
		FileHandler.writeToFile("Config", "StocksOn:" + Boolean.toString(stocksOn) + FileHandler.nl
								+ "ScoreMultiplier:" + Double.toString(scoreMultiplier) + FileHandler.nl
								+ "DividendRate:" + Double.toString(dividendRate) + FileHandler.nl
								+ "CheekyEmotes:" + Boolean.toString(cheekyEmotes) + FileHandler.nl
								+ "ChokeRate:" + Double.toString(chokeRate) + FileHandler.nl
								+ "GoldPayout:" + Integer.toString(goldPayout) + FileHandler.nl   //Gold PB and Reset Payout are all in the config file, but not in the GUI.
								+ "PBPayout:" + Double.toString(pbPayout) +FileHandler.nl 		  // This is a decision by me to avoid clutter, since I don't think people
								+ "ResetPayout:" + Double.toString(resetPayout) +FileHandler.nl); // will edit them that much, but I still think they should be editable.
	}
	
	public static void getValues() {
		if(FileHandler.getFileLength("Config") > 7) {
			stocksOn = Boolean.parseBoolean(FileHandler.readFromFile("Config", 0).split(":")[1]);
			scoreMultiplier = Double.parseDouble(FileHandler.readFromFile("Config", 1).split(":")[1]);
			dividendRate = Double.parseDouble(FileHandler.readFromFile("Config", 2).split(":")[1]);
			cheekyEmotes = Boolean.parseBoolean(FileHandler.readFromFile("Config", 3).split(":")[1]);
			chokeRate = Double.parseDouble(FileHandler.readFromFile("Config", 4).split(":")[1]);
			goldPayout = Integer.parseInt(FileHandler.readFromFile("Config", 5).split(":")[1]);
			pbPayout = Double.parseDouble(FileHandler.readFromFile("Config", 6).split(":")[1]);
			resetPayout = Double.parseDouble(FileHandler.readFromFile("Config", 7).split(":")[1]);
		}
		else {
			stocksOn = true;
			scoreMultiplier = 10.0;
			dividendRate = 0.5; //for some reason it was 5.0 points per second before
			chokeRate = 0.85;
			goldPayout = 100;
			pbPayout = 2.0;
			resetPayout = 0.75;
			cheekyEmotes = true;
			writeValues();
		}
	}
}
