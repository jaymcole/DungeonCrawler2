package ecu.se;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import actors.Stats;
import actors.Team;

public abstract class GameObject implements Comparable<GameObject> {
	protected float x, y, z;
	protected Texture texture;
	protected boolean alive;
	protected Polygon bounds;
	protected boolean idle;
	public Team team;

	public GameObject(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		alive = true;
		bounds = Utils.getRectangleBounds(x, y, 20, 20, Utils.ALIGN_BOTTOM_CENTER);
		team = team.NEUTRAL;
	}

	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = Globals.Z_LEVEL_FLOOR;
		alive = true;
		bounds = Utils.getRectangleBounds(x, y, 5, 5, Utils.ALIGN_BOTTOM_CENTER);
		team = team.NEUTRAL;
	}

	public abstract void update(float deltaTime);

	public abstract void render(SpriteBatch batch);

	public abstract void dispose();

	// public void debugRender(ShapeRenderer render) {
	// render.polygon(bounds.getVertices());
	// }

	/**
	 * Should be implements by subclassses.
	 * 
	 * @param otherObject
	 *            - The object that's colliding with this object.
	 */
	public void collision(GameObject otherObject) {

	}

	public void attack() {

	}

	public float defend(Stats type, float damage) {
		return 0;
	}

	@Override
	public int compareTo(GameObject arg0) {
		if (this.y > arg0.y)
			return -1;
		if (this.y < arg0.y)
			return 1;
		else
			return 0;
	}

	protected void kill() {
		this.alive = false;
		ObjectManager.remove(this);
		dispose();
	}

	public Vector3 getPosition() {
		return new Vector3(x, y, 1);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public Vector2 getPositionV2() {
		return new Vector2(x, y);
	}

	public Polygon getBounds() {
		return bounds;
	}

	public boolean isAlive() {
		return alive;
	}
}
