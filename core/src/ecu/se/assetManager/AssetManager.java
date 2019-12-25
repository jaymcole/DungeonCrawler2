package ecu.se.assetManager;

import java.util.HashMap;
import java.util.Map;

import ecu.se.Logger;
import ecu.se.Utils;

public class AssetManager {	
	public static HashMap<String, Asset> assets = new HashMap<String, Asset>();
	
	/**
	 * 
	 * @param name - the Font file path
	 * @param size - the font size.
	 * @returns a FontAsset
	 */
	public static FontAsset getFont(String name, int size) {
		String key =name + size + "g";
		if(assets.containsKey(key)) 
			return (FontAsset)(assets.get(key));
		
		FontAsset temp = new FontAsset(name, size);
		if (temp.loadedSuccessfully()) {
			Logger.Debug(AssetManager.class.getName(), "getFont", "Adding a new font for: " + name);
			assets.put(key, temp);
			
			
			return temp.getAsset();
		}
		Logger.Error(AssetManager.class.getName(), "getFont", "Failed to load font: \"" + name + "\"");
		return null;
	}

	/**
	 * 
	 * @param name - the filePath of the texture to use.
	 * @return a textureAsset
	 */
	public static TextureAsset getTexture(String name) {
		if(assets.containsKey(name))
			return (TextureAsset)(assets.get(name));
		

		TextureAsset temp = new TextureAsset(name);

		if (temp.loadedSuccessfully()) {
			Logger.Debug(AssetManager.class.getName(), "getTexture", "Adding a new texture for: " + name);

			assets.put(name, temp);
			return temp.getAsset();
		}
		Logger.Error(AssetManager.class.getName(), "getTexture", "Failed to load texture: \"" + name + "\"");
		return null;
	}

	/**
	 * 
	 * @param name - the filePath to the spritesheet.
	 * @returns a SpriteAsset. Used for animations.
	 */
	public static SpriteAsset getSpriteSheet(String name) {
		if(assets.containsKey(name))
			return (SpriteAsset)(assets.get(name));

		SpriteAsset temp = new SpriteAsset(name);
		if (temp.loadedSuccessfully()) {
			Logger.Debug(AssetManager.class.getName(), "getSpriteSheet", "Adding a new sprite for: " + name);
			assets.put(name, temp);
			return temp.getAsset();
		}
		Logger.Error(AssetManager.class.getName(), "getSpriteSheet", "Failed to load sprite: \"" + name + "\"");
		return null;
	}
	
	public static MusicAsset getMusic(String name) {
		
		Logger.Debug("NA", "NA",name);
		if(assets.containsKey(name))
			return (MusicAsset)(assets.get(name));

		MusicAsset temp = new MusicAsset(name);
		if (temp.loadedSuccessfully()) {
			Logger.Debug(AssetManager.class.getName(), "getMusic", "Adding a new sound for: " + name);
			assets.put(name, temp);
			return temp.getAsset();
		}
		Logger.Error(AssetManager.class.getName(), "getMusic", "Failed to load music: \"" + name + "\"");
		return null;
		
	}
	
	public static SoundAsset getSound(String name) {
		
		Logger.Debug("NA", "NA",name);
		if(assets.containsKey(name))
			return (SoundAsset)(assets.get(name));

		SoundAsset temp = new SoundAsset(name);
		if (temp.loadedSuccessfully()) {
			Logger.Debug(AssetManager.class.getName(), "getSound", "Adding a new sound for: " + name);
			assets.put(name, temp);
			return temp.getAsset();
		}
		Logger.Error(AssetManager.class.getName(), "getSound", "Failed to load sound: \"" + name + "\"");
		return null;
		
	}

	/**
	 * Disposes resources.
	 */
	public static void dispose() {
		for(Map.Entry<String, Asset> entry : assets.entrySet()) {
			Asset a = entry.getValue();
			a.dispose();
		}
	}
}
