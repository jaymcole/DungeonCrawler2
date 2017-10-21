package assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class FontAsset extends Asset{
    private BitmapFont font;
    private FreeTypeFontParameter parameter;
    private String name;
    
    // TODO: Fix FontAsset constructors so that they manage characters better. 
    
    public FontAsset(String name) {
        this.name = name;
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(name));
            parameter = new FreeTypeFontParameter();
            parameter.size = 48;
            parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"'()+,-./";
            font = generator.generateFont(parameter);
            generator.dispose();    
        } catch (GdxRuntimeException e) {
            System.err.println("Unable to load texture \""+ name +"\"");
        }
    }
    
    public FontAsset(String name, int size) {
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
    
    public FontAsset(String name, FreeTypeFontParameter parameter) {
        this.name = name;
        this.parameter = parameter;
        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(name));
            parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!\"'()+,-./";
            font = generator.generateFont(parameter);
            generator.dispose();    
        } catch (GdxRuntimeException e) {
            System.err.println("Unable to load texture \""+ name +"\"");
        }
    }
    
    public boolean loadedSuccessfully() {
        return font != null;
    }
    
    public FontAsset getAsset() {
        return this;
    }
    
    public BitmapFont getFont() {
        return font;
    }
    
    public String getName () {
        return name;
    }
    
    public FreeTypeFontParameter getFreeTypeFontParameter() {
        return parameter;
    }
    
    public void dispose() {
        if(font != null)
            
            font.dispose();
    }
}
