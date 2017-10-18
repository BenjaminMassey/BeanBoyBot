package bbb;

import java.util.ArrayList;

import bbb.PlayersHandler.Player;

public class PointsGameHandler {
	
	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	public static boolean buyRun(String newName) {
		
		ArrayList<Player> players = PlayersHandler.getPlayers();
		
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i).name.equals(newName))
			{
				if(players.get(i).points - SplitGame.getCost() > 0 && players.get(i).state == 0)
				{
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
		
		for(int i = 0; i < players.size(); i++)
		{
			if(players.get(i).name.equals(newName))
			{
				if(players.get(i).state == 1)
				{
					// Right now just going to make it sell immediately, when we do delayed selling it will be set state = 2
					players.get(i).points += SplitGame.getCost();
					players.get(i).state = 0;
					players.get(i).investment = 0;
					
					PlayersHandler.saveAll();
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void sellAll() {
		
		ArrayList<Player> players = PlayersHandler.getPlayers();
		
		for(int i = 0; i < players.size(); i++)
		{
			players.get(i).points += SplitGame.getCost();
			players.get(i).state = 0;
			players.get(i).investment = 0;
		}
		
		PlayersHandler.saveAll();
	}
	
	public static void addBonusPoints(int bonus) {
		
		PlayersHandler.addPointsAll(bonus);

		PlayersHandler.saveAll();
	}
	
}
