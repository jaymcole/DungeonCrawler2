package ecu.se.map;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import ecu.se.GameObject;
import ecu.se.Lighting;
import ecu.se.Utils;
import ecu.se.assetManager.AssetManager;
import ecu.se.objects.ItemObject;

/**
 *
 * A tile - what the each floor is composed of
 *
 */
public class Tile extends GameObject {
	private int x, y, width, height;
	//private float renderX, renderY;
	private TextureRegion texture;

	public boolean isWall;

	public PathNode pathNode;

	private LinkedList<GameObject> objects;
	private LinkedList<GameObject> decals;
	
	public LineSegment[] Boundinglines;

	public Tile(int x, int y, int width, int height) {
		super(x, y);
		this.x = x;
		this.y = y;
		//renderX = x;
		//renderY = y;
		
		this.width = width;
		this.height = height;
		decals = new LinkedList<GameObject>();
		objects = new LinkedList<GameObject>();
		CreateBounds();
		texture = new TextureRegion();
		texture.setRegion(AssetManager.getTexture("texture/misc/black.jpg").getTexture());
	}

	@Override
	public void update(float deltaTime) {

		// pathNode.pathParent = null;

		for (GameObject object : decals) {
			if (!object.isIdle())
				object.update(deltaTime);
		}

		for (GameObject object : objects) {
			if (!object.isIdle())
				object.update(deltaTime);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		if (texture != null) {
			batch.setColor(Color.WHITE);
			batch.draw(texture, x, y, width, height);
		}

	}
	
	private void CreateBounds() {
		bounds = Utils.getRectangleBounds(x, y, width, height, Utils.ALIGN_BOTTOM_LEFT);
		
		if (Boundinglines == null)
			Boundinglines = new LineSegment[4];
		
		Boundinglines[0] = new LineSegment(x,y,x, y+height);
		Boundinglines[1] = new LineSegment(x,y+height,x + width, y + height);
		Boundinglines[2] = new LineSegment(x+width,y+height,x+width, y+height);
		Boundinglines[3] = new LineSegment(x,y,x+width, y);
	}
	
	public LinkedList<Vector2> getAllIntersections(LineSegment line) {
		LinkedList<Vector2> intersections = new LinkedList<Vector2>(); 
		Vector2 intersection = null;
		
		for (int i = 0 ; i < Boundinglines.length; i++) {
			LineSegment bline = Boundinglines[i];
			Intersector.intersectSegments(line.x1, line.y1, line.x2, line.y2, bline.x1, bline.y1, bline.x2, bline.y2, intersection);
			if (intersection != null) {
				intersections.add(intersection);
			}
		}
		return intersections;	
	}
	
	/**
	 * Renders the decals on this tile
	 * 
	 * @param batch
	 */
	public void renderDecals(SpriteBatch batch) {
		for (GameObject g : decals) {
			batch.setColor(Color.WHITE);
			g.render(batch);
		}

		for (GameObject object : objects) {
			batch.setColor(Color.WHITE);
			object.render(batch);
		}
	}

	@Override
	public void debugRender(ShapeRenderer renderer) {
		if (isWall)
			renderer.setColor(Color.RED);
		else
			renderer.setColor(Color.FOREST);

		renderer.polygon(bounds.getTransformedVertices());
		
//		renderer.setColor(Color.CYAN);
//		renderer.line(Boundinglines[0].x1, Boundinglines[0].y1, Boundinglines[0].x2-5, Boundinglines[0].y2);
//		
//		renderer.setColor(Color.RED);
//		renderer.line(Boundinglines[1].x1, Boundinglines[1].y1, Boundinglines[1].x2, Boundinglines[1].y2 + 5);
//		
//		renderer.setColor(Color.GREEN);
//		renderer.line(Boundinglines[2].x1, Boundinglines[2].y1, Boundinglines[2].x2+5, Boundinglines[2].y2);
//		
//		renderer.setColor(Color.YELLOW);
//		renderer.line(Boundinglines[3].x1, Boundinglines[3].y1-5, Boundinglines[3].x2, Boundinglines[3].y2);

		
		for (GameObject g : decals) {
			g.debugRender(renderer);
		}

		for (GameObject object : objects) {
			object.debugRender(renderer);
		}
	}

	/**
	 * 
	 * @return the GameObjects stored on this tile
	 */
	public LinkedList<GameObject> getObjects() {
		return objects;
	}

	/**
	 * Sets the texture this tile should use
	 * 
	 * @param texture
	 */
	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	/**
	 * Sets the x position of this tile
	 * 
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets the y position of this tile
	 * 
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Sets this tiles wall status to isWall
	 * 
	 * @param isWall
	 */
	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}

	/**
	 * 
	 * @return true if this tile is a wall
	 */
	public boolean getWall() {
		return isWall;
	}

	/**
	 * Adds an object to be stored on this tiles
	 * 
	 * @param object
	 */
	public void addObject(GameObject object) {
		if (object instanceof ItemObject) {
			objects.add(object);
		}
	}

	/**
	 * Removes an object stored on this tile
	 * @param object
	 */
	public void remove(GameObject object) {
		objects.remove(object);
	}


	/**
	 * Load operation for when this tiles parent floor is loaded.
	 */
	public void load() {
		for (GameObject g : objects) {
			if (g.getLight() != null)
				Lighting.addLight(g.getLight());
		}

		for (GameObject g : decals) {
			if (g.getLight() != null)
				Lighting.addLight(g.getLight());
		}

	}

	@Override
	public void dispose() {
		for (GameObject object : objects) {
			object.dispose();
		}

		for (GameObject object : decals) {
			object.dispose();
		}
	}

}
