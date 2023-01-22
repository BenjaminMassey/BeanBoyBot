package bbb;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.awt.Desktop;
import javax.imageio.ImageIO;

public class StreamImage implements Runnable{
	// Handles getting images from users
	
	private static int cost = 1000;
	public static ArrayList<String> images = new ArrayList<String>();
	private static String dirPath = null;
	
	public void run() {
		while(TwitchChat.connected) {
			if(images.size() > 0 && GUIHandler.imageApproval) {
				GUIHandler.imageApproval = false;
				String urlStr = images.get(0);
				images.remove(0);
				StreamMessage.playSound();
				// Image downloading from https://stackoverflow.com/a/5882039
				BufferedImage image = null;
				try {
				    URL url = new URL(urlStr);
				    image = ImageIO.read(url);
				    if (dirPath == null) {
					    Path currentRelativePath = Paths.get("");
					    dirPath = currentRelativePath.toAbsolutePath().toString();
				    }
				    File imageFile = new File(dirPath + "/image.png");
				    ImageIO.write(image, "png", imageFile);
				} catch (Exception e) {
					System.err.println(e);
					if (dirPath != null)
						System.err.println(dirPath);
				}
			}
			else if(images.size() > 0 && GUIHandler.previewImage) {
				GUIHandler.previewImage = false;
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					String urlStr = images.get(0);
					try {
						Desktop.getDesktop().browse(new URI(urlStr));
					} catch (IOException e) {
						throw new RuntimeException(e);
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
				}
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
		if(PlayersHandler.getPoints(user) >= cost) {
			// message looks like "!buyimage https://www.imgur.com/meme.jpg"
			String rawURL = message.substring(10);
			if(rawURL.contains("http://") || rawURL.contains("https://")) {
				PlayersHandler.removePoints(user, cost);
				System.out.println("Attempted image from " + user + " with " + message.substring(10));
				images.add(message.substring(10));
				TwitchChat.outsideMessage(user + " queued an image to the screen.");
				TwitchChat.outsidePM(user, "Your image has been added "+
							"to the queue, " + user + ".");
			}
			else {
				TwitchChat.outsidePM(user, "Sorry, " + user + ", but that "+
						"URL looks weird D: I expect something like " +
						"'https://i.imgur.com/meme.jpg'");
			}
			
		}
		else {
			TwitchChat.outsidePM(user, "Sorry, " + user + ", but it "+
						"it costs " + cost + " points to buy an image.");
		}
	}
}
