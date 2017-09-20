package assetManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ecu.se.Globals;

public class TextureAsset {
    private Texture texture;
    private TextureRegion textureRegion;
    private String name;
    
    public TextureAsset(String name) {
        this.name = name;
        try {
            texture = new Texture(name);    
            textureRegion = new TextureRegion(texture);
        } catch (GdxRuntimeException e) {
            System.err.println("Unable to load texture \""+ name +"\"");
            try {
                texture = new Texture(Globals.DEFAULT_TEXTURE);
                textureRegion = new TextureRegion(texture);
            } catch (GdxRuntimeException e2){
                System.err.println("Failed to load default texture " + Globals.DEFAULT_TEXTURE);
            }
        }
    }
    
    public boolean loadedSuccessfully() {
        return texture!=null;
    }
    
    public Texture getTexture() {
        return texture;
    }
    
    public String getName () {
        return name;
    }
    
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
    
    public TextureAsset getAsset() {
        return this;
    }
    
    public void dispose() {
        if(texture != null)
            texture.dispose();
    }
}
