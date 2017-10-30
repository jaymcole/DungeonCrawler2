package actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.map.Map;

import ecu.se.map.Direction;

public class BadGuy extends Actor{

	private Player player;
	
	public BadGuy(float x, float y, float z, Map map, String spriteSheet, Player player) {
		super(x, y, z, map, spriteSheet);
		this.player = player;
		 currentSpeed = new Vector2(0, 0);
	        drag = 0.3f;
	        topSpeed = 50;
	        acceleration = 200;
		// TODO Auto-generated constructor stub
	}
	
	public void act(float deltaTime) {
		move(deltaTime, Direction.directionTo(x, y, player.getPosition().x, player.getPosition().y));
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
