package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import actions.Action;
import actions.Spell_Explosion;
import actions.Spell_Fireball;
import actions.Spell_Teleport;
import archive.Archiver;
import archive.TimeRecords;
import ecu.se.ObjectManager;
import ecu.se.map.Direction;
import ecu.se.map.Map;
import ecu.se.objects.InteractableItem;
import ecu.se.objects.ItemObject;
import ecu.se.objects.Projectile;
import stats.Stats;

public class Player extends Actor {
	private Direction dir;
	
	public Player(float x, float y, float z, Map map, OrthographicCamera camera, String spriteSheet) {
		super(x, y, z, map, spriteSheet);
		currentSpeed = new Vector2(0, 0);
		Archiver.set(TimeRecords.TIME_IDLE, false);
		currentHealth = 100;
		dir = Direction.NORTH;
		team = Team.PLAYER;
	
		primaryAction = new Spell_Fireball(this);
		secondaryAction = new Spell_Fireball(this);
	}
	
	@Override
	protected void updateMovement(float deltaTime) {
		oldx = x;
		oldy = y;

		x += currentSpeed.x;
		y += currentSpeed.y;
		currentSpeed.x *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] * deltaTime;
		currentSpeed.y *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] * deltaTime;

		if (map.currentTile((int) x, (int) y) == null || map.currentTile((int) x, (int) y).getWall()) {
			x = oldx;
			y = oldy;
		}
		
		for (Action a : actions) {
			if (a.isActive()) {
				a.update(deltaTime);
			}
		}
		bounds.setPosition(x, y);
	}

	public void act(float deltaTime) {
		
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
		if (idle) {
			Archiver.set(TimeRecords.TIME_IDLE, false);
		} else {
			Archiver.set(TimeRecords.TIME_MOVING, false);
		}
	}

	//TODO: Come up with a better solution to this (if someone could make a system for adding directions, that'd be great).
	public void input(float deltaTime) {
		if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
			dir = Direction.NORTHEAST;
			move(deltaTime, dir, true);
			return;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
			dir = Direction.NORTHWEST;
			move(deltaTime, dir, true);
			return;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
			dir = Direction.SOUTHEAST;
			move(deltaTime, dir, true);
			return;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
			dir = Direction.SOUTHWEST;
			move(deltaTime, dir, true);
			return;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			dir = Direction.NORTH;
			move(deltaTime, dir, true);
			return;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			dir = Direction.WEST;
			move(deltaTime, dir, true);
			return;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			dir = Direction.SOUTH;
			move(deltaTime, dir, true);
			return;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			dir = Direction.EAST;
			move(deltaTime, dir, true);
			return;
		}

	}
	
	public void attack(double x, double y) {
//		double angleInRadians = Math.atan2(y - this.y, x - this.x) - Math.atan2(0, 0);
//		ObjectManager.add(new Projectile(this.x, this.y, angleInRadians,  this));
	}

	
	
}
