package actors;

public enum Team {
	NONE,
	NEUTRAL,
	PLAYER,
	MOB,
	FINANCIAL_DISPARAGED;
	
	/** 
	 * @param t1 - The team GameObject 1 is currently on.
	 * @param t2 - The team GameObject 2 is currently on.
	 * @return true if t1 and t2 are friends.
	 * 
	 * Note: This could be made more interesting later.
	 * 		Ex. Orc mobs might be hostile to Elven mobs but friendly to Ogre mobs.
	 */
	public static boolean isFriendly(Team t1, Team t2) {
		if (t1 == NONE || t2 == NONE)
			return false;
		else 
			return t1 == t2;
	}
}
