package ecu.se.assetManager;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;

public class SoundConstants {

	private static Random random = new Random();
	
	private static String[] screams = new String[] {
			"sounds/CSounds/scream2cut.mp3",
			"sounds/CSounds/growl1.mp3",
			"sounds/CSounds/scream1cut.mp3",
			
	};
	
	private static String[] laugh = new String[] {
			"sounds/CSounds/laugh1.mp3",
			"sounds/voice/laughing/male_01.mp3",
			"sounds/voice/laughing/male_02.mp3",
			"sounds/voice/laughing/hyena_01.mp3"			
	};
	
	public static Sound getScream() {
		return AssetManager.getSound(screams[random.nextInt(screams.length)]).getSound();
		
	}
	
	public static Sound getLaugh() {
		return AssetManager.getSound(laugh[random.nextInt(laugh.length)]).getSound();
		
	}
	
	
	
	
	
	
	
}
