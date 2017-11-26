package ecu.se;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import actors.Team;
import ecu.se.objects.ItemObject;
import ecu.se.objects.Light;
import stats.Stats;

public abstract class GameObject implements Comparable<GameObject> {
	protected float x, y, z;
	protected float width, height;
	protected Texture texture;
	private boolean alive;
	protected Polygon bounds;
	protected boolean idle;
	protected Light light;
	public Team team;

	public GameObject(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		alive = true;
		bounds = Utils.getRectangleBounds(x, y, 20, 20, Utils.ALIGN_CENTERED);
		team = Team.NEUTRAL;
	}

	public GameObject(float x, float y) {
		this.x = x;
		this.y = y;
		this.z = Globals.Z_LEVEL_FLOOR;
		alive = true;
		bounds = Utils.getRectangleBounds(x, y, 5, 5, Utils.ALIGN_CENTERED);
		team = Team.NEUTRAL;
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		bounds.setPosition(x, y);
	}

	public void setPosition(Vector2 pos) {
		setPosition((int)pos.x, (int)pos.y);
	}

	public abstract void update(float deltaTime);

	public abstract void render(SpriteBatch batch);

	public abstract void dispose();

	public void debugRender(ShapeRenderer render) {
		render.setColor(Color.WHITE);
		if (this instanceof ItemObject)
			render.setColor(Color.CYAN);
		
		if (this != Game.player && ObjectManager.isColliding(Game.player, this))
			render.setColor(Color.YELLOW);
			
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

	}
	
	public Light getLight() {
		return light;
	}

	public void setLight (Light light) {
		this.light = light;
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

	protected void setAlive(boolean alive){
		this.alive = alive;
	}
	
	protected void die() {}
	
	public void kill() {
		die();
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

	public boolean isIdle() {
		return idle;
	}
}
