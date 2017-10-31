package assetManager;

import java.util.*;
import java.io.*;
import ecu.se.Globals;

public class SpriteAsset extends Asset{

	private TextureAsset sprite;
	private String name;
	public int numRows;
	public int numCol;
	public int spriteW;
	public int spriteH;
//	public Animation animation;

	//Load text file with sprite sheet 
	
	
	public SpriteAsset(String name) {
        this.name = name;

        sprite = AssetManager.getTexture(name);
        
        name = name.substring(0, name.indexOf('.')) + "." + Globals.SPRITE_INFORMATION_EXTENSION;
        System.out.println("Path=" + name);
        Scanner spriteFile = null;
        try {
        	spriteFile = new Scanner(new File(name));
        	numRows = spriteFile.nextInt();
        	numCol = spriteFile.nextInt();
        	spriteW = spriteFile.nextInt();
        	spriteH = spriteFile.nextInt();        	
        	spriteFile.close();
        }
        catch(FileNotFoundException e) {
        	System.err.println("!!! Missing Texture: Could not find sprite sheet.");
        	System.err.println("\tFailed: " + name);
        }
        System.out.println(numRows + " " + numCol + " " + spriteW + " " + spriteH);
    }
	
    public boolean loadedSuccessfully() {
        return sprite!=null;
    }
    
    public String getName () {
        return name;
    }
    
    public SpriteAsset getAsset() {
        return this;
    }
    
    public int getSpriteRows() {
    	return this.numRows;
    }
    
    public int getSpriteColumns() {
    	return this.numCol;
    }
    
    public int getSpriteWidth() {
    	return this.spriteW;
    }
    
    public int getSpriteHeight() {
    	return this.spriteH;
    }
    
    public TextureAsset getTexture() {
    	return sprite;
    }
    
//    public Animation getAnimation() {
//    	return new Animation(0, 0, 0, this);
//    }

	@Override
	public void dispose() {
//		if(animation != null)
//			animation.dispose();
		if (sprite != null)
			sprite.dispose();
	}
}