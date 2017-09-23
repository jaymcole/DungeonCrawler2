package actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.map.Map;

public class TestActor extends Actor {

//    private OrthographicCamera camera;
//    private TextureRegion textureRegion;
//
//    private Animation animation;
//    private int spriteWidth = 40;
//    private int spriteHeight = 48;
//    private int spriteSequences = 5;
//    

    public TestActor(float x, float y, float z, Map map, String spriteSheet) {
        super(x, y, z, map, spriteSheet);
        oldx = x;
        oldy = y;
        
        
//        animation = AssetManager.getSpriteSheet("texture/spritesheet/adventuretime_sprites.png").getAnimation();
//        texture = AssetManager.getTexture("texture/spritesheet/adventuretime_sprites.png").getTexture();
//        textureRegion = AssetManager.getTexture("texture/spritesheet/adventuretime_sprites.png").getTextureRegion();
        
//        bounds = Utils.getRectangleBounds(x, y, 50, 50);
    }

    @Override
    public void update(float deltaTime) {
        //bounds.setPosition(x, y);
        animation.update(deltaTime);
        animation.setXY((int) x, (int) y);
    }

    @Override
    public void render(SpriteBatch batch) {
        animation.render(batch);
    }

    @Override
    public void dispose() {

    }

}
