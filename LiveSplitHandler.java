package bbb;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;

public class LiveSplitHandler {

	// A part of BeanBoyBot
	// Copyright 2020 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot

	// This class handles interacting with the LiveSplit application using its
	// Server component

	private static String hostName = "localhost";
	private static int portNumber = 16834;
	private static Socket socket;
	private static BufferedReader in;
	private static OutputStreamWriter osw;

	public static void initialize() throws IOException {
		socket = new Socket(hostName, portNumber);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
	}

	private static String receive(String str) {
		// Use a various "get" command to receive info from the server

		try {
			String message = "Oopsie doodles! D:";
			send(str);
			message = in.readLine();
			return message;
		} catch (Exception e) {
			System.err.println(e);
		}

		return "Nothing, sorry... :(";
	}

	private static void send(String str) {
		// Send a command to the LiveSplit server

		// System.out.println("Sending to server: " + str);
		str += "\r\n";
		try {
			send(str, osw);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private static void send(String str, OutputStreamWriter o) throws IOException {
		o.write(str, 0, str.length());
		o.flush();
	}

	public static String getSplit() {
		// Get the current split time

		return receive("getcomparisonsplittime");
	}

	public static String getCurrentTime() {
		// Get the current timer time

		return receive("getcurrenttime");
	}

	public static String getDelta() {
		// Get the current delta for PB (changed because I switch between comparisons sometimes, but still want the bot to be only on PB.)

		return receive("getdelta Personal Best");
	}

	public static String getBestDelta() {
		return receive("getdelta Best Segments");
	}

	public static String getSplitIndex() {
		// Get which split you're on

		return receive("getsplitindex");
	}

	public static String getBestPossibleTime() {
		// Get the calculated best possible time

		return receive("getbestpossibletime");
	}

	public static String getFinalTime() {
		// Get the final time

		return receive("getfinaltime Personal Best");
	}

	public static String getLastSplitTime() {
		// Get the last split time

		return receive("getlastsplittime");
	}

	public static String getComparisonSplitTime() {
		return receive("getcomparisonsplittime");
	}

	public static String getCurrentTimerPhase() {
		return receive("getcurrenttimerphase");
	}
	
	public static String getPreviousSplitName() {
		return receive("getprevioussplitname");
	}

	public static void split() {
		// Input a split

		send("split");
	}

}
