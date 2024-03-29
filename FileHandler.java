package bbb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.time.LocalDateTime;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

	// A part of BeanBoyBot
	// Copyright 2020 Ben Massey
	// https://github.com/BenjaminMassey/BeanBoyBot

	// Interacts with text files in terms of reading, writing and other general file
	// stuff

	public static String nl = System.getProperty("line.separator");
	
	public static void checkForFilesAndCreateIfNone() throws IOException {
		checkFileAndCreateIfNone("Players");
		checkFileAndCreateIfNone("Accounts");
		checkFileAndCreateIfNone("Output");
		checkFileAndCreateIfNone("StreamMessage");
		checkFileAndCreateIfNone("Config");
		checkFileAndCreateIfNone("Chokes");
		checkFileAndCreateIfNone("IgnoreSplits"); // This seems like a lot of files with the way I'm implementing Chokes and IgnoreSplits...
	}
	
	private static void checkFileAndCreateIfNone(String filename) throws IOException {
		File f = new File(filename + ".txt");
		if (!f.exists())
				f.createNewFile();
	}
	
	public static void writeToFile(String fileName, String message) {
		try {
			File file = new File(fileName + ".txt");
			FileWriter fw = new FileWriter(file, false);
			fw.write(message);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			System.err.println("Oops: " + e);
		}
	}
	
	public static void appendToFile(String fileName, String message) {
		// Write a given message to a text file with given fileName

		try {
			File file = new File(fileName + ".txt");
			FileWriter fw = new FileWriter(file, true);
			fw.append(message);
			fw.flush();
			fw.close();
		} catch (Exception e) {
			System.err.println("Oops: " + e);
		}
	}

	public static String readFromFile(String fileName, int lineNum) {
		// Read a given lineNum of a given fileName (starts at 0)

		String line = "Failed D:";
		if (getFileLength(fileName) > 0) {
			try {
				File file = new File(fileName + ".txt");
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				int currentLineIndex = 0;
				String currentLine;
				while ((currentLine = br.readLine()) != null) {
					if (currentLineIndex == lineNum) {
						line = currentLine;
						break;
					}
					currentLineIndex++;
				}
				fr.close();
				br.close();
			} catch (Exception e) {
				System.err.println("Oops: " + e);
			}
		}
		return line;
	}

	public static ArrayList<String> readEntireFile(String fileName) {
		//Returns an entire file as an ArrayList of lines.

		ArrayList<String> result = new ArrayList<String>();
		try {
			Scanner s = new Scanner(new File(fileName + ".txt"));
			while(s.hasNextLine()) {
				result.add(s.nextLine());
			}
			s.close();

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}

		return result;
	}

	public static void deleteLineFromFile(String fileName, int lineNum) {
		// Delete a given lineNum from a given fileName by reconstructing file without
		// that line

		try {
			File file = new File(fileName + ".txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			int fileLength = getFileLength(fileName);
			String content = "";
			int currentLineIndex = 0;
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				if (currentLineIndex != lineNum) {
					content += currentLine;
					if (currentLineIndex != fileLength)
						content += nl;
				}
				currentLineIndex++;
			}
			fr.close();
			br.close();
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.flush();
			fw.close();
			// writeToFile(fileName, content);
		} catch (Exception e) {
			System.err.println("Oops: " + e);
		}
	}

	public static void backup(String fileName) throws IOException {
		try {
			// Set up path for file you're backing up
			File f = new File(fileName + ".txt");
			Path filePath = Paths.get(f.getAbsolutePath());

			// Get time of backup
			LocalDateTime time = LocalDateTime.now();
			String strTime = time.getYear() + "-" + time.getMonthValue() + "-" + time.getDayOfMonth() + "--"
					+ time.getHour() + "-" + time.getMinute();

			// Create file and set up path to copy to
			File nf = new File("backups/" + fileName + "BackupAt" + strTime + ".txt");
			nf.createNewFile();
			Path backupPath = Paths.get(nf.getAbsolutePath());

			// Actually copy the file using those two paths
			Files.copy(filePath, backupPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES,
					LinkOption.NOFOLLOW_LINKS);

		} catch (Exception e) {
			System.err.println("Oops: " + e);
		}
	}

	public static int getFileLength(String fileName) {
		// Get the file length of a given fileName by going line by line and counting

		int lineNum = 0;
		try {
			File file = new File(fileName + ".txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while (br.readLine() != null) {
				lineNum++;
			}
			fr.close();
			br.close();
		} catch (Exception e) {
			System.err.println("Oops: " + e);
		}
		return lineNum;
	}

}
