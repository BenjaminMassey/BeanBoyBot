package bbb;

import java.util.Timer;
import java.util.TimerTask;

public class SplitGame extends TimerTask{
	
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
	
	public void run() {
		updateSplit();
		updateBPT();
		updateCT();
		updatePD();
		//updateD(); BROKEN
		generateValue();
		if(checkReset()) {
			cost = (int) Math.round(cost * 0.75);
			PointsGameHandler.sellAll();
			TwitchChat.outsideMessage("Sold out everyone at " + cost + " for a reset (spoilers)");
		}
		if(checkPB()) {
			cost = (int) Math.round(cost * 1.5);
			PointsGameHandler.sellAll();
			TwitchChat.outsideMessage("Sold out everyone at " + cost + " for a PB (spoilers)");
		}
		setCost();
		updatePB();
		GUIHandler.cost.setText("Cost: " + Long.toString(cost));
		//print();
	}
	
	public static void start() {
		reset = true;
		pb = -1; // Need to check before first update, but in order to check need a value
		TimerTask splitStocks = new SplitGame();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(splitStocks, 0, 1000);
	}
	
	public static void stop() {
		timer.cancel();
	}
	
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
		split = Integer.parseInt(LiveSplitHandler.getSplitIndex());
	}
	
	private static boolean checkReset() {
		if(split == -1 && !reset) {
			reset = true;
			return true;
		}
		else {
			if (split > -1)
				reset = false;
			return false;
		}
	}
	
	private static boolean checkPB() {
		double finalTime = stringTimeToSeconds(LiveSplitHandler.getFinalTime());
		if(finalTime < pb)
			return true;
		else
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
	/* THIS METHOD IS GETTING A STRANGE RESULT THAT IS CAUSING ISSUES
	private static void updateD() {
		d = stringTimeToSeconds(LiveSplitHandler.getDelta());
	}
	*/
	private static void updatePB() {
		pb = stringTimeToSeconds(LiveSplitHandler.getFinalTime());
	}
	
	private static void generateValue() {
		double value = 1; // Start out at maximum value, then will subtract
		//double factor = (0.5 * ( (pb - ct)/pb ) + 0.5 * ( (pb - bpt) / pb ));
		//double timeSaveFactor = d / (pb - bpt);
		double timeSaveFactor = 1 - ((pb - bpt) / bpt);
		double timeThroughFactor = 1 - pd;
		
		double factor = 1.75*timeSaveFactor + 0.25*timeThroughFactor; // max 2
		factor = Math.pow(factor, 4); // max 16
		value = 16 - factor;
		if(value > 16)
			value = 16;
		if(value < 0)
			value = 0;
		v = value;
	}
	
	private static void setCost() {
		cost =  (int) Math.round(v * scoreMultiplier);
	}
	
	public static int getCost() {
		return cost;
	}
	
	private static double stringTimeToSeconds(String givenTime) {
		try {
			String[] timeStrArray = givenTime.split(":"); // { minutes, seconds }
			double time; // in seconds
			if (timeStrArray.length == 1) // X.XX
				time = Double.parseDouble(timeStrArray[0]);
			else if (timeStrArray.length == 2) { // XX:XX.XX
				time = (Double.parseDouble(timeStrArray[0]) * 60) + 
						Double.parseDouble(timeStrArray[1]);
			}
			else { // Probably nothing, maybe into hours
				time = 0;
				System.err.println("DID NOT PARSE '" + givenTime + "' CORRECTLY");
			}
			return time;
		}catch(Exception e) {
			return 0;
		}
	}
}
