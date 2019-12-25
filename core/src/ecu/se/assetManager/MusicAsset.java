package ecu.se.assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import ecu.se.Logger;

public class MusicAsset extends Asset{

	private Music sound;
	private String name;
	
	
	
	public MusicAsset(String name) {
		this.name = name;
		Logger.Debug("NA", "NA",Gdx.files.internal(name));
		
		sound = Gdx.audio.newMusic(Gdx.files.internal(name));
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
    public Music getMusic() {
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
    public MusicAsset getAsset() {
        return this;
    }
    
    @Override
	public void dispose() {
    	if (sound != null)
		sound.dispose();
	}
	
}
