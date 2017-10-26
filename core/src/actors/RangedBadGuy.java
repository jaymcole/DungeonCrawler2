package actors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.map.Map;

import ecu.se.map.Direction;

public class RangedBadGuy extends Actor {

	private Player player;
	
	public RangedBadGuy(float x, float y, float z, Map map, String spriteSheet, Player player) {
		super(x, y, z, map, spriteSheet);
		this.player = player;
		 currentSpeed = new Vector2(0, 0);
	        drag = 0.3f;
	        topSpeed = 50;
	        acceleration = 200;
	}

	@Override
	public void update(float deltaTime) {
		float deltaX = player.getPosition().x - x;
		float deltaY = player.getPosition().y - y;
		if(deltaX > 200 || deltaX < -200 || deltaY > 200 || deltaY < -200)
		{
			move(deltaTime, Direction.directionTo(x, y, player.getPosition().x, player.getPosition().y));
		}
		 textureRegion.setRegion(0, 0, spriteWidth, spriteHeight);
	        oldx = x;
	        oldy = y;

	        x += currentSpeed.x;
	        y += currentSpeed.y;
	        currentSpeed.x *= drag *deltaTime;
	        currentSpeed.y *= drag *deltaTime;

	        bounds.setPosition(x,y);
	        animation.setIdle(idle);
	        animation.update(deltaTime);
	        animation.setXY((int) x,(int) y);
	        idle = true;
		
		
	}

	@Override
	public void render(SpriteBatch batch) {
		 animation.render(batch);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
