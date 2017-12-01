package ecu.se;

import java.util.Random;

public class DecalPicker {
	private static Random random = new Random();
	
	/**
	 * Various corpse decals
	 */
	private static String[] actorCorpse = new String[] {
			"texture/decals/decal_corpse_1.png",
			"texture/decals/decal_corpse_2.png",
			"texture/decals/decal_corpse_3.png",
			"texture/decals/decal_corpse_4.png",
	};
	
	/**
	 * Various moss decals
	 */
	private static String[] moss = new String[] {
			"texture/decals/moss_1.png",
			"texture/decals/moss_2.png",
	};
	
	/**
	 * 
	 * @return a string to a suitable corpse
	 */
	public static String getActorCorpse() {
		return actorCorpse[random.nextInt(actorCorpse.length)];
	}
	
	/**
	 * 
	 * @return a string to random moss decal
	 */
	public static String getMossDecal() {
		return moss[random.nextInt(moss.length)];
	}
}
