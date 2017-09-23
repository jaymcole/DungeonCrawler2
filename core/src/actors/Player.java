package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import assetManager.Animation;
import assetManager.AssetManager;
import ecu.se.Utils;
import ecu.se.map.Direction;
import ecu.se.map.Map;


public class Player extends Actor{
    
    private  OrthographicCamera camera;
    private TextureRegion textureRegion;
    
    private Animation animation;
    private int spriteWidth = 40;
    private int spriteHeight = 48;
    private int spriteSequences = 5;
    private Direction direction;
    
    public Player(float x, float y, float z, Map map, OrthographicCamera camera) {
        super(x, y, z, map);
        //texture = Utilities.loadTexture("texture/spritesheet/adventuretime_sprites.png");
        animation = AssetManager.getSpriteSheet("texture/spritesheet/adventuretime_sprites.png").getAnimation();
        texture = AssetManager.getTexture("texture/spritesheet/adventuretime_sprites.png").getTexture();
        textureRegion = AssetManager.getTexture("texture/spritesheet/adventuretime_sprites.png").getTextureRegion();
        bounds = Utils.getRectangleBounds(x, y, 50, 50);
        currentSpeed = new Vector2(0, 0);
        drag = 0.3f;
        topSpeed = 900;
        acceleration = 900;
        this.camera = camera;
    }
   

    @Override
    public void update(float deltaTime) {
        // TODO Auto-generated method stub
        textureRegion.setRegion(0, 0, spriteWidth, spriteHeight);
        oldx = x;
        oldy = y;

        x += currentSpeed.x;
        y += currentSpeed.y;
        currentSpeed.x *= drag *deltaTime;
        currentSpeed.y *= drag *deltaTime;
        /*
        if(map.currentTile((int) x, (int) y) == null || map.currentTile((int) x, (int) y).getWall()) {
            x = oldx;
            y = oldy;
        }
        */
        bounds.setPosition(x,y);
        
        animation.update(deltaTime);
        animation.setXY((int) x,(int) y);
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        animation.render(deltaTime, batch);
//        if(texture != null) {
            //batch.draw(texture, x, y);
         //   batch.draw(textureRegion, (int)(oldx-(spriteWidth*0.5)), oldy);
//        }
    }

    @Override
    public void dispose() {
        
    }
    
    public void input(float deltaTime) {
        // Pan Camera
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
             move(deltaTime, Direction.NORTH);
        } if(Gdx.input.isKeyPressed(Input.Keys.A)){
            move(deltaTime, Direction.WEST);
        } if(Gdx.input.isKeyPressed(Input.Keys.S)){
            move(deltaTime, Direction.SOUTH);
        } if(Gdx.input.isKeyPressed(Input.Keys.D)){
            move(deltaTime, Direction.EAST);
        }
       
           
    }

    

}
