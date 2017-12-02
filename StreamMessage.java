package bbb;

import java.util.ArrayList;

public class StreamMessage implements Runnable{
	
	private static ArrayList<String> messages = new ArrayList<String>();
	
	public void run() {
		while(TwitchChat.connected) {
			if(messages.size() > 0) {
				String message = messages.get(0);
				messages.remove(0);
				FileHandler.writeToFile("StreamMessage", message);
				try {
					Thread.sleep(8000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				FileHandler.writeToFile("StreamMessage", "");
			}
			else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void add(String user, String message) {
		if(PlayersHandler.getPoints(user) >= 1000) {
			if(message.length() <= 280) {
				PlayersHandler.removePoints(user, 1000);
				messages.add(user + ": " + message.substring(12));
				TwitchChat.outsideMessage("Your message has been added "+
							"to the queue, " + user + ".");
			}
			else {
				TwitchChat.outsideMessage("Sorry, " + user + ", but there "+
						"is a 280 character limit on messages.");
			}
			
		}
		else {
			TwitchChat.outsideMessage("Sorry, " + user + ", but it "+
						"it costs 1000 points to buy a message.");
		}
	}
	
}
