package ecu.se.assetManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ecu.se.Globals;

/**
 * 
 * Non animated textures
 */
public class TextureAsset extends Asset{
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
    
    /**
     * 
     * @return true if loaded correctly
     */
    public boolean loadedSuccessfully() {
        return texture!=null;
    }
    
    /**
     * 
     * @return texture used
     */
    public Texture getTexture() {
        return texture;
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
     * @return this textures texture region
     */
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
    
    /**
     * 
     * @return this asset
     */
    public TextureAsset getAsset() {
        return this;
    }
    
    @Override
    public void dispose() {
        if(texture != null)
            texture.dispose();
    }
}
