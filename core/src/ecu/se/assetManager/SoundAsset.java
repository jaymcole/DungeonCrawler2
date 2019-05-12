package ecu.se.assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundAsset extends Asset{

	private Sound sound;
	private String name;	
	
	public SoundAsset(String name) {
		this.name = name;
		System.out.println(Gdx.files.internal(name));
		sound = Gdx.audio.newSound(Gdx.files.internal(name));
	}
	
	/**
     * 
     * @return true if loaded correctly
     */
    public boolean loadedSuccessfully() {
        return sound != null;
    }
    
    /**
     * 
     * @return texture used
     */
    public Sound getSound() {
        return sound;
    }
    
    /**
     * 
     * @return file path
     */
    public String getName () {
        return name;
    }
        
    /**
     * 
     * @return this asset
     */
    public SoundAsset getAsset() {
        return this;
    }
    
    @Override
	public void dispose() {
    	if (sound != null)
		sound.dispose();
	}

}
