package bbb;

public class ConfigValues {
	// Class to hold and manage all configuration settings
	
	public static boolean stocksOn;
	public static double scoreMultiplier;
	public static double dividendRate;
	public static boolean cheekyEmotes;
	
	public static void writeValues() {
		FileHandler.writeToFile("Config", "StocksOn:" + Boolean.toString(stocksOn) + FileHandler.nl
								+ "ScoreMultiplier:" + Double.toString(scoreMultiplier) + FileHandler.nl
								+ "DividendRate:" + Double.toString(dividendRate) + FileHandler.nl
								+ "CheekyEmotes:" + Boolean.toString(cheekyEmotes));
	}
	
	public static void getValues() {
		if(FileHandler.getFileLength("Config") > 3) {
			stocksOn = Boolean.parseBoolean(FileHandler.readFromFile("Config", 0).split(":")[1]);
			scoreMultiplier = Double.parseDouble(FileHandler.readFromFile("Config", 1).split(":")[1]);
			dividendRate = Double.parseDouble(FileHandler.readFromFile("Config", 2).split(":")[1]);
			cheekyEmotes = Boolean.parseBoolean(FileHandler.readFromFile("Config", 3).split(":")[1]);
		}
		else {
			stocksOn = true;
			scoreMultiplier = 10.0;
			dividendRate = 5.0;
			cheekyEmotes = true;
			writeValues();
		}
	}
}
