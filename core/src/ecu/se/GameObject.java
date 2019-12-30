package ecu.se;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.actors.Team;
import ecu.se.lights.Light;
import ecu.se.objects.ItemObject;
import ecu.se.stats.Stats;

/**
 *
 * The parent class to most other Dungeon Crawler classes.
 * A basic object
 *
 */
public abstract class GameObject implements Comparable<GameObject> {
	protected float x, y, z;
	protected float width, height;
	protected Texture texture;
	private boolean alive;
	protected Polygon bounds;
	protected boolean idle;
	protected Light light;
	public Team team;
	protected boolean isColliding;

	public GameObject(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		alive = true;
		bounds = Utils.getRectangleBounds(x, y, 20, 20, Utils.ALIGN_CENTERED);
		team = Team.NEUTRAL;
		isColliding = false;
	}

	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = Globals.Z_LEVEL_FLOOR;
		alive = true;
		bounds = Utils.getRectangleBounds(x, y, 5, 5, Utils.ALIGN_CENTERED);
		team = Team.NEUTRAL;
		isColliding = false;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		isColliding = false;
		bounds.setPosition(x, y);
		team = Team.NEUTRAL;
	}

	/**
	 * Sets the objects position to pos
	 * @param pos
	 */
	public void setPosition(Vector2 pos) {
		setPosition((int)pos.x, (int)pos.y);
	}

	/**
	 * Updates this object using deltaTime
	 * @param deltaTime
	 */
	public abstract void update(float deltaTime);

	/**
	 * Renders this object
	 * @param batch
	 */
	public abstract void render(SpriteBatch batch);

	/**
	 * Disposes this object.
	 */
	public abstract void dispose();

	/**
	 * Renders the debug view for this object
	 * @param render
	 */
	public void debugRender(ShapeRenderer render) {
		render.setColor(Color.WHITE);
		if (this instanceof ItemObject)
			render.setColor(Color.CYAN);
		
		if (this != Game.player && ObjectManager.isColliding(Game.player, this))
			render.setColor(Color.YELLOW);
			
		if (isColliding)
			render.setColor(Color.ORANGE);
		render.polygon(bounds.getTransformedVertices());
		render.ellipse(bounds.getOriginX(), bounds.getOriginY(), 10, 10);
		Vector2 center = new Vector2();
		bounds.getBoundingRectangle().getCenter(center);
		float hHeight = bounds.getBoundingRectangle().height * 0.5f;
		float hWidth = bounds.getBoundingRectangle().width * 0.5f;
		render.line(center.x - hWidth, center.y - hHeight, center.x + hWidth, center.y + hHeight);
		render.line(center.x - hWidth, center.y + hHeight, center.x + hWidth, center.y - hHeight);
	}

	/**
	 * Should be implements by subclassses.
	 * 
	 * @param otherObject
	 *            - The object that's colliding with this object.
	 */
	public void onCollision(GameObject otherObject) {
		isColliding = true;
	}
	
	/**
	 * 
	 * @return a light - may be null
	 */
	public Light getLight() {
		return light;
	}

	/**
	 * 
	 * @param light - sets this objects light
	 */
	public void setLight (Light light) {
		this.light = light;
	}
	
	/**
	 * 
	 * @param attacker
	 * @param type
	 * @param damage
	 * @return the amount of damage this object actually took. 
	 * 				(Some objects may mitigate some or all damage)
	 */
	public float defend(GameObject attacker, Stats type, float damage) {
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

	/**
	 * Sets this.alive to alive
	 * @param alive
	 */
	protected void setAlive(boolean alive){
		this.alive = alive;
	}
	
	/**
	 * Any special actions this object needs to perform when dying
	 */
	protected void die() {}
	
	/**
	 * Object cleanup - required for all GameObjects
	 */
	public void kill() {
		if (!this.isAlive())
			return;
		die();
		this.alive = false;
		ObjectManager.remove(this);
		dispose();
	}
	
	/**
	 * Sets the size of this object to width x height
	 * @param width
	 * @param height
	 */
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		bounds = Utils.getRectangleBounds(x, y, width, height, Utils.ALIGN_CENTERED);
	}

	/**
	 * 
	 * @return a vector3 representing this objects world position
	 */
	public Vector3 getPosition() {
		return new Vector3(x, y, 1);
	}

	/**
	 * 
	 * @return the x world coordinate for this object
	 */
	public float getX() {
		return x;
	}

	/**
	 * 
	 * @return the y world coordinate for this object
	 */
	public float getY() {
		return y;
	}

	/**
	 * 
	 * @return a Vector2 representing this objects world coordinates
	 */
	public Vector2 getPositionV2() {
		return new Vector2(x, y);
	}

	/**
	 * 
	 * @return the bounding polygon for this object. Used mostly for collision detection.
	 */
	public Polygon getBounds() {
		return bounds;
	}

	/**
	 * 
	 * @return alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * 
	 * @return the idle state for this object
	 */
	public boolean isIdle() {
		return idle;
	}
}
