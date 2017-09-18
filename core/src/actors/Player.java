package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Utilities;
import ecu.se.map.Direction;
import ecu.se.map.Map;

public class Player extends Actor{
    
    private  OrthographicCamera camera;
    
    public Player(float x, float y, float z, Map map, OrthographicCamera camera) {
        super(x, y, z, map);
        texture = Utilities.loadTexture("texture/floor/castle_tile.jpg");
        currentSpeed = new Vector2(0, 0);
        drag = 0.3f;
        topSpeed = 900;
        acceleration = 900;
        this.camera = camera;
    }
    float oldx = 0;
    float oldy = 0;

    @Override
    public void update(float deltaTime) {
        // TODO Auto-generated method stub
        oldx = x;
        oldy = y;

        x += currentSpeed.x;
        y += currentSpeed.y;
        currentSpeed.x *= drag *deltaTime;
        currentSpeed.y *= drag *deltaTime;
        if(map.currentTile((int) x, (int) y) == null || map.currentTile((int) x, (int) y).getWall()) {
            x = oldx;
            y = oldy;
        }
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        // TODO Auto-generated method stub
        if(texture != null) {
            batch.draw(texture, x, y);
            
        }
        Utilities.DrawDebugLine(new Vector2(x, y),  new Vector2(oldx, oldy), camera.combined);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
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
