package archive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Archiver {	
	public static final String TIME_RECORD = "time";
	public static final String TOTAL_RECORD = "total";
	
	// CURRENT EXPECTED BUGS 
	// Changing the number of recorded kept will likely destroy all old records.
	
	/**
	 * archiveDirectory - The folder to store records.
	 */
	private static String ARCHIVE_DIRECTORY = System.getProperty("user.home") + "\\Desktop\\";
	
	/**
	 * profile - User to load/save record from/to.
	 */
	private static String PROFILE = "default";
	
	private static final String EXTENSION = ".txt";
	
	/**
	 * DIR - The file to load/save to/from.
	 */
	private static String DIR;
	
	private static Double[] totalRecords = new Double[TotalRecords.values().length];
	private static long[] timeRecords = new long[TimeRecords.values().length * 2];
	
	/**
	 * Starts Archiver with the given directory / user
	 * @param directory
	 * @param user
	 */
	public static void startArchiver(String directory, String user) {
		ARCHIVE_DIRECTORY = directory;
		PROFILE = user;
		
	}
	
	/**
	 * Starts Archiver with the default save location.
	 */
	public static void startArchiver() {
		init();
	}
	
	/**
	 * Initializes Archiver
	 */
	private static void init() {
		updateDIR();
		load();
		set(TimeRecords.TIME_IN_GAME, false);
		set(TimeRecords.TOTAL_TIME_PLAYED, false);
	}
	
	/**
	 * save() - Saves all 
	 */
	private static void save() {
		System.out.println("[Archiver] Saving to: " + DIR);
		File file = new File(DIR);
		try {
			file.delete();
			file.createNewFile();
			FileWriter fileWriter= new FileWriter(file); 
			BufferedWriter writer = new BufferedWriter(fileWriter);
			
			for(int i = 1; i < timeRecords.length; i+=2) {
				writer.write(TIME_RECORD + " " + i + " " + timeRecords[i] + " " + TimeRecords.values()[(i-1)/2].name() +"\n");
			}
			
			for(int i = 0; i < totalRecords.length; i++) {
				writer.write(TOTAL_RECORD + " " + i + " " + totalRecords[i] + " " + TotalRecords.values()[i].name() + "\n");
			}
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.err.println("[Archiver] Failed to save records :'(");
			e.printStackTrace();
		}
	}
	
	/**
	 * load() - Loads previous records from 
	 */
	private static void load() {
		System.out.println("[Archiver] Loading from: " + DIR);
		for(@SuppressWarnings("unused") long rec : timeRecords) {
			rec = 0;
		}
		
		File file = new File(DIR);
		try {
			file.createNewFile();
			FileReader fileReader = new FileReader(file); 
			BufferedReader reader = new BufferedReader(fileReader);
			
			String line = null;
			String[] parts = null;
			while((line = reader.readLine()) != null) {
                parts = line.split(" ");
                if(parts[0].equals(TIME_RECORD)) {
                	timeRecords[Integer.parseInt(parts[1])] = Long.parseLong(parts[2]);
                } else if (parts[0].equals(TOTAL_RECORD)) {
                	if (parts[2].equals("null"))
                		totalRecords[Integer.parseInt(parts[1])] =  0.0;
                	else
                		totalRecords[Integer.parseInt(parts[1])] = Double.valueOf(parts[2]);
                } else {
                	System.out.println("[X] " + line);
                }
                
            } 
			reader.close();
			
		} catch (IOException e) {
			System.err.println("[Archiver] Failed to load records :'(");
			e.printStackTrace();
		}
	}
	
	/**
	 * set(type, value): Simply adds value the current records of type type.
	 * @param type - The type of record to process.
	 * @param value - The record to save.
	 */
	public static void set(TotalRecords type, double value) {
		int index = type.ordinal();
		totalRecords[index] += value;
	}
	
	/**
	 * 
	 * @param record - the record time to store.
	 * @param reset - If true, sets the start time to the current time, else makes it null.
	 */
	public static void set(TimeRecords record, boolean reset) {
		int index =2* TimeRecords.valueOf(record.name()).ordinal();
		long time = System.currentTimeMillis();

		if (timeRecords[index] == 0) {
			timeRecords[index] = time;
		}else {
			timeRecords[index+1] += time - timeRecords[index];

			if(reset) {
				timeRecords[index] = time;
			} else {
				timeRecords[index] = 0;
			}
		}
	}
	
	
	private static void updateDIR() {
		DIR = ARCHIVE_DIRECTORY + PROFILE + EXTENSION;
	}
	
	/**
	 * Saves and disposes Archiver.
	 */
	public static void dispose () {
		for(int i = 0; i < timeRecords.length; i+=2) {
			if(timeRecords[i] != 0) {
				set(TimeRecords.values()[i/2], false);
			}			
			System.out.println("[Archiver]" + TimeRecords.values()[(i+1)/2].name() + " " + TimeRecords.print(timeRecords[i+1]));
		}
		save();
	}

}
