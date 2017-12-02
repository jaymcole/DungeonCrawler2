package ecu.se.actors;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import ecu.se.Game;
import ecu.se.map.Direction;
import ecu.se.stats.Stats;

/**
 * 
 * No longer used. 
 */
public class RangedBadGuy extends Actor {
	
	private int maxDist = 115, minDist = 100;
	
	public RangedBadGuy(float x, float y, float z, String[] spriteSheet, int[] row) {
		super(x, y, z, spriteSheet, row);
		 currentSpeed = new Vector2(0, 0);
	     team = Team.MOB;
	     
	     Random random = new Random();
	     this.baseStats[Stats.SIZE.ordinal()] += (random.nextFloat() - 0.5f) / 2;
	     calculateStats();
	}
	
	@Override
	public void act(float deltaTime) {
		float dist = Math.abs(Game.player.getPosition().x-x) + Math.abs(Game.player.getPosition().y-y);
		
		if(dist > maxDist)
		{
			move(deltaTime, Direction.directionTo(x, y, Game.player.getPosition().x, Game.player.getPosition().y), true);
		} else if(dist < minDist)
		{
			move(deltaTime, Direction.directionTo(Game.player.getPosition().x, Game.player.getPosition().y, x, y), true);
		}
		
		lookAt = Game.player.getPositionV2();
    }
}
