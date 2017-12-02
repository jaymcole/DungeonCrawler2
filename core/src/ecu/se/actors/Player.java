package ecu.se.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import ecu.se.DecalPicker;
import ecu.se.Game;
import ecu.se.actions.Action;
import ecu.se.archive.Archiver;
import ecu.se.archive.TimeRecords;
import ecu.se.archive.TotalRecords;
import ecu.se.assetManager.AssetManager;
import ecu.se.gui.GUI;
import ecu.se.map.Direction;
import ecu.se.map.Map;
import ecu.se.objects.Decal;
import ecu.se.stats.Stats;

/**
 * 
 * The players Actor class.
 */
public class Player extends Actor {
	private Direction dir;
	
	
	
	public Player(float x, float y, float z, OrthographicCamera camera, String[] spriteSheet, int[] row) {
		super(x, y, z, spriteSheet, row);
		currentSpeed = new Vector2(0, 0);
		Archiver.set(TimeRecords.TIME_IDLE, false);
		currentHealth = 100;
		dir = Direction.NORTH;
		team = Team.PLAYER;
		attributePoints = 5;
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

	@Override
	protected void die() {
		Archiver.set(TotalRecords.DEATHS, 1);
		
		
		Map.getTile((int) x, (int) y).addObject(
				new Decal(x, y, "ass", AssetManager.getTexture(DecalPicker.getActorCorpse()).getTextureRegion()));
		Game.currentState = Game.GAME_STATE_PAUSED;
		Game.GAME_OVER = true;
		GUI.setWindow(GUI.WINDOW_GAME_OVER);
	}
	
	@Override
	public void setIdle(boolean idle) {
		this.idle = idle;
		if (idle) {
			Archiver.set(TimeRecords.TIME_IDLE, false);
		} else {
			Archiver.set(TimeRecords.TIME_MOVING, false);
		}
	}

	/**
	 * Movement controls for the player.
	 * @param deltaTime
	 */
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
