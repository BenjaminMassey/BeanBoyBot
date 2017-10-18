package bbb;

import java.io.IOException;

public class Program {
	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot

	// Main program to be run that will call the Twitch Bot itself

	public static void main(String[] args) throws IOException {
		FileHandler.checkForFilesAndCreateIfNone();
		PlayersHandler.initialize();
		AccountsManager.updateAll();
		GUIHandler.createWindow("Twitch Bot", "Bean.png"); // Start button on GUI handles other start ups
	}

}
