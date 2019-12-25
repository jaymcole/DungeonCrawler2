package ecu.se.actors;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.Globals;
import ecu.se.ObjectManager;
import ecu.se.map.Direction;
import ecu.se.map.Map;
import ecu.se.map.PathNode;
import ecu.se.pathfinding.Pathfinder2;
import ecu.se.stats.Stats;

public class RandomBasicEnemy extends BasicEnemy {
	
	private LinkedList<Vector2> path;
	private float attackRange = 650;
	private float wakeDistance = 750;
	private float sleepDistance = 1500;
	private float friendWakeDistance = 200;
	private float pathUpdateRate = 30;
	private Random rand = new Random();

	
	public RandomBasicEnemy(float x, float y, float z, String[] spriteSheet, int[] row, Sound soundMoving) {
		super(x, y, z, spriteSheet, row, soundMoving);
		currentSpeed = new Vector2(0, 0);
		team = Team.MOB;
		awake = false;
		Random random = new Random();
		this.baseStats[Stats.SIZE.ordinal()] += (random.nextFloat() - 0.5f) / 2;
		this.baseStats[Stats.BASE_SPEED.ordinal()] = 1;
		calculateStats();
	}

	@Override
	public void act(float deltaTime) {
		float playerDistance = this.getPositionV2().dst(Game.player.getPositionV2());
		if (awake) {
			actAwake(deltaTime, playerDistance);
			if (playerDistance > sleepDistance) {
				this.onSleep();
			}
		} else {
			actAsleep(deltaTime, playerDistance);
			if (this.getPositionV2().dst(Game.player.getPositionV2()) < wakeDistance) {
				this.onWake();
			}
		}
		
	}
	
	private int counter = 0;
	
	/**
	 * AI Behavior when actively chasing player.
	 * 
	 * @param deltaTime
	 * @param playerDistance
	 */
	protected void actAwake(float deltaTime, float playerDistance) {
		counter++;
		if (counter > pathUpdateRate) {
			path = Pathfinder2.findPath(getPositionV2(), Game.player.getPositionV2());
			counter = 0;
		}

		if (path != null && path.size() > 0) {
			lookAt = path.get(0);
			move(deltaTime, Direction.angleRad(this.getPositionV2(), lookAt), true);
		}

		if (playerDistance < attackRange) {
			
			
			primaryAction((int) Game.player.getPositionV2().x + (rand.nextInt(400) - 200), (int) Game.player.getPositionV2().y + (rand.nextInt(400) - 200));
		}
	}

	/**
	 * AI Behavior when not actively chasing player.
	 * 
	 * @param deltaTime
	 * @param playerDistance
	 */
	protected void actAsleep(float deltaTime, float playerDistance) {

	}

	/**
	 * Called when the wake state changes from awake to asleep.
	 */
	@Override
	public void onSleep() {
		this.awake = true;

	}

	/**
	 * Called when the wake state changes from sleep to awake. Example use: A
	 * mob may want to wake nearby mobs even if the player hasn't reached their
	 * wake bubble.
	 */
	@Override
	public void onWake() {
		this.awake = true;
		for(GameObject a : ObjectManager.getActors() ) {
			Actor actor = ((Actor)a);
			if (Team.isFriendly(team, actor.team)) {
				if (!actor.awake && Vector2.dst(x, y, actor.getX(), actor.getY()) < friendWakeDistance) {	
					actor.onWake();
				}
			}
				
		}
	}
}