package ecu.se.assetManager;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;

public class SoundConstants {

	private static Random random = new Random();
	
	private static String[] screams = new String[] {
			"sounds/voice/scream_female_human_01.mp3",
			"sounds/voice/screaming_male_human_02.mp3",
			"sounds/voice/screaming_01.mp3",
			
	};
	
	private static String[] laugh = new String[] {
			"sounds/voice/laughing/evil_01.mp3",
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
