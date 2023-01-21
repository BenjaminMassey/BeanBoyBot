package bbb;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class StreamMessage implements Runnable{
	
	public static ArrayList<String> messages = new ArrayList<String>();
	private static File soundEffect = new File("MessageSound.wav");
	
	public void run() {
		while(TwitchChat.connected) {
			if(messages.size() > 0 && GUIHandler.messageApproval) {
				GUIHandler.messageApproval = false;
				String message = messages.get(0);
				messages.remove(0);
				playSound();
				FileHandler.writeToFile("StreamMessage", message);
				try {
					Thread.sleep(12000);
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
			if(message.length() <= 292) {
				PlayersHandler.removePoints(user, 1000);
				messages.add(user + ": " + message.substring(12));
				TwitchChat.outsideMessage(user + " queued the message: \"" +
						message.substring(12) + "\" to the screen.");
				TwitchChat.outsidePM(user, "Thanks for buying a message for 1000 points!");
			}
			else {
				TwitchChat.outsidePM(user, "Sorry, " + user + ", but there "+
						"is a 280 character limit on messages.");
			}
			
		}
		else {
			TwitchChat.outsidePM(user, "Sorry, " + user + ", but it "+
						"it costs 1000 points to buy a message.");
		}
	}
	
	// Credit to: https://www.youtube.com/watch?v=QVrxiJyLTqU for help
	public static void playSound() {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(soundEffect));
			clip.start();
		}catch(Exception e) {
			System.err.println("Error with sound: " + e);
		}
	}
}
