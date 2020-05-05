package bbb;

public class AccountsManager {
	
	// A part of BeanBoyBot
	// Copyright 2020 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	private static String chatChannel;
	private static String botName;
	private static String botOauth;
	private static String nl = System.getProperty("line.separator");
	
	public static void updateAll() {
		updateChannel();
		updateBotName();
		updateBotOauth();
	}
	private static void updateChannel() {
		chatChannel = FileHandler.readFromFile("Accounts", 0);
	}
	private static void updateBotName() {
		botName = FileHandler.readFromFile("Accounts", 1);
	}
	private static void updateBotOauth() {
		botOauth = FileHandler.readFromFile("Accounts", 2);
	}
	private static void write() {
		while(FileHandler.getFileLength("Accounts") != 0)
			FileHandler.deleteLineFromFile("Accounts", 0);
		FileHandler.appendToFile("Accounts", chatChannel + nl);
		FileHandler.appendToFile("Accounts", botName + nl);
		FileHandler.appendToFile("Accounts", botOauth);
	}
	public static void setChatChannel(String channel) {
		chatChannel = "#" + channel.toLowerCase();
		write();
	}
	public static void setBotName(String name) {
		botName = name;
		write();
	}
	public static void setBotOauth(String oauth) {
		if(oauth.startsWith("oauth:"))
			botOauth = oauth;
		else
			botOauth = "oauth:" + oauth;
		write();
	}
	public static String getChatChannel() {
		return chatChannel;
	}
	public static String getBotName() {
		return botName;
	}
	public static String getBotOauth() {
		return botOauth;
	}
}
