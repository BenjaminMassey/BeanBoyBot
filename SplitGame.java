package bbb;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class SplitGame extends TimerTask {

	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot

	private static int split; // For splits, -1 means not started and then counts from 0
	private static boolean reset; // Whether we have already paid out for the reset

	private static double bpt; // Best Possible Time in seconds
	private static double ct; // Current Time in seconds
	private static double pd; // Percent Done with the run in seconds
	private static double d; // Delta in seconds
	private static double bd; // delta for best segments in seconds
	private static double pb; // Personal Best in seconds
	private static double v; // The calculated value from 0 to 1 of the run

	private static int cost; // How much a run will cost at the moment
	private static Timer timer;
	private static boolean pbMessage; // Toggle for special PB message

	private static int dividend; // Dividend value


	private static int chokes; // number of chokes left in the run
	
	private static ArrayList<String> ignoreSplits; // Ignore splits of any name in this ArrayList for dividend rewards (useful specifically with Loading Splits in TTT)
	private static ArrayList<String> chokeSplits;

	public void run() {
		if(ConfigValues.stocksOn) {
			updateSplit();
			updateBPT();
			updateCT();
			updatePD();
			updateD();
			updateBD();
			generateValue();
			if (checkReset()) {
				cost = (int) Math.round(cost * 0.75);
				PointsGameHandler.sellAll();
				chokes = bbb.FileHandler.getFileLength("Chokes");
				bd = 0; //dont want first split to always be a "gold"
				if(PlayersHandler.getNumActivePlayers() > 0) // prevent spam
					TwitchChat.outsideMessage("Sold out everyone at " + cost + " for a reset (spoilers)");
				if (ConfigValues.cheekyEmotes)
					StreamEmote.botEmote("RIPRUN", "FeelsBadMan");

			}
			if (checkPB()) {
				cost = (int) Math.round(cost * 2);
				PointsGameHandler.sellAll();
				chokes = bbb.FileHandler.getFileLength("Chokes");
				bd = 0; //dont want first split to always be a "gold"
				TwitchChat.outsideMessage("Sold out everyone at " + cost + " for a PB (spoilers)");
				pbMessage = true;
				if (ConfigValues.cheekyEmotes)
					StreamEmote.botEmote("FINALLYPB", "FeelsGoodMan");
			}
			setCost();
			updatePB();
			if (pbMessage) {
				FileHandler.writeToFile("Output", "Congrats on\r\nthe PB!");
				if (ct != pb) { pbMessage = false; }
			}
			else {
				output();
			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				System.err.println("Oops: " + e);;
			}
		}
		else {
			FileHandler.writeToFile("Output", "SplitGame is\r\nnot on!");
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.err.println("Oops: " + e);;
			}
		}
	}

	public static void start() {
		reset = true;
		pb = -1; // Need to check before first update, but in order to check need a value
		pbMessage = false;
		//ConfigValues.dividendRate = 0.5; // 0 means disabled (commented this line out, i wanted to change the dividend rate (too many points) but it wouldn't let me because of this line.
		TimerTask splitStocks = new SplitGame();
		TimeForPoints.start();
		timer = new Timer(true);
		timer.scheduleAtFixedRate(splitStocks, 0, 1000);
		ignoreSplits = bbb.FileHandler.readEntireFile("IgnoreSplits");
		ignoreSplits.add("-Loading"); // This is specifically for me right now, don't know if you can use it. If you can't simply remove this line
		chokeSplits = bbb.FileHandler.readEntireFile("Chokes");
		chokes = bbb.FileHandler.getFileLength("Chokes");
	}

	public static void stop() {
		timer.cancel();
		TimeForPoints.stop();
	}
	
	private static void output() {
		FileHandler.writeToFile("Output", "!SplitGame\nCost: " + cost + "\nInvestors: " + PlayersHandler.getInvestorCount());
	}
	
	@SuppressWarnings("unused")
	private static void print() {
		System.out.println("bpt : " + bpt);
		System.out.println("ct : " + ct);
		System.out.println("pd : " + pd);
		System.out.println("d : " + d);
		System.out.println("pb : " + pb);
		System.out.println("v : " + v);
		System.out.println("cost : " + cost);
		System.out.println("split : " + split);
		System.out.println("reset : " + reset);
	}

	private static void updateSplit() {
		int newSplit = Integer.parseInt(LiveSplitHandler.getSplitIndex());

		if (split != newSplit) { // Split just changed to a new split, check delta to see if it pays dividends
			if(newSplit > 0)
			{
				String buffer = LiveSplitHandler.getPreviousSplitName();
				
				if(!(ignoreSplits.contains(buffer) || buffer.startsWith("-"))) // Check if this split is in the ignore list, if so don't reward Dividends.
					rewardDividends(newSplit);								 // Edited it to automatically ignore subsplits, so it doesn't reward dividends every 15 seconds for my runs -DNVIC.

				rewardGolds(newSplit);
				if(chokeSplits.contains(buffer)) // Check if the previous split was a "choke split", if so reduce the "chokes" by 1
					chokes--;
			}
			PlayersHandler.setBeginSplit(newSplit); // Update the begin split if you are invested at this point in time
			System.out.println("New Split");
		}

		split = newSplit;
	}

	private static void rewardDividends(int thisSplit) {
		updateD();
		if ((int) (Math.abs(d) * ConfigValues.dividendRate) > 0 && d < 0) { // Added check that delta is negative
			dividend = (int) Math.ceil(Math.abs(d) * ConfigValues.dividendRate);
			if(PlayersHandler.getNumActivePlayers() > 0) // prevent spam
				TwitchChat.outsideMessage("PB Pace! Dividends pay " + dividend + " points to everyone who was invested at the start of this split.");
			PointsGameHandler.addDividendPoints(dividend, thisSplit); // Wrote new function that checks players.beginSplit
		}
	}

	private static void rewardGolds(int thisSplit) {;
		double prevbd = bd;
		updateBD();
		if (bd < prevbd) {
			TwitchChat.outsideMessage("New Gold! The generosity of the run has given everyone invested " + ConfigValues.goldPayout + " points!"); //no spam prevention because golds are rare
			PointsGameHandler.addGoldPayout();
		}
	}
	
	private static boolean checkReset() {
		if (split == -1 && !reset) {
			reset = true;
			return true;
		} else {
			if (split > -1)
				reset = false;
			return false;
		}
	}

	private static boolean checkPB() {
		double finalTime = stringTimeToSeconds(LiveSplitHandler.getFinalTime());
		if (finalTime < pb && ct == finalTime) {
			pb = finalTime;
			return true;
		} else
			return false;
	}

	private static void updateBPT() {
		bpt = stringTimeToSeconds(LiveSplitHandler.getBestPossibleTime());
	}

	private static void updateCT() {
		ct = stringTimeToSeconds(LiveSplitHandler.getCurrentTime());
	}

	private static void updatePD() {
		pd = ct / bpt;
	}

	private static void updateD() {
		// Have to handle some weird stuff here
		if (LiveSplitHandler.getCurrentTimerPhase().equals("Running")) {
			String buffer = LiveSplitHandler.getDelta();
			if (buffer.contains("−"))
				buffer = buffer.replaceAll("−", "-");
			d = stringTimeToSeconds(buffer);
			
			//System.out.println("Delta - String: " + buffer + " num: " + d);
		}
	}
	private static void updateBD() {
		// Have to handle some weird stuff here
		if (LiveSplitHandler.getCurrentTimerPhase().equals("Running")) {
			String buffer = LiveSplitHandler.getBestDelta();
			if (buffer.contains("−"))
				buffer = buffer.replaceAll("−", "-");
			bd = stringTimeToSeconds(buffer);

			//System.out.println("Delta - String: " + buffer + " num: " + d);
		}
	}

	private static void updatePB() {
		pb = stringTimeToSeconds(LiveSplitHandler.getFinalTime());
	}

	private static void generateValue() {
		double value = 16; // Start out at maximum value, then will subtract
		double timeSaveFactor = 0;
		double timeThroughFactor = 0;

		timeSaveFactor = 1 - ((pb - bpt) / pb); // replaced xx / bpt with / pb - This appears to work much better ... so far.

		timeThroughFactor = 1 - pd;
		
		double factor = 1.75 * timeSaveFactor + 0.25 * timeThroughFactor; // max 2
		
		factor = Math.pow(factor, 4); // max 16
		value -= factor;
		if (value > 16)
			value = 16;
		if (value < 0)
			value = 0;
		v = value;
	}

	private static void setCost() {
		if (!LiveSplitHandler.getCurrentTimerPhase().equals("Ended")) { // Fix for calculating a high cost even if you finish a run poorly
			cost = (int) Math.round(v * ConfigValues.scoreMultiplier * Math.pow(ConfigValues.chokeRate, chokes));
		}
	}

	public static int getCost() {
		return cost;
	}
	
	public static double getTime() {
		return ct;
	}
	
	public static void gamble(String player, String message) {
		// Have a player randomly be given or randomly taken away a given amount
		
		// Parse the amount out of the message
		String amountStr = "";
		for(int i = 0; i < message.length(); i++) {
			if (i > 7)
				amountStr += message.toCharArray()[i];
		}
		int amount = 0;
		try {
			amount = Math.abs(Integer.parseInt(amountStr));
		}catch(Exception e) {
			System.err.println("Oops: " + e);
		}
		
		if(PlayersHandler.getPoints(player) < amount) // Doesn't have enough points
			TwitchChat.outsidePM(player, "Gamble " + amount + " with " + PlayersHandler.getPoints(player) + " Kappa");
		
		else { // Has enough points
			Random rng = new Random();
			double result = rng.nextDouble();
			double chance = 0.5;
			
			// Gamble amount for it to display in chat
			double cap = PlayersHandler.getPoints(player) * 0.5;
			if (cap < 100)
				cap = 100;
			
			if(result < chance) {
				TwitchChat.outsidePM(player, player + ", you won " + amount + " points! PogChamp");
				if(amount > cap)
					TwitchChat.outsideMessage(player + " won " + amount + " points! PogChamp");
				PlayersHandler.addPoints(player, amount);
			}
			else {
				TwitchChat.outsidePM(player, player + ", you lost " + amount + " points... FeelsBadMan");
				if (amount > cap)
					TwitchChat.outsideMessage(player + " lost " + amount + " points... FeelsBadMan");
				PlayersHandler.removePoints(player, amount);
			}
		}
	}

	private static double stringTimeToSeconds(String givenTime) {
		
		boolean negativeNumber = false;
		
		if(givenTime.startsWith("-")) // Check if this number is negative
			negativeNumber = true;
		
		
		try {
			
			// Tenka - Added support for negative numbers, this was messing up some calculations on delta
			// E.g. on a pace of -2:36 .. it would separate into -2 and 36 ... do -2 * 60 = -120 + 36 = -94 .. instead of what it should be, -156
			
			String[] timeStrArray = givenTime.split(":"); // { minutes, seconds }
			double time; // in seconds
			if (timeStrArray.length == 1) // X.XX
				time = Math.abs(Double.parseDouble(timeStrArray[0]));
			else if (timeStrArray.length == 2) { // XX:XX.XX
				time = (Math.abs(Double.parseDouble(timeStrArray[0])) * 60) + Math.abs(Double.parseDouble(timeStrArray[1]));
			} else if(timeStrArray.length == 3){ // X:XX:XX.XX Why not add this I figure .. might be important down the line
				time = (Math.abs(Double.parseDouble(timeStrArray[0])) * 3600) + (Math.abs(Double.parseDouble(timeStrArray[1])) * 60) + Math.abs(Double.parseDouble(timeStrArray[2]));
			} else {
				time = 0;
				System.err.println("DID NOT PARSE '" + givenTime + "' CORRECTLY");
			}
			
			if(negativeNumber)
				time = -time; // Add the negative back in
			
			return time;
		} catch (Exception e) {
			return 0;
		}
	}
}
