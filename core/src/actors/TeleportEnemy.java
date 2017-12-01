package actors;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import actions.Action;
import actions.Spell_Teleport;
import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.Globals;
import ecu.se.ObjectManager;
import ecu.se.map.Direction;
import ecu.se.map.Map;
import ecu.se.map.PathNode;
import pathfinding.Pathfinder2;
import stats.Stats;
/**
 *
 * An enemy type that teleports. Current broken/unused.
 *
 */
public class TeleportEnemy extends BasicEnemy {

	private LinkedList<Vector2> path;
	private float attackRange = 650;
	private float wakeDistance = 750;
	private float sleepDistance = 1500;
	private float friendWakeDistance = 200;
	private float pathUpdateRate = 30;
	private Random rand = new Random();
	private Action tele = new Spell_Teleport(this);
	private int teleIndex;
	private int teleWait = 0;

	public TeleportEnemy(float x, float y, float z, String[] spriteSheet, int[] row) {
		super(x, y, z, spriteSheet, row);
		currentSpeed = new Vector2(0, 0);
		team = Team.MOB;
		awake = false;
		Random random = new Random();
		this.baseStats[Stats.SIZE.ordinal()] += (random.nextFloat() - 0.5f) / 2;
		this.baseStats[Stats.BASE_SPEED.ordinal()] = 1;
		calculateStats();
		this.setSecondaryAction(tele);
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

	@Override
	protected void actAwake(float deltaTime, float playerDistance) {
		counter++;

		if (counter > pathUpdateRate) {
			path = Pathfinder2.findPath(getPositionV2(), Game.player.getPositionV2());
			counter = 0;
		}

		if (path != null && path.size() > 0) {
			teleWait++;
			lookAt = path.get(0);
			move(deltaTime, Direction.angleRad(this.getPositionV2(), lookAt), true);
			teleIndex = rand.nextInt(path.size());
			if (teleWait == 500) {
				secondaryAction((int) path.get(teleIndex).x, (int) path.get(teleIndex).y);
				teleWait = 0;
			}
		}

		if (playerDistance < attackRange) {
			 primaryAction((int) Game.player.getPositionV2().x, (int) Game.player.getPositionV2().y);
		}
	}

	@Override
	protected void actAsleep(float deltaTime, float playerDistance) {

	}

	/**
	 * Called when the wake state changes from awake to asleep.
	 */
	@Override
	public void onSleep() {
		this.awake = true;

	}

	@Override
	public void onWake() {
		this.awake = true;
		for (GameObject a : ObjectManager.getActors()) {
			Actor actor = ((Actor) a);
			if (team.isFriendly(team, actor.team)) {
				if (!actor.awake && Vector2.dst(x, y, actor.getX(), actor.getY()) < friendWakeDistance) {
					actor.onWake();
				}
			}

		}
	}

	@Override
	public void debugRender(ShapeRenderer render) {
		render.setColor(Color.WHITE);
		render.polygon(bounds.getTransformedVertices());
		render.ellipse(bounds.getOriginX(), bounds.getOriginY(), 10, 10);
		Vector2 center = new Vector2();
		bounds.getBoundingRectangle().getCenter(center);
		float hHeight = bounds.getBoundingRectangle().height * 0.5f;
		float hWidth = bounds.getBoundingRectangle().width * 0.5f;
		render.line(center.x - hWidth, center.y - hHeight, center.x + hWidth, center.y + hHeight);
		render.line(center.x - hWidth, center.y + hHeight, center.x + hWidth, center.y - hHeight);

		for (int i = 0; i < Map.getCurrentFloor().getMapWidth(); i++) {
			for (int j = 0; j < Map.getCurrentFloor().getMapHeight(); j++) {
				PathNode n1 = Map.getTileByIndex(i, j).pathNode;
				PathNode n2 = n1.pathParent;

				if (n1 != null && n2 != null) {
					render.line(n1.x * Globals.TILE_PIXEL_WIDTH, n1.y * Globals.TILE_PIXEL_HEIGHT,
							n2.x * Globals.TILE_PIXEL_WIDTH, n2.y * Globals.TILE_PIXEL_HEIGHT);
				}
			}
		}

		// Draw wake distance
		// TODO: Make relative to perception?
		render.setColor(Color.YELLOW);
		render.circle(x, y, wakeDistance);

		// Draw wake distance
		// TODO: Make relative to perception?
		render.setColor(Color.RED);
		render.circle(x, y, attackRange);

		// Draw wake distance
		// TODO: Make relative to perception?
		render.setColor(Color.BLUE);
		render.circle(x, y, friendWakeDistance);

		if (path != null && path.size() > 0) {
			render.setColor(Color.MAGENTA);

			render.line(path.get(0).x, path.get(0).y, x, y);
			for (int i = 0; i < path.size() - 1; i++) {
				render.line(path.get(i).x, path.get(i).y, path.get(i + 1).x, path.get(i + 1).y);

				render.circle(path.get(i).x, path.get(i).y, 15.0f);
			}
		}
	}
}