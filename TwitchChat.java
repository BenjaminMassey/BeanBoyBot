package bbb;

import java.io.IOException;

import org.jibble.pircbot.*;

public class TwitchChat extends PircBot {
	
	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	// This class handles actually reading from and talking to Twitch Chat
	
	// Code for Twitch Chat functionality based on tutorial from this channel: https://www.youtube.com/channel/UCoQuKOXYxUBeNWbendo3sF
	
	public static boolean connected = false;
	private static String channel; // What channel the bot should talk to/read from
	private static TwitchChat bot;
	
	public TwitchChat() {
		// Quick mini setup
		
		this.setName(AccountsManager.getBotName());
		this.isConnected();
	}
	
	public static void initialize() throws NickAlreadyInUseException, IOException, IrcException {
		// Set up the Twitch Bot to be in chat
		channel = AccountsManager.getChatChannel();
		connected = true;
		bot = new TwitchChat();
		bot.setVerbose(true);
		bot.connect("irc.twitch.tv", 6667, AccountsManager.getBotOauth());
		bot.joinChannel(channel);
	}
	
	public static void deactivate() throws IOException, IrcException { 
		bot.disconnect(); 
		connected = false;
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		// React to a given message
		
		// Here are the commands that should be taken action for
		if(message.equalsIgnoreCase("!test"))
			messageChat("Hey here's my test!");
		
		if(message.startsWith("!addquote ")) {
			QuotesHandler.addQuote(message);
			messageChat("Added your quote!");
		}
		
		if(message.startsWith("!quote"))
			messageChat(QuotesHandler.getQuote(message));
		
		if(message.startsWith("!delquote ")) {
			boolean deleted = QuotesHandler.delQuote(message);
			if(deleted)
				messageChat("Deleted!");
			else
				messageChat("Failed to delete... D:");
		}
		
		if(message.equalsIgnoreCase("!SplitGame")) {
			messageChat("Now you can play a game in chat with the speedrun! " +
						"The run will always have a 'cost' associated with it, " +
						"and you can buy or sell the run at that price at any " +
						"time. Think of it like a stock. Use !join to add yourself " +
						"into the game, !buy to buy a run, !sell to sell a run and " +
						"!points to see your points. A PB will give 50% extra points, " +
						"but a reset will only give you 75% of the current cost. I'm still an early version, so " +
						"sorry if something doesn't work or if the game isn't very balanced. " +
						"Feel free to give feedback in chat, and Bean will do his best to " +
						"improve it. Have fun!");
		}
		
		if(message.startsWith("!points"))
			messageChat(sender + " has " + PointsGameHandler.getPoints(sender) + " points.");
		
		if(message.startsWith("!join")) {
			boolean joined = PointsGameHandler.addPlayer(sender);
			if(joined)
				messageChat("Thanks for joining, " + sender + "! You start with 100 points.");
			else
				messageChat("Sorry, " + sender + ", but failed to add you... D:");
		}
		
		if(message.startsWith("!buy")) {
			boolean bought = PointsGameHandler.buyRun(sender);
			if(bought)
				messageChat("Thanks for buying, " + sender + "! It cost you " + SplitGame.getCost() + 
							" points. You now have " + PointsGameHandler.getPoints(sender) + "points.");
			else
				messageChat("Sorry, " + sender + ", but failed to buy... D:");
		}
		
		if(message.startsWith("!sell")) {
			boolean sold = PointsGameHandler.sellRun(sender);
			if(sold)
				messageChat("Thanks for selling, " + sender + "! It gave you " + SplitGame.getCost() + 
							" points. You now have " + PointsGameHandler.getPoints(sender) + "points.");
			else
				messageChat("Sorry, " + sender + ", but failed to sell... D:");
		}
	}
	public static void outsideMessage(String message) {
		bot.messageChat(message);
	}
	
	private void messageChat(String message) { sendMessage(channel, message); } // Simply puts a string in chat
	
}