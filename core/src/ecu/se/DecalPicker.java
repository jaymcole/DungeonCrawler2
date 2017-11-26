package ecu.se;

import java.util.Random;

public class DecalPicker {
	private static Random random = new Random();
	
	private static String[] actorCorpse = new String[] {
			"texture/decals/decal_corpse_1.png",
			"texture/decals/decal_corpse_2.png",
			"texture/decals/decal_corpse_3.png",
			"texture/decals/decal_corpse_4.png",
	};
	
	private static String[] moss = new String[] {
			"texture/decals/moss_1.png",
			"texture/decals/moss_2.png",
	};
	
	
	public static String getActorCorpse() {
		return actorCorpse[random.nextInt(actorCorpse.length)];
	}
	
	public static String getMossDecal() {
		return moss[random.nextInt(moss.length)];
	}
}
