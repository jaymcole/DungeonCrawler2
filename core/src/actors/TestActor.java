package actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.map.Map;

public class TestActor extends Actor {

    public TestActor(float x, float y, float z, Map map, String spriteSheet) {
        super(x, y, z, map, spriteSheet);
        oldx = x;
        oldy = y;
    }

    
    public void act(float deltaTime) {
    	
    }

    @Override
    public void render(SpriteBatch batch) {
        animation.render(batch);
    }

    @Override
    public void dispose() {

    }

}
