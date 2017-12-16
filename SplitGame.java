package bbb;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

public class SplitGame extends TimerTask {

	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot

	private static final double scoreMultiplier = 10;

	private static int split; // For splits, -1 means not started and then counts from 0
	private static boolean reset; // Whether we have already paid out for the reset

	private static double bpt; // Best Possible Time in seconds
	private static double ct; // Current Time in seconds
	private static double pd; // Percent Done with the run in seconds
	private static double d; // Delta in seconds
	private static double pb; // Personal Best in seconds
	private static double v; // The calculated value from 0 to 1 of the run

	private static int cost; // How much a run will cost at the moment
	private static Timer timer;

	private static int dividend; // Dividend value
	private static double diviMultiplier; // Dividend multiplier (as a percentage of delta)
	
	private static ArrayList<String> ignoreSplits; // Ignore splits of any name in this ArrayList for dividend rewards (useful specifically with Loading Splits in TTT)

	public void run() {
		updateSplit();
		updateBPT();
		updateCT();
		updatePD();
		updateD();
		generateValue();
		if (checkReset()) {
			cost = (int) Math.round(cost * 0.75);
			PointsGameHandler.sellAll();
			TwitchChat.outsideMessage("Sold out everyone at " + cost + " for a reset (spoilers)");
		}
		if (checkPB()) {
			cost = (int) Math.round(cost * 2);
			PointsGameHandler.sellAll();
			TwitchChat.outsideMessage("Sold out everyone at " + cost + " for a PB (spoilers)");
		}
		setCost();
		updatePB();
		output();
		// GUIHandler.cost.setText("Cost: " + Long.toString(cost));
		// print();
	}

	public static void start() {
		reset = true;
		pb = -1; // Need to check before first update, but in order to check need a value
		diviMultiplier = 0.5; // 0 means disabled
		TimerTask splitStocks = new SplitGame();
		TimeForPoints.start();
		timer = new Timer(true);
		timer.scheduleAtFixedRate(splitStocks, 0, 1000);
		ignoreSplits = new ArrayList<String>(); // Make sure to leave this though
		ignoreSplits.add("-Loading"); // This is specifically for me right now, don't know if you can use it. If you can't simply remove this line
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
				
				if(!ignoreSplits.contains(buffer)) // Check if this split is in the ignore list, if so dont reward Dividends
					rewardDividends(newSplit);
			}
			PlayersHandler.setBeginSplit(newSplit); // Update the begin split if you are invested at this point in time
			System.out.println("New Split");
		}

		split = newSplit;
	}

	private static void rewardDividends(int thisSplit) {
		updateD();
		if ((int) (Math.abs(d) * diviMultiplier) > 0 && d < 0) { // Added check that delta is negative
			dividend = (int) Math.ceil(Math.abs(d) * diviMultiplier);
			TwitchChat.outsideMessage("PB Pace! Dividends pay " + dividend + " points to everyone who was invested at the start of this split.");
			PointsGameHandler.addDividendPoints(dividend, thisSplit); // Wrote new function that checks players.beginSplit
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
		if (!LiveSplitHandler.getCurrentTimerPhase().equals("Ended")) { // Fix for calculating a high cost even if u finish a run poorly
			cost = (int) Math.round(v * scoreMultiplier);
		}
	}

	public static int getCost() {
		return cost;
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
			TwitchChat.outsideMessage("Gamble " + amount + " with " + PlayersHandler.getPoints(player) + " Kappa");
		
		else { // Has enough points
			Random rng = new Random();
			double result = rng.nextDouble();
			double chance = 0.5;
			
			if(result < chance) {
				TwitchChat.outsideMessage(player + ", you won " + amount + " points! PogChamp");
				PlayersHandler.addPoints(player, amount);
			}
			else {
				
				TwitchChat.outsideMessage(player + ", you lost " + amount + " points... FeelsBadMan");
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
				time = (Math.abs(Double.parseDouble(timeStrArray[0])) * 360) + (Math.abs(Double.parseDouble(timeStrArray[1])) * 60) + Math.abs(Double.parseDouble(timeStrArray[2]));
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
