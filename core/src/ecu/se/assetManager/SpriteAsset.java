package ecu.se.assetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ecu.se.Globals;
import ecu.se.Logger;

/**
 * 
 *  SpriteAsset for animations.
 */
public class SpriteAsset extends Asset {

	private TextureAsset sprite;
	private String name;
	public int numRows;
	public int numCol;
	public int spriteW;
	public int spriteH;
	public float frameTimeLength;

	public SpriteAsset(String name) {
		this.name = name;
		sprite = AssetManager.getTexture(name);
		name = name.substring(0, name.indexOf('.')) + "." + Globals.SPRITE_INFORMATION_EXTENSION;
		numRows = 2;
		numCol = 2;
		spriteW = 1;
		spriteH = 1;
		frameTimeLength = 1;		
		
		JarFile jarFile = null;
		BufferedReader reader = null;
		FileReader fileReader = null;
		File file = null;
		
		try {
			
			if (false) {
				//file.createNewFile();
				file = new File(name);
				fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);				
			} else {
				String jarPath = SpriteAsset.class.getProtectionDomain().getCodeSource().getLocation().toString();
				jarPath = SpriteAsset.class.getProtectionDomain().getCodeSource().getLocation().toString();
				
				jarPath = jarPath.replace("file:", "");
				jarPath = jarPath.replace("jar:", "");
				
				jarFile = new JarFile(jarPath);
				
				JarEntry entry = jarFile.getJarEntry(name);
				InputStream input = jarFile.getInputStream(entry);
				
				InputStreamReader isr = new InputStreamReader(input);
				reader = new BufferedReader(isr);
			}
			
			
			
			
			
			String line = null;
			String[] parts = null;
			while ((line = reader.readLine()) != null) {
				parts = line.split(" ");
				if (parts.length > 1) {
					if (parts[0].startsWith("#ROWS")) {
						numRows = Integer.parseInt(parts[1].trim());
					} else if (parts[0].startsWith("#COLUMNS")) {
						numCol = Integer.parseInt(parts[1].trim());
					} else if (parts[0].startsWith("#FRAME_HEIGHT")) {
						spriteH = Integer.parseInt(parts[1].trim());
					} else if (parts[0].startsWith("#FRAME_WIDTH")) {
						spriteW = Integer.parseInt(parts[1].trim());
					} else if (parts[0].startsWith("#FRAME_WIDTH")) {
						spriteW = Integer.parseInt(parts[1].trim());
					} else if (parts[0].startsWith("#FRAME_SPEED")) {
						frameTimeLength = Float.parseFloat(parts[1]);
					}
				}
			}
			
		} catch (IOException e) {
			Logger.Error(this.getClass(), "Constructor", "Failed to load spritesheet info from "+name);
			Logger.Error(this.getClass(), "Constructor", e.getMessage());

			numRows = 1;
			numCol = 1;
			spriteW = sprite.getTexture().getWidth();
			spriteH = sprite.getTexture().getHeight();
			frameTimeLength = 1;
			
			sprite = AssetManager.getTexture("");
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (jarFile != null)
					jarFile.close();
				if (fileReader != null)
					fileReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.Error(getClass(), "Constructor", "Failed to close jarfile/reader: " + e.getMessage() );
				e.printStackTrace();
			}		
		}
	}
	
	/**
	 * 
	 * @return true if loaded correctly
	 */
	public boolean loadedSuccessfully() {
		return sprite != null;
	}

	/**
	 * 
	 * @return file path
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return this asset
	 */
	public SpriteAsset getAsset() {
		return this;
	}

	/**
	 * 
	 * @return current fram column
	 */
	public int getSpriteRows() {
		return this.numRows;
	}

	/**
	 * 
	 * @return total frame columns
	 */
	public int getSpriteColumns() {
		return this.numCol;
	}

	/**
	 * 
	 * @return frame width
	 */
	public int getSpriteWidth() {
		return this.spriteW;
	}

	/**
	 * 
	 * @return frame height
	 */
	public int getSpriteHeight() {
		return this.spriteH;
	}

	
	/**
	 * 
	 * @return texture used for this spriteAsset
	 */
	public TextureAsset getTexture() {
		return sprite;
	}

	@Override
	public void dispose() {
		if (sprite != null)
			sprite.dispose();
	}
}