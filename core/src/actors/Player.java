package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels.TextureRegionInitializer;
import com.badlogic.gdx.math.Vector2;

import archive.Archiver;
import archive.TimeRecords;
import assetManager.Animation;
import assetManager.AssetManager;
import ecu.se.map.Direction;
import ecu.se.map.Map;


public class Player extends Actor {

    public Player(float x, float y, float z, Map map, OrthographicCamera camera, String spriteSheet) {
        super(x, y, z, map, spriteSheet);
        currentSpeed = new Vector2(0, 0);
//        drag = 0.3f;
//        topSpeed = 900;
//        acceleration = 300;
        Archiver.set(TimeRecords.TIME_IDLE, false);
        Stats.print(currentStats);
        currentHealth = 100;
    }
   

    @Override
    public void update(float deltaTime) {
//        textureRegion.setRegion(0, 0, spriteWidth, spriteHeight);
        oldx = x;
        oldy = y;

        x += currentSpeed.x;
        y += currentSpeed.y;
        currentSpeed.x *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] * deltaTime;
        currentSpeed.y *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] * deltaTime;
        
        if(map.currentTile((int) x, (int) y) == null || map.currentTile((int) x, (int) y).getWall()) {
            x = oldx;
            y = oldy;
        }
        
        bounds.setPosition(x,y);
        animation.setIdle(idle);
        animation.update(deltaTime);
        animation.setXY((int) x,(int) y);
        idle = true;
    }
    
    public void act(float deltaTime) {
    	
    }
    
    public void setIdle(boolean idle) {
    	this.idle = idle;
    	if(idle) {
    		Archiver.set(TimeRecords.TIME_IDLE, false);
    	} else {
    		Archiver.set(TimeRecords.TIME_MOVING, false);
    	}
    }

    public void input(float deltaTime) {
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
