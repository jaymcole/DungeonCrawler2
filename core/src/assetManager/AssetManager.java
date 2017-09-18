package assetManager;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

public class AssetManager {
    public static ArrayList<TextureAsset> textures = new ArrayList<TextureAsset>();
    
    public static Texture getTexture(String name) {
        for(TextureAsset t : textures) {
            if(t.getName().equals(name)) {
                return t.getTexture();
            }
        }
        TextureAsset temp = new TextureAsset(name);
        if (temp.loadedSuccessfully()) {
            System.out.println("Adding a new texture for: " + name);
            textures.add(temp);
            return temp.getTexture();
        }
        return null;   
    }
    
    public static void dispose() {
        for(TextureAsset t : textures) {
            t.dispose();
        }
    }
    
}
