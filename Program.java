package bbb;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Program {
	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	// Main program to be run that will call the Twitch Bot itself
	
	public static void main(String[] args) throws IOException {
		FileHandler.checkForFilesAndCreateIfNone();
		AccountsManager.updateAll();
		GUIHandler.createWindow("Twitch Bot", "Bean.png");
	}
	
}
