package ecu.se;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import ecu.se.archive.Archiver;
import ecu.se.archive.TimeRecords;
import ecu.se.archive.TotalRecords;

public class SettingsManager {

	/**
	 * Settings Diretory - The folder to store records.
	 */
	private static String WorkingDirectory;
	
	private static boolean SaveOnQuit = true;
	
	private static String LogFileName;
	private static String FileExtension = ".config";
	private static File ConfigFile;
	
	private static void Init() {
		WorkingDirectory = (String) System.getProperties().getProperty("user.dir");
		try {
			WorkingDirectory = (String) System.getProperties().getProperty("user.dir");
			LogFileName = WorkingDirectory + "\\" + "settings" + FileExtension;

			ConfigFile = new File(LogFileName);
			if (!ConfigFile.exists()) {
				ConfigFile.createNewFile();
				Logger.Debug(SettingsManager.class, "Init", "Creating a new log file.");
			}
			Logger.Debug(SettingsManager.class, "Init", "Log file at: " + LogFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void Load() {
		Init();
		try {
			FileReader reader = new FileReader(ConfigFile);
			BufferedReader br = new BufferedReader(reader);
			
			String line = br.readLine();
			while(line != null) {
				processLoadLine(line);
			}
			
			br.close();
			reader.close();
		}catch (Exception e) {
			Logger.Error(SettingsManager.class, "Load", e.getMessage());
		}
	}
	
	private static void processLoadLine(String line) {
		String[] elements = line.split(":");
		String propertyTag = elements[0];
		String propertyValue = elements[1];		
		switch (propertyTag) {
		//Globals
		case "DEBUG":
			Globals.DEBUG = Boolean.parseBoolean(propertyValue);
			break;
		case "GUI_DEBUG_RENDER_FILL":
			Globals.GUI_DEBUG_RENDER_FILL = Boolean.parseBoolean(propertyValue);
			break;
		case "srcPointer":
			Globals.srcPointer = Integer.parseInt(propertyValue);
			break;
		case "dstPointer":
			Globals.dstPointer = Integer.parseInt(propertyValue);
			break;
		case "LIGHT_DISTANCE":
			Globals.LIGHT_DISTANCE = Integer.parseInt(propertyValue);
			break;
		case "RENDER_ALL_TILES":
			Globals.RENDER_ALL_TILES = Boolean.parseBoolean(propertyValue);
			break;
		case "MAP_TILE_WIDTH":
			Globals.MAP_TILE_WIDTH = Integer.parseInt(propertyValue);
			break;
		case "MAP_TILE_HEIGHT":
			Globals.MAP_TILE_HEIGHT = Integer.parseInt(propertyValue);
			break;
		case "TILE_PIXEL_WIDTH":
			Globals.TILE_PIXEL_WIDTH = Integer.parseInt(propertyValue);
			break;
		case "TILE_PIXEL_HEIGHT":
			Globals.TILE_PIXEL_HEIGHT = Integer.parseInt(propertyValue);
			break;
		case "MIN_PATH_DENSITY":
			Globals.MIN_PATH_DENSITY = Float.parseFloat(propertyValue);
			break;
		}
	}
	
	
	public static void Save() {
		Init();
		try {
			FileWriter wr = new FileWriter(ConfigFile);
			BufferedWriter bw = new BufferedWriter(wr);
			SaveLine(bw, "DEBUG", "" + Globals.DEBUG);
			SaveLine(bw, "GUI_DEBUG_RENDER_FILL", "" + Globals.GUI_DEBUG_RENDER_FILL);
			SaveLine(bw, "srcPointer", "" + Globals.srcPointer);
			SaveLine(bw, "dstPointer", "" + Globals.dstPointer);
			SaveLine(bw, "LIGHT_DISTANCE", "" + Globals.LIGHT_DISTANCE);
			SaveLine(bw, "RENDER_ALL_TILES", "" + Globals.RENDER_ALL_TILES);
			SaveLine(bw, "MAP_TILE_WIDTH", "" + Globals.MAP_TILE_WIDTH);
			SaveLine(bw, "MAP_TILE_HEIGHT", "" + Globals.MAP_TILE_HEIGHT);
			SaveLine(bw, "TILE_PIXEL_WIDTH", "" + Globals.TILE_PIXEL_WIDTH);
			SaveLine(bw, "TILE_PIXEL_HEIGHT", "" + Globals.TILE_PIXEL_HEIGHT);
			SaveLine(bw, "MIN_PATH_DENSITY", "" + Globals.MIN_PATH_DENSITY);
			bw.close();
			wr.close();
		}catch (Exception e) {
			Logger.Error(SettingsManager.class, "Save", e.getMessage());
		}
	}
	
	public static void SaveLine(BufferedWriter bw, String propertyTag, String propertyValue) {
		try {
			bw.append(propertyTag + " "+ propertyValue+"\n");
		} catch (IOException e) {
			Logger.Error(SettingsManager.class, "SaveLine", "Failed to save: \"" + propertyTag + "\"");
			Logger.Error(SettingsManager.class, "SaveLine", e.getMessage());
		}
	}
	
	
	public static void Dispose() {
		if (SaveOnQuit)
			Save();
	}
}
