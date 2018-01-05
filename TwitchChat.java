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
	private static TwitchChat whisperBot;

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
		// Below is common permission but I don't need it yet
		//bot.sendRawLine("CAP REQ :twitch.tv/tags");
		bot.sendRawLine("CAP REQ :twitch.tv/commands"); // Need it to parse whispers
		bot.joinChannel(channel);
		new Thread(new StreamMessage()).start();
		new Thread(new StreamEmote()).start();
	}

	public static void deactivate() throws IOException, IrcException {
		bot.disconnect();
		connected = false;
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		// React to a given message
		
		// Here are the commands that should be taken action for

		if (message.startsWith("!addquote ")) {
			QuotesHandler.addQuote(message);
			bot.sendMessage(sender, "Added your quote!");
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
					+ "based on the quality of the run, and you can buy or sell "
					+ "the run at that price at any time. Think of it like a stock. "
					+ "To start playing, type !join in chat, and then follow "
					+ "the bot ( https://www.twitch.tv/" + AccountsManager.getBotName() + " ) "
					+ "to receive whispers! Send it !Commands to see what "
					+ "you can do, and have fun!");
		}
		
		
		
		if (message.equalsIgnoreCase("!join")) {
			boolean joined = PlayersHandler.addPlayer(sender);
			if (joined)
				messageChat("Thanks for joining, " + sender + "! "
						+ "You start with 100 points. Make sure to follow me "
						+ "( https://www.twitch.tv/" + AccountsManager.getBotName() + " ) "
						+ "to start receiving my whispers, and whisper me !Commands "
						+ "to see how to play!");
			else {
				if(PlayersHandler.playing(sender))
					messageChat("Silly " + sender + "! You're already in!");
				else
					messageChat("Sorry, " + sender + ", but failed to add you... D:");
			}
		}
	}
	
	protected void onUnknown(String line) {
		if(line.contains("WHISPER")) {
			String sender = line.split("!")[0].substring(1);
			String message = line.split("WHISPER ")[1].split(" :")[1];
			
			if (message.equalsIgnoreCase("!commands")) {
				privateMessage(sender, "Use !buy to buy the run at the current cost (on screen), !sell "
						+ "to sell the run at the current cost (on screen), and !points to check your "
						+ "current number of points. The run will autosell at only 75% of the cost " 
						+ "at a reset, so be careful. !AllCommands will get you a list of all the commands, "
						+ "but those basics should get you started. Have fun!");
			}
			
			if (message.equalsIgnoreCase("!allcommands")) {
				privateMessage(sender, "!join : join the game (start with 100 points) | "
						+ "!points : check your point count | "
						+ "!buy : invest in the current run at the current cost (on screen) | "
						+ "!sell : sell your current run for the current cost (on screen) | "
						+ "!investment : check how much you bought for (and if you bought) | "
						+ "!gamble XX : 50% chance to win XX points, 50% chance to lose XX points "
						+ "!buymessage XX : put XX on stream for 12 seconds for 1000 points | "
						+ "!buyemote XX : put XX emote on stream for 8 seconds for 200 points");
			}

			if (message.equalsIgnoreCase("!points")) {
				if(!PlayersHandler.playing(sender))
					privateMessage(sender, "First you gotta !join.");
				else
					privateMessage(sender, "You have " + PlayersHandler.getPoints(sender) + 
							" points and are rank #" + PlayersHandler.getPlacement(sender));
			}
			if (message.equalsIgnoreCase("!buy")) {
				boolean bought = PointsGameHandler.buyRun(sender);
				if (bought)
					privateMessage(sender, "Thanks for buying, " + sender + "! It cost you " + SplitGame.getCost()
							+ " points. You now have " + PlayersHandler.getPoints(sender) + " points.");
				else {
					if(!PlayersHandler.playing(sender))
						privateMessage(sender, sender + ", first you gotta !join.");
					else {
						if(PlayersHandler.getState(sender) > 0)
							privateMessage(sender, "You already bought, " + sender + "!");
						else
							privateMessage(sender, "Sorry, " + sender + ", but failed to buy... D:");
					}
				}
			}

			if (message.equalsIgnoreCase("!sell")) {
				boolean sold = PointsGameHandler.sellRun(sender);
				if (sold)
					privateMessage(sender, "Thanks for selling, " + sender + "! It gave you " + SplitGame.getCost()
							+ " points. You now have " + PlayersHandler.getPoints(sender) + " points.");
				else {
					if(!PlayersHandler.playing(sender))
						privateMessage(sender, sender + ", first you gotta !join.");
					else {
						if(PlayersHandler.getState(sender) == 0)
							privateMessage(sender, sender + ", first you need to !buy");
						else
							privateMessage(sender, "Sorry, " + sender + ", but failed to sell... D:");
					}
				}
			}
			
			if (message.equalsIgnoreCase("!investment")) {
				if(PlayersHandler.getState(sender) > 0) {
					privateMessage(sender, "Hey, " + sender + ". You invested for " + PlayersHandler.getInvestment(sender)
								+ " points.");
				}
				else {
					if(!PlayersHandler.playing(sender))
						privateMessage(sender, sender + ", first you gotta !join.");
					else
						privateMessage(sender, sender + "? you haven't bought yet! You silly goose!");
				}
			}
			
			if (message.startsWith("!gamble ")) {
				if(!PlayersHandler.playing(sender))
					privateMessage(sender, sender + ", first you gotta !join.");
				else
					SplitGame.gamble(sender, message); // Messaging handled there, since need to accommodate for 0 points
			}
			
			if (message.startsWith("!buymessage ")) {
				if(!PlayersHandler.playing(sender))
					privateMessage(sender, sender + ", first you gotta !join.");
				else
					StreamMessage.add(sender, message);
			}
			
			if (message.startsWith("!buyemote ")) {
				if(!PlayersHandler.playing(sender))
					privateMessage(sender, sender + ", first you gotta !join.");
				else
					StreamEmote.add(sender, message);
			}
			
			if (message.startsWith("!give ")) {
				try {
					String[] pieces = message.split(" ");
					int points = Math.abs(Integer.parseInt(pieces[2]));
					String receiver = pieces[1];
					receiver = receiver.toLowerCase();
					if(PlayersHandler.playing(receiver)) {
						String channelOwner = channel.substring(1);
						if (sender.equals(channelOwner)) {// If channel owner
							PlayersHandler.addPoints(receiver, points); // Just give the points - not transfer
							messageChat(pieces[1] + " was blessed by THE " + channel.substring(1) + " himself!");
						}
						else { // Randy
							if(PlayersHandler.getPoints(sender) > points) {
								PlayersHandler.removePoints(sender, points);
								PlayersHandler.addPoints(receiver, points);
								messageChat(sender + " gave " + receiver + " "
										+ points + " points.");
							}
							else
								privateMessage(sender, sender + ", you don't have that many "
										+ "points to give! D:");
						}
					}
					else
						privateMessage(sender, "Cannot find player " + receiver);
				}catch(Exception e) {
					privateMessage(sender, "Failed! Make sure to use this format: "
							+ "'!give RECIPIENT NUMPOINTS'");
				}
			}
			
			if(message.startsWith("!take")) {
				try {
					String[] pieces = message.split(" ");
					int points = Math.abs(Integer.parseInt(pieces[2]));
					String loser = pieces[1];
					loser = loser.toLowerCase();
					if(PlayersHandler.playing(loser)) {
						String channelOwner = channel.substring(1);
						if (sender.equals(channelOwner)) {// If channel owner
							PlayersHandler.removePoints(loser, points); // Just give the points - not transfer
							messageChat(pieces[1] + " was smited by THE " + channel.substring(1) + " himself!");
						}
					}
					else
						privateMessage(sender, "Cannot find player " + loser);
				}catch(Exception e) {
					privateMessage(sender, "Failed! Make sure to use this format: "
							+ "'!take RECIPIENT NUMPOINTS'");
				}
			}
			
			if(message.startsWith("!check")) {
				try {
					String player = message.substring(7);
					player = player.toLowerCase();
					privateMessage(sender, player + " has " + PlayersHandler.getPoints(player)
							+ " points.");
				}catch(Exception e) {
					privateMessage(sender, "Failed to check...");
					System.err.println(e);
				}
			}
			
			if(message.equalsIgnoreCase("!leaderboard"))
				privateMessage(sender, PlayersHandler.getLeaderBoard());
		}
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
	
	public static void outsidePM(String person, String message) {
		bot.privateMessage(person, message);
	}
	
	private void privateMessage(String person, String message) {
		messageChat("/w " + person + " " + message);
	}

	private void messageChat(String message) {
		sendMessage(channel, message);
	} // Simply puts a string in chat

}