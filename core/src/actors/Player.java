package actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import actions.Action;
import actions.Spell_Fireball;
import actions.Spell_Teleport;
import archive.Archiver;
import archive.TimeRecords;
import ecu.se.map.Direction;
import ecu.se.map.Map;
import stats.Stats;

public class Player extends Actor {
	private Direction dir;
	
	
	
	public Player(float x, float y, float z, OrthographicCamera camera, String[] spriteSheet, int[] row) {
		super(x, y, z, spriteSheet, row);
		currentSpeed = new Vector2(0, 0);
		Archiver.set(TimeRecords.TIME_IDLE, false);
		currentHealth = 5;
		dir = Direction.NORTH;
		team = Team.PLAYER;
		attributePoints = 100;
		primaryAction = new Spell_Fireball(this);
		secondaryAction = new Spell_Teleport(this);
	}
	
	@Override
	protected void updateMovement(float deltaTime) {
		oldx = x;
		oldy = y;

		x += currentSpeed.x;
		y += currentSpeed.y;
		currentSpeed.x *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] * deltaTime;
		currentSpeed.y *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] * deltaTime;

		if (Map.getTile((int) x, (int) y) == null || Map.getTile((int) x, (int) y).getWall()) {
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
}
