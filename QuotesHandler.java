package bbb;

import java.util.Random;

public class QuotesHandler {
	
	// A part of BeanBoyBot
	// Copyright 2017 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot
	
	// Handles all the functionality behind storing, reading and deleting quotes
	
	public static void addQuote(String message) {
		// Adds a given quote to the database
		
		// Take out the "!addquote " at the beginning of the message
		String quote = "";
		for(int i = 0; i < message.length(); i++) {
			if (i > 9)
				quote += message.toCharArray()[i];
		}
		try {
			if((FileHandler.getFileLength("Quotes") % 10) == 0)
				FileHandler.backup("Quotes");
		}catch(Exception e) { System.err.println("Oops: " + e);}

		FileHandler.appendToFile("Quotes", quote + "\n");
	}
	
	public static String getQuote(String message) {
		// Gets a quote from the database. A random quote if no/bad parameter, 
		// otherwise the specified numbered quote
		
		String quote = "Failed D:"; // If no quote or messes up in some other way, just give this generic message
		try {
			// Take out the "!quote " at the beginning of the message
			String numStr = "";
			for(int i = 0; i < message.length(); i++) {
				if (i > 6)
					numStr += message.toCharArray()[i];
			}
			// Then read that number and use it to read that line of the Quotes.txt file
			int num = Integer.valueOf(numStr); // If nonsense will fail, and go to catch to get random quote
			quote = FileHandler.readFromFile("Quotes", num);
		}
		catch(Exception e) {
			if(FileHandler.getFileLength("Quotes") > 0) { // Only do if there is a quote
				Random rng = new Random();
				int rnum = rng.nextInt(FileHandler.getFileLength("Quotes"));
				quote = FileHandler.readFromFile("Quotes", rnum);
			}
		}
		return quote;
	}
	
	public static boolean delQuote(String message) {
		// Delete a given quote for the database, and just ignore if not given a recognizable number
		// Returns whether or not it deleted, in case it was given some nonsense / number that is not a quote
		
		try {
			String numStr = "";
			// Take out the "!quote " at the beginning of the message
			for(int i = 0; i < message.length(); i++) {
				if (i > 9)
					numStr += message.toCharArray()[i];
			}
			// Then read that number and use it to delete that line of the Quotes.txt file
			int num = Integer.valueOf(numStr); // If nonsense will fail, and go to catch to stop delete process
			FileHandler.deleteLineFromFile("Quotes", num);
			return true;
		}
		catch(Exception e) { 
			System.err.println("Oops: " + e);
			return false;
		}
	}
}
