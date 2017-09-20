package assetManager;

import java.util.ArrayList;

public class AssetManager {
    public static ArrayList<TextureAsset> textures = new ArrayList<TextureAsset>();
    public static ArrayList<FontAsset> fonts = new ArrayList<FontAsset>();
    
    public static FontAsset getFont(String name) {
        for(FontAsset f : fonts) {
            if(f.getName().equals(name)) {
                return f.getAsset();
            }
        }
        
        FontAsset temp = new FontAsset(name);
        
        if (temp.loadedSuccessfully()) {
            System.out.println("Adding a new font for: " + name);
            fonts.add(temp);
            return temp.getAsset();
        }
        System.err.println("Failed to load font.");
        return null;   
    }
    
    public static TextureAsset getTexture(String name) {
        for(TextureAsset t : textures) {
            if(t.getName().equals(name)) {
                return t.getAsset();
            }
        }
        
        TextureAsset temp = new TextureAsset(name);
        
        if (temp.loadedSuccessfully()) {
            System.out.println("Adding a new texture for: " + name);
            textures.add(temp);
            return temp.getAsset();
        }
        System.err.println("Failed to load texture.");
        return null;   
    }
    
    public static void dispose() {
        for(TextureAsset t : textures) {
            t.dispose();
        }
    }
    
}
