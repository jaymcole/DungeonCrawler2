package assetManager;

import java.io.*;
import ecu.se.Globals;

public class SpriteAsset extends Asset {

	private TextureAsset sprite;
	private String name;
	public int numRows;
	public int numCol;
	public int spriteW;
	public int spriteH;
	public float frameTimeLength;
	// public Animation animation;

	// Load text file with sprite sheet

	public SpriteAsset(String name) {
		this.name = name;
		sprite = AssetManager.getTexture(name);
		name = name.substring(0, name.indexOf('.')) + "." + Globals.SPRITE_INFORMATION_EXTENSION;
		// System.out.println("Path=" + name);
		// Scanner spriteFile = null;
		// try {
		// spriteFile = new Scanner(new File(name));
		// numRows = spriteFile.nextInt();
		// numCol = spriteFile.nextInt();
		// spriteW = spriteFile.nextInt();
		// spriteH = spriteFile.nextInt();
		// spriteFile.close();
		// }
		// catch(FileNotFoundException e) {
		// System.err.println("!!! Missing Texture: Could not find sprite
		// sheet.");
		// System.err.println("\tFailed: " + name);
		// }
		// System.out.println(numRows + " " + numCol + " " + spriteW + " " +
		// spriteH);

		numRows = 2;
		numCol = 2;
		spriteW = 1;
		spriteH = 1;
		frameTimeLength = 1;

		File file = new File(name);
		try {
			file.createNewFile();
			FileReader fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);

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
			reader.close();

		} catch (IOException e) {
			System.err.println("[Archiver] Failed to load records :'(");
			e.printStackTrace();
		}
	}

	public boolean loadedSuccessfully() {
		return sprite != null;
	}

	public String getName() {
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

	@Override
	public void dispose() {
		if (sprite != null)
			sprite.dispose();
	}
}