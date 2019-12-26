package ecu.se.archive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ecu.se.Logger;

/**
 * 
 * Saves/Loads/Maintains all records.
 */
public class Archiver {
	public static final String TIME_RECORD = "time";
	public static final String TOTAL_RECORD = "total";

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

	private static Double[] totalRecords = new Double[TotalRecords.values().length * 2];
	private static long[] timeRecords = new long[TimeRecords.values().length * 2];

	/**
	 * Starts Archiver with the given directory / user
	 * 
	 * @param directory
	 * @param user
	 */
	public static void startArchiver(String directory, String user) {
		ARCHIVE_DIRECTORY = directory;
		PROFILE = user;
		init();
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
	}

	/**
	 * save() - Saves all
	 */
	private static void save() {
		for (TimeRecords tr : TimeRecords.values()) {
			set(tr, false);
		}

		Logger.Debug(Archiver.class, "save","[Archiver] Saving to: " + DIR);
		File file = new File(DIR);
		try {
			file.delete();
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(fileWriter);

			int counter = 0;
			for (int i = 0; i < timeRecords.length; i += 2) {
				writer.write(
						TIME_RECORD + " " + timeRecords[i + 1] + " " + TimeRecords.values()[counter].name() + "\n");
				counter++;
			}

			counter = 0;
			for (int i = 0; i < totalRecords.length; i += 2) {
				writer.write(TOTAL_RECORD + " " + totalRecords[i] + " " + TotalRecords.values()[counter].name() + "\n");
				counter++;
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
		Logger.Debug(Archiver.class, "load","[Archiver] Loading from: " + DIR);
		for (@SuppressWarnings("unused")
		long rec : timeRecords) {
			rec = 0;
		}
		File file = new File(DIR);
		try {
			file.createNewFile();
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			String[] parts = null;

			for (int i = 0; i < timeRecords.length; i++) {
				timeRecords[i] = 0;
			}

			for (int i = 0; i < totalRecords.length; i++) {
				totalRecords[i] = 0.0;
			}

			while ((line = reader.readLine()) != null) {
				parts = line.split(" ");
				if (parts[0].equals(TIME_RECORD)) {
					timeRecords[TimeRecords.valueOf(parts[2]).ordinal() * 2 + 1] = Long.parseLong(parts[1]);

				} else if (parts[0].equals(TOTAL_RECORD)) {
					totalRecords[TotalRecords.valueOf(parts[2]).ordinal() * 2] = Double.valueOf(parts[1]);
				} else {
					Logger.Debug(Archiver.class, "load","[X] " + line);
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
	 * 
	 * @param type
	 *            - The type of record to process.
	 * @param value
	 *            - The record to save.
	 */
	public static void set(TotalRecords type, double value) {
		int index = 2 * type.ordinal();
		totalRecords[index] += value;
		totalRecords[index + 1] += value;
	}

	/**
	 * 
	 * @param record
	 *            - the record time to store.
	 * @param reset
	 *            - If true, sets the start time to the current time, else makes
	 *            it null.
	 */
	public static void set(TimeRecords record, boolean reset) {
		int index = 2 * record.ordinal();
		long time = System.currentTimeMillis();

		if (timeRecords[index] == 0) {
			timeRecords[index] = time;
		} else {
			timeRecords[index + 1] += time - timeRecords[index];

			if (reset) {
				timeRecords[index] = time;
			} else {
				timeRecords[index] = 0;
			}
		}
	}

	/**
	 * If alltime == true, returns the record from all games, otherwise returns
	 * the record for this session (since last "new game").
	 * 
	 * @param type
	 * @param allTime
	 * @return Double, record correlating to type
	 */
	public static float getRecord(TotalRecords type, boolean allTime) {
		if (allTime)
			return totalRecords[type.ordinal() * 2].floatValue();
		else
			return totalRecords[type.ordinal() * 2 + 1].floatValue();
	}

	/**
	 * 
	 * @param type
	 * @return the time record of type type
	 */
	public static long getRecord(TimeRecords type) {
		return timeRecords[type.ordinal() * 2];
	}

	/**
	 * Updates the save file directory
	 */
	private static void updateDIR() {
		DIR = ARCHIVE_DIRECTORY + PROFILE + EXTENSION;
	}

	/**
	 * Saves and disposes Archiver.
	 */
	public static void dispose() {
		int counter = 0;
		for (int i = 0; i < timeRecords.length; i += 2) {
			if (timeRecords[i] != 0) {
				set(TimeRecords.values()[counter], false);
			}
			Logger.Debug(Archiver.class, "dispose", TimeRecords.values()[counter].name() + " " + TimeRecords.print(timeRecords[i + 1]));
			counter++;
		}
		save();
	}

}
