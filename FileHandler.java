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
import java.time.LocalDateTime;
import java.nio.file.StandardCopyOption;
import java.util.Vector;

public class FileHandler {
	// Interacts with text files in terms of reading, writing and other general file stuff
	
	public static void appendToFile(String fileName, String message) {
		// Write a given message to a text file with given fileName
		
		try {
			File file = new File(fileName + ".txt");
			FileWriter fw = new FileWriter(file, true);
			fw.append(message);
			fw.flush();
			fw.close();
		}
		catch(Exception e) {System.err.println("Oops: " + e);}
	}
	
	public static void saveAll(String fileName)
	{
		
		try {
			File file = new File(fileName + ".txt");
			FileWriter fw = new FileWriter(file, false);
			for(int i = 0 ; i < PlayersHandler.getSize(); i++)
			{
				fw.write(PlayersHandler.getNameAt(i) + ":" + PlayersHandler.getPointsAt(i) + ":"  + PlayersHandler.getStateAt(i) + ":" + PlayersHandler.getInvestmentAt(i) + System.getProperty("line.separator"));
			}
			
			fw.flush();
			fw.close();
		}
		catch (Exception e)
		{
			
		}
		
	}
	

	public static boolean loadAll(String fileName)
	{
		try {
			File file = new File(fileName + ".txt");
			
			if(!file.exists())
				return false;
			
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			
			while(br.ready())
			{
				String line = br.readLine();
				
				String name;
				int points, state, investment;
				
				
				name = line.split(":")[0];
				
				points = Integer.parseInt(line.split(":")[1]);
				
				state = Integer.parseInt(line.split(":")[2]);
				
				investment = Integer.parseInt(line.split(":")[3]);

				
				PlayersHandler.addPlayer(name,points,state,investment);
			}
			
			br.close();
			fr.close();
			return  true;
			
		}
		catch (Exception e)
		{
			
		}
		
		
		return false;
	}
	
	
	public static String readFromFile(String fileName, int lineNum) {
		// Read a given lineNum of a given fileName
		
		String line = "Failed D:";
		if(getFileLength(fileName) > 0) {
			try {
				File file = new File(fileName + ".txt");
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				int currentLineIndex = 0;
				String currentLine;
				while( (currentLine = br.readLine()) != null) {
					if(currentLineIndex == lineNum) {
						line = currentLine;
						break;
					}
					currentLineIndex++;
				}
				fr.close();
				br.close();
			}
			catch(Exception e) {System.err.println("Oops: " + e);}
		}
		return line;
	}
	
	public static void deleteLineFromFile(String fileName, int lineNum) {
		// Delete a given lineNum from a given fileName by reconstructing file without that line
		
		try {
			File file = new File(fileName + ".txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			int fileLength = getFileLength(fileName);
			String content = "";
			int currentLineIndex = 0;
			String currentLine;
			while( (currentLine = br.readLine()) != null) {
				if (currentLineIndex != lineNum) {
					content += currentLine;
					if (currentLineIndex != fileLength)
						content += System.getProperty("line.separator");
				}
				currentLineIndex++;
			}
			fr.close();
			br.close();
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.flush();
			fw.close();
			//writeToFile(fileName, content);
		}
		catch(Exception e) {System.err.println("Oops: " + e);}
	}
	
	public static void backup(String fileName) throws IOException {
		try{
			// Set up path for file you're backing up
			File f = new File(fileName + ".txt");
			Path filePath = Paths.get(f.getAbsolutePath());
			
			// Get time of backup
			LocalDateTime time = LocalDateTime.now();
			String strTime = time.getYear() + "-" + time.getMonthValue() + "-" + time.getDayOfMonth() + 
								"--" + time.getHour() + "-" + time.getMinute();
			
			// Create file and set up path to copy to
			File nf = new File("backups/" + fileName + "BackupAt" + strTime + ".txt");
			nf.createNewFile();
			Path backupPath = Paths.get(nf.getAbsolutePath());
			
			// Actually copy the file using those two paths
			Files.copy(filePath,
					   backupPath,
					   StandardCopyOption.REPLACE_EXISTING,
		               StandardCopyOption.COPY_ATTRIBUTES,
		               LinkOption.NOFOLLOW_LINKS);
			
			
		}catch(Exception e) { System.err.println("Oops: " + e);}
	}
	
	public static int getFileLength(String fileName) {
		// Get the file length of a given fileName by going line by line and counting
		
		int lineNum = 0;
		try {
			File file = new File(fileName + ".txt");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			while(br.readLine() != null) {
				lineNum++;
			}
			fr.close();
			br.close();
		}
		catch(Exception e) {System.err.println("Oops: " + e);}
		return lineNum;
	}

}
