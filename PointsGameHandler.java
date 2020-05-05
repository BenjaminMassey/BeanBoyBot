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

}
