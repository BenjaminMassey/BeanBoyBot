package bbb;

import java.io.IOException;

import org.jibble.pircbot.*;

public class TwitchChat extends PircBot {

	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot

	// This class handles actually reading from and talking to Twitch Chat

	// Code for Twitch Chat functionality based on tutorial from this channel:
	// https://www.youtube.com/channel/UCoQuKOXYxUBeNWbendo3sF

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
		bot.sendRawLine("CAP REQ :twitch.tv/membership"); // Allows special stuff (viewer list)
		bot.joinChannel(channel);
		new Thread(new StreamMessage()).start();
	}

	public static void deactivate() throws IOException, IrcException {
		bot.disconnect();
		connected = false;
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		// React to a given message
		
		// Here are the commands that should be taken action for
		if (message.equalsIgnoreCase("!test"))
			messageChat("Hey here's my test!");

		if (message.startsWith("!addquote ")) {
			QuotesHandler.addQuote(message);
			messageChat("Added your quote!");
		}

		if (message.startsWith("!quote"))
			messageChat(QuotesHandler.getQuote(message));

		if (message.startsWith("!delquote ")) {
			boolean deleted = QuotesHandler.delQuote(message);
			if (deleted)
				messageChat("Deleted!");
			else
				messageChat("Failed to delete... D:");
		}

		if (message.equalsIgnoreCase("!SplitGame")) {
			messageChat("Now you can play a game in chat with the speedrun! "
					+ "The run will always have a 'cost' associated with it, "
					+ "and you can buy or sell the run at that price at any "
					+ "time. Think of it like a stock. Use !join to add yourself "
					+ "into the game, !buy to buy a run, !sell to sell a run and "
					+ "!points to see your points. A PB will give double points, "
					+ "but a reset will only give you 75% of the current cost. I'm still an early version, so "
					+ "sorry if something doesn't work. !SplitGameCommands for all commands.");
		}
		
		if (message.equalsIgnoreCase("!SplitGameCommands")) {
			messageChat("!join : join the game (start with 100 points) | "
					+ "!points : check your point count | "
					+ "!buy : invest in the current run at the current cost (on screen) | "
					+ "!sell : sell your current run for the current cost (on screen) | "
					+ "!investment : check how much you bought for (and if you bought) | "
					+ "!gamble XX : 50% chance to win XX points, 50% chance to lose XX points "
					+ "!SplitGame : rules | "
					+ "!buymessage XX : put XX on stream for 12 seconds for 1000 points");
		}

		if (message.startsWith("!points")) {
			if(!PlayersHandler.playing(sender))
				messageChat(sender + ", first you gotta !join.");
			else
				messageChat(sender + " has " + PlayersHandler.getPoints(sender) + 
						" points and is rank #" + PlayersHandler.getPlacement(sender));
		}
		
		if (message.startsWith("!join")) {
			boolean joined = PlayersHandler.addPlayer(sender);
			if (joined)
				messageChat("Thanks for joining, " + sender + "! You start with 100 points.");
			else {
				if(PlayersHandler.playing(sender))
					messageChat("Silly " + sender + "! You're already in!");
				else
					messageChat("Sorry, " + sender + ", but failed to add you... D:");
			}
		}

		if (message.equalsIgnoreCase("!buy")) {
			boolean bought = PointsGameHandler.buyRun(sender);
			if (bought)
				messageChat("Thanks for buying, " + sender + "! It cost you " + SplitGame.getCost()
						+ " points. You now have " + PlayersHandler.getPoints(sender) + " points.");
			else {
				if(!PlayersHandler.playing(sender))
					messageChat(sender + ", first you gotta !join.");
				else {
					if(PlayersHandler.getState(sender) > 0)
						messageChat("You already bought, " + sender + "!");
					else
						messageChat("Sorry, " + sender + ", but failed to buy... D:");
				}
			}
		}

		if (message.startsWith("!sell")) {
			boolean sold = PointsGameHandler.sellRun(sender);
			if (sold)
				messageChat("Thanks for selling, " + sender + "! It gave you " + SplitGame.getCost()
						+ " points. You now have " + PlayersHandler.getPoints(sender) + " points.");
			else {
				if(!PlayersHandler.playing(sender))
					messageChat(sender + ", first you gotta !join.");
				else {
					if(PlayersHandler.getState(sender) == 0)
						messageChat(sender + ", first you need to !buy");
					else
						messageChat("Sorry, " + sender + ", but failed to sell... D:");
				}
			}
		}
		
		if (message.startsWith("!investment")) {
			if(PlayersHandler.getState(sender) > 0) {
				messageChat("Hey, " + sender + ". You invested for " + PlayersHandler.getInvestment(sender)
							+ " points.");
			}
			else {
				if(!PlayersHandler.playing(sender))
					messageChat(sender + ", first you gotta !join.");
				else
					messageChat(sender + "? you haven't bought yet! You silly goose!");
			}
		}
		
		if (message.startsWith("!gamble ")) {
			if(!PlayersHandler.playing(sender))
				messageChat(sender + ", first you gotta !join.");
			else
				SplitGame.gamble(sender, message); // Messaging handled there, since need to accommodate for 0 points
		}
		
		if (message.startsWith("!buymessage ")) {
			if(!PlayersHandler.playing(sender))
				messageChat(sender + ", first you gotta !join.");
			else
				StreamMessage.add(sender, message);
		}
		
		if (message.startsWith("!give ") && sender.equals(channel.substring(1))) {
			String[] pieces = message.split(" ");
			PlayersHandler.addPoints(pieces[1], Integer.parseInt(pieces[2]));
		}
		
		if(message.equalsIgnoreCase("!leaderboard"))
			messageChat(PlayersHandler.getLeaderBoard());
	}
	
	public static String[] getViewers() {
		try {
			User[] users = bot.getUsers(channel);
			String[] viewers = new String[users.length];
			for(int i = 0; i < users.length; i++)
				viewers[i] = users[i].getNick();
			return viewers;
		}catch(Exception e) {
			return new String[0];
		}
	}

	public static void outsideMessage(String message) {
		bot.messageChat(message);
	}

	private void messageChat(String message) {
		sendMessage(channel, message);
	} // Simply puts a string in chat

}