package assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ecu.se.gui.GUI;

/**
 * 
 * Stores font information
 */
public class FontAsset extends Asset{
    private BitmapFont font;
    private FreeTypeFontParameter parameter;
    private String name;
     
    public FontAsset(String name, int size) {
    	size = (int)(size * (GUI.conversionX + GUI.conversionY) * 0.5f);
    	this.name = name;
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(name));
            parameter = new FreeTypeFontParameter();
            parameter.size = size;
            parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"'()+,-./";
            font = generator.generateFont(parameter);
            generator.dispose();    
        } catch (GdxRuntimeException e) {
            System.err.println("Unable to load texture \""+ name +"\"");
        }        
    }
    
    /**
     * 
     * @return true if font was loaded correctly.
     */
    public boolean loadedSuccessfully() {
        return font != null;
    }
    
    /**
     * 
     * @returns this fontAsset (for some reason). 
     */
    public FontAsset getAsset() {
        return this;
    }
    
    /**
     * 
     * @return font
     */
    public BitmapFont getFont() {
        return font;
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
     * @return the parameters used to construct this font asset.
     */
    public FreeTypeFontParameter getFreeTypeFontParameter() {
        return parameter;
    }
    
    @Override
    public void dispose() {
        if(font != null)
            
            font.dispose();
    }
}
