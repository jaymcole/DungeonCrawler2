package ecu.se.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

import actors.Player;
import assetManager.AssetManager;
import assetManager.TextureAsset;

public class HUD {
    
    private Player player;
    private int halfWidth, halfHeight;
    private int x, y;
    private OrthographicCamera hudCamera;
    private Matrix4 oldProjectionMatrix;
    
    private final float defaultWidth = 1920.0f;
    private final float defaultHeight = 1080.0f;
    private float conversionX;
    private float conversionY; 
    
    
    
    // TEST Objects
    private BitmapFont font;
    private TextureAsset textureAsset;
    private TextureRegion textureRegion;
    private Texture texture;
    // END TEST objects
    
    public HUD (Player player, int screenWidth, int screenHeight) {
        this.player = player;
        halfWidth = (int)(screenWidth * 0.5);
        conversionX = screenWidth / defaultWidth; 
                
        halfHeight = (int)(screenHeight *0.5);
        conversionY = screenHeight / defaultHeight;
        
        hudCamera = new OrthographicCamera(screenWidth, screenHeight);
        font = AssetManager.getFont("font/font_jay.ttf").getFont();
        
        textureAsset = AssetManager.getTexture("texture/hud/gui_test.png");
        texture = textureAsset.getTexture();
    }
    
    public void update (float deltaTime) {
        
    }
    
    public void render(SpriteBatch batch) {
        oldProjectionMatrix = batch.getProjectionMatrix();
        batch.setProjectionMatrix(hudCamera.combined);
        // RENDER CODE HERE
        
        
//        font.draw(batch, "(0 , 0)", getProportionalX(0), getProportionalY(0));
//        font.draw(batch, "(1920 , 0)", getProportionalX(1920), getProportionalY(0));
//        font.draw(batch, "(0 , 1080)", getProportionalX(0), getProportionalY(1080));
//        font.draw(batch, "(1920 , 1080)", getProportionalX(1920), getProportionalY(1080));
        batch.draw(texture, getProportionalX(0), getProportionalY(0), convertX(1920), convertY(1080));
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), getProportionalX(20), getProportionalY(1080-40));
        
        
        // END RENDER CODE
        batch.setProjectionMatrix(oldProjectionMatrix);
    }
    
    private int convertX (int x) {
        return (int)(x * conversionX);
    }
    private int convertY (int y) {
        return (int)(y * conversionY);
    }
    
    private int getProportionalX(int x) {
        return convertX(x) - halfWidth;
    }
    
    private int getProportionalY(int y) {
        return convertY(y) - halfHeight;
    }
    
    
}
