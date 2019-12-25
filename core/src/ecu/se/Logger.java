package ecu.se;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileSystem;
import java.util.Date;

public class Logger {

	private static final String TAG = "Logger";

	private static boolean DebugEnabled = true;

	// Controls printing to system.out
	private static boolean PrintLogsToSystemOut = true;

	// Controls printing to
	private static boolean SaveLogsToDisk = true;

	// Overrides SaveLogsToDisk for error messages only
	private static boolean AlwaysSaveErrorLogs = true;

	// Controls logging in general.
	private static boolean LoggingEnabled = true;

	private static boolean LoggerReady = false;

	private static String LogFileName;
	private static String WorkingDirectory;
	private static String FileExtension = ".txt";
	private static File LogFile;
	private static BufferedWriter fileWriter;
	private static StringBuilder sb = new StringBuilder();

	private static void Init() {
		try {
			if (WorkingDirectory == null)
				WorkingDirectory = (String) System.getProperties().getProperty("user.dir");
			LogFileName = WorkingDirectory + "\\" + "Log" + FileExtension;

			LogFile = new File(LogFileName);
			if (!LogFile.exists()) {
				LogFile.createNewFile();
				Debug(TAG, "Init", "Creating a new log file.");
			}
			Writer writer = new FileWriter(LogFileName);
			fileWriter = new BufferedWriter(writer);
			LoggerReady = true;

			Debug(TAG, "Init", "Log file at: " + LogFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void Error(String className, String method, String message) {
		if (!LoggingEnabled && !AlwaysSaveErrorLogs)
			return;
		WriteLog(ComposeLogMessage(className, method, "ERROR", message));
	}

	public static void Debug(String className, String method, Object message) {
		Debug(className, method, message.toString());
	}
	
	public static void Debug(String className, String method, String message) {
		if (!DebugEnabled || !LoggingEnabled)
			return;
		WriteLog(ComposeLogMessage(className, method, "DEBUG", message));
	}

	public static void Info(String className, String method, String message) {
		if (!LoggingEnabled)
			return;
		WriteLog(ComposeLogMessage(className, method, "INFO", message));
	}

	private static String ComposeLogMessage(String className, String method, String type, String message) {
		sb = new StringBuilder();

		sb.append("[" + GetTimestamp() + "]");
		sb.append("[" + className + "]");
		sb.append("[" + method + "]");
		sb.append("[" + type + "] ");
		sb.append("" + message + "\n");

		return sb.toString();
	}

	private static String GetTimestamp() {
		Date date = new Date(System.currentTimeMillis());
		return date.toString();
	}

	private static void WriteLog(String message) {
		if (PrintLogsToSystemOut)
			WriteToSystem(message);
		if (SaveLogsToDisk)
			WriteToFile(message);
	}

	private static void WriteToFile(String message) {
		try {
			if (!LoggerReady || !LogFile.exists()) {
				Init();
				if (!LoggerReady || !LogFile.exists())
					throw new IOException();
			}
			fileWriter.append(message);
		} catch (IOException e) {
			System.err.println("Failed to initiate logger.");
			e.printStackTrace();
		}
	}

	private static void WriteToSystem(String message) {
		System.out.print(message);
	}

	public static void SetLogFileName(String fileName) {
		LogFileName = fileName;
		Init();
	}

	public static void SetLogFileDirectory(String directory) {
		File dir = new File(directory);
		if (dir.exists()) {
			Error(TAG, "SetLogFileDirectory", "Directory \"" + directory + "\" does not exist.");
			return;
		}
		WorkingDirectory = directory;
		Init();
	}

	public static void SetDebugEnabled(boolean enabled) {
		DebugEnabled = enabled;
	}

	public static void DeleteLog() {
		if (LogFile != null && LogFile.exists())
			LogFile.delete();
	}

	public static void Dispose() {
		try {
			if (fileWriter != null) {
				fileWriter.append("---------------------------------------------------------Logger Disposed / Game Exit---------------------------------------------------------");
				fileWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
