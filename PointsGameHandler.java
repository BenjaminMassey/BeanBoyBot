package bbb;

import java.util.ArrayList;

import bbb.PlayersHandler.Player;

public class PointsGameHandler {

	// A part of BeanBoyBot
	// Copyright 2020 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot

	public static boolean buyRun(String newName) {

		ArrayList<Player> players = PlayersHandler.getPlayers();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(newName)) {
				if (players.get(i).points - SplitGame.getCost() > 0 && players.get(i).state == 0) {
					players.get(i).points -= SplitGame.getCost();
					players.get(i).state = 1;
					players.get(i).investment = SplitGame.getCost();
					PlayersHandler.saveAll();
					return true;
				} else if (players.get(i).points - SplitGame.getCost() > 0 && players.get(i).state == 3) { // buying after a short
					players.get(i).points -= SplitGame.getCost();
					players.get(i).state = 0;
					players.get(i).investment = 0;
					PlayersHandler.saveAll();
					return true;
				}
			}
		}

		return false;
	}
	public static boolean shortRun(String newName) {//separate function from sell because I don't want people shorting accidentally.

		ArrayList<Player> players = PlayersHandler.getPlayers();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(newName)) {
				if (players.get(i).state == 0) {
					players.get(i).points += SplitGame.getCost();
					players.get(i).state = 3; // used 3 since 2 is going to be used for delayed selling
					players.get(i).investment = -SplitGame.getCost();
					PlayersHandler.saveAll();
					return true;
				}
			}
		}

		return false;
	}
	public static boolean sellRun(String newName) {

		ArrayList<Player> players = PlayersHandler.getPlayers();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).name.equals(newName)) {
				if (players.get(i).state == 1) {
					// Right now just going to make it sell immediately, when we do delayed selling
					// it will be set state = 2
					players.get(i).points += SplitGame.getCost();
					players.get(i).state = 0;
					players.get(i).investment = 0;
					players.get(i).beginSplit = -1; // Reset the begin split

					PlayersHandler.saveAll();
					return true;
				}
			}
		}

		return false;
	}

	public static void sellAll() {

		ArrayList<Player> players = PlayersHandler.getPlayers();

		for (int i = 0; i < players.size(); i++) {
			
			
			
			if(players.get(i).state == 1)
			{
				int payout = SplitGame.getCost();
				TwitchChat.outsidePM(players.get(i).name, "RIP run... You get paid out " 
						+ payout + " points for a result of " + (payout - players.get(i).investment));
				players.get(i).points += payout;
				players.get(i).state = 0;
				players.get(i).investment = 0;
			} else if(players.get(i).state == 3) { //shorting
				int payout = SplitGame.getCost();
				TwitchChat.outsidePM(players.get(i).name, "RIP run... You have to buy the stock back at the price of "
						+ payout + " points for a result of " + (-payout - players.get(i).investment));
				players.get(i).points -= payout;
				players.get(i).state = 0;
				players.get(i).investment = 0;
			}

		}

		PlayersHandler.saveAll();
	}

	public static void addBonusPoints(int bonus) {

		PlayersHandler.addPointsAll(bonus);

		PlayersHandler.saveAll();
	}
	
	public static void addDividendPoints(int bonus, int split) {
		
		ArrayList<Player> players = PlayersHandler.getPlayers();
		
		for(int i = 0; i < players.size(); i++) {
			
			if(players.get(i).beginSplit < split && players.get(i).beginSplit != -1) // if beginSplit == -1 then the player has bought in mid split and will not be rewarded
			{
				System.out.println(players.get(i).name + " was invested at split " + players.get(i).beginSplit + " it is currently split" + split);
				players.get(i).points += bonus;
				TwitchChat.outsidePM(players.get(i).name, "Thanks for holding onto the run! Enjoy a reward of " + bonus + " points!");
			}
		}
		
		PlayersHandler.saveAll();
	}

	public static void addGoldPayout() {
		ArrayList<Player> players = PlayersHandler.getPlayers();

		for(int i = 0; i < players.size(); i++) {

			if(players.get(i).state == 1) // check if bought at all. mid-split shouldnt matter for this, as its hard to tell (if you have good splits) if a split will be a gold until right before you split.
			{
				players.get(i).points += ConfigValues.goldPayout;
				TwitchChat.outsidePM(players.get(i).name, "GOOOOOOOOOOOOOLD!!!! Enjoy a payout of " + ConfigValues.goldPayout + " points!");
			}
		}
	}

}
