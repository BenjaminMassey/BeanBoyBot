package bbb;

import java.util.Timer;
import java.util.TimerTask;

public class TimeForPoints  extends TimerTask {
	
	private static TimerTask tt;
	private static Timer t;
	
	public static void start() {
		tt = new TimeForPoints();
		t = new Timer(true);
		t.scheduleAtFixedRate(tt, 0, 20000);
	}
	
	public void run() {
		String[] viewers = TwitchChat.getViewers();
		for(String viewer : viewers) {
			if(PlayersHandler.playing(viewer))
				PlayersHandler.addPoints(viewer, 1);
		}
	}
	
	public static void stop() {
		t.cancel();
		tt.cancel();
	}
}
