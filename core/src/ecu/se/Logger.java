package ecu.se;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class Logger {
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
	
	private static int BufferClass = 0;
	private static int BufferMethod = 0;

	private static void Init() {
		try {
			if (WorkingDirectory == null)
				WorkingDirectory = (String) System.getProperties().getProperty("user.dir");
			LogFileName = WorkingDirectory + "\\" + "Log" + FileExtension;

			LogFile = new File(LogFileName);
			if (!LogFile.exists()) {
				LogFile.createNewFile();
				Debug(Logger.class, "Init", "Creating a new log file.");
			}
			Writer writer = new FileWriter(LogFileName);
			fileWriter = new BufferedWriter(writer);
			LoggerReady = true;

			Debug(Logger.class, "Init", "Log file at: " + LogFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public static void Error(Class caller, String method, String message) {
		if (!LoggingEnabled && !AlwaysSaveErrorLogs)
			return;
		WriteLog(ComposeLogMessage(caller, method, "ERROR", message));
	}

	@SuppressWarnings("rawtypes")
	public static void Warning(Class caller, String method, String message) {
		if (!DebugEnabled || !LoggingEnabled)
			return;
		WriteLog(ComposeLogMessage(caller, method, "WARN", message));
	}

	@SuppressWarnings("rawtypes")
	public static void Debug(Class caller, String method, Object message) {
		if (message != null)
			Debug(caller, method, message.toString());
	}

	@SuppressWarnings("rawtypes")
	public static void Debug(Class caller, String method, String message) {
		if (!DebugEnabled || !LoggingEnabled)
			return;
		WriteLog(ComposeLogMessage(caller, method, "DEBUG", message));
	}

	@SuppressWarnings("rawtypes")
	public static void Info(Class caller, String method, String message) {
		if (!LoggingEnabled)
			return;
		WriteLog(ComposeLogMessage(caller, method, "INFO", message));
	}

	@SuppressWarnings("rawtypes")
	private static String ComposeLogMessage(Class caller, String method, String type, String message) {
		sb = new StringBuilder();

		String className = caller.getSimpleName();
		if (className.length() <= 0)
			className = "(InAn)" + caller.getSuperclass().getSimpleName();
		
		if (className.length() > BufferClass)
			BufferClass = className.length();
		if (method.length() > BufferMethod)
			BufferMethod = method.length();
		
		sb.append("[" + GetTimestamp() + "]");
		sb.append("[" + center(className, BufferClass) + "]");
		sb.append("[" + center(method, BufferMethod) + "]");
		sb.append("[" + type + "] ");
		sb.append("" + message + "\n");

		return sb.toString();
	}
	
	public static String center(String text, int len){
        if (len <= text.length())
            return text.substring(0, len);
        int before = (len - text.length())/2;
        if (before == 0)
            return String.format("%-" + len + "s", text);
        int rest = len - before;
        return String.format("%" + before + "s%-" + rest + "s", "", text);  
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
			fileWriter.flush();
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
			Error(Logger.class, "SetLogFileDirectory", "Directory \"" + directory + "\" does not exist.");
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
