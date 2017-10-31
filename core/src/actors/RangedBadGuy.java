package actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.map.Map;

import ecu.se.map.Direction;

public class RangedBadGuy extends Actor {

	private Player player;
	
	private int maxDist = 115, minDist = 100;
	
	public RangedBadGuy(float x, float y, float z, Map map, String spriteSheet, Player player) {
		super(x, y, z, map, spriteSheet);
		this.player = player;
		 currentSpeed = new Vector2(0, 0);
	        drag = 0.3f;
	        topSpeed = 50;
	        acceleration = 200;
	}
	
	public void act(float deltaTime) {
		float deltaX = player.getPosition().x - x;
		float deltaY = player.getPosition().y - y;
		
		
		float dist = Math.abs(player.getPosition().x-x) + Math.abs(player.getPosition().y-y);
		
		if(dist > maxDist)
		{
			move(deltaTime, Direction.directionTo(x, y, player.getPosition().x, player.getPosition().y));
		} else if(dist < minDist)
		{
			move(deltaTime, Direction.directionTo(player.getPosition().x, player.getPosition().y, x, y));
		}
		

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
