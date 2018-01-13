package bbb;

public class TimeForPoints implements Runnable {
	
	private static boolean running = false;
	
	public static void start() {
		running = true;
	}
	
	public void run() {
		while(TwitchChat.connected && running) {
			String[] viewers = TwitchChat.getViewers();
			for(String viewer : viewers) {
				System.out.println("I see: " + viewer);
				if(PlayersHandler.playing(viewer)) {
					PlayersHandler.addPoints(viewer, 1);
					System.out.println("Gave time for points to: " + viewer);
				}
			}
			try {
				Thread.sleep(19000);
			}catch(Exception e) {
				System.err.println("Couldn't wait in TimeForPoints: " + e);
				stop();
			}
		}
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			System.err.println("Couldn't wait in TimeForPoints: " + e);
			stop();
		}
	}
	
	public static void stop() {
		running = false;
	}
}
