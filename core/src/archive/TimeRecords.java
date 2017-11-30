package archive;

/**
 *	Record dealing with time.
 */
public enum TimeRecords {
	TIME_IN_GAME,
	TIME_IN_MENU,
	TOTAL_TIME_PLAYED,
	TIME_MOVING,
	TIME_IDLE,
	TIME_PAUSED;
	
    public static String print(long millis) {
    	long minutes = (millis / 1000)  / 60;
    	long seconds = (millis / 1000) % 60;
    	return minutes + "m " + seconds + "s";
    }
}
