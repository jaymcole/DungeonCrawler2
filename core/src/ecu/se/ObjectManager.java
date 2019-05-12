package ecu.se;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;

import ecu.se.actors.Actor;
import ecu.se.map.Direction;
import ecu.se.map.Map;
import ecu.se.objects.InteractableItem;

/**
 *
 * Manages all objects
 * 	Updating/rendering
 * 	Adding/removing
 * 	Collision
 *
 */
public class ObjectManager {

	private static LinkedList<GameObject> objects;
	private static LinkedList<GameObject> addWaitList;
	private static LinkedList<GameObject> remWaitList;
	private static LinkedList<GameObject> actors;
	private static Iterator<GameObject> updater;
	private static Iterator<GameObject> collisionChecker;

	private static GameObject object;
	private static Actor actor1;
	private static Actor actor2;
	private static Boolean doneChecking;

	public ObjectManager() {
		objects = new LinkedList<GameObject>();
		addWaitList = new LinkedList<GameObject>();
		remWaitList = new LinkedList<GameObject>();
		actors = new LinkedList<GameObject>();
	}

	/**
	 * Uses deltaTime to update all GameObjects
	 * 
	 * @param deltaTime
	 *            - The time between frames.
	 */
	public static void update(float deltaTime) {
		// UPDATE OBJECTS
		updater = objects.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			if (object.isAlive()) {
				object.update(deltaTime);
			} else {
				remove(object);
			}
		}

		// // CHECK Game.player COLLISION
		updater = actors.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			if (object.isAlive()) {
				object.update(deltaTime);
				if (isColliding(Game.player, object)) {
					((Actor) object).onCollision(Game.player);
//					((Actor) object).act(deltaTime);
				}
			} else {
				remove(object);
			}
		}
		
		updater = objects.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			if (object.isAlive()) {
				object.update(deltaTime);
				if (isColliding(Game.player, object)) {
					((GameObject) object).onCollision(Game.player);
				}
			} else {
				remove(object);
			}
		}
		
		
		for (GameObject obj : Map.getTile((int) Game.player.getX(), (int) Game.player.getY()).getObjects()) {
			if (isColliding(Game.player, obj)) {
				obj.onCollision(Game.player);
			}
		}

		// CHECK ACTOR COLLISION
		updater = actors.iterator();
		while (updater.hasNext()) {

			actor1 = (Actor) updater.next();
			collisionChecker = actors.iterator();
			doneChecking = false;
			while (collisionChecker.hasNext() || doneChecking) {
				actor2 = (Actor) collisionChecker.next();
				if (isColliding(actor1, actor2) && actor1 != actor2) {
					actor1.move(deltaTime, Direction.directionTo(actor2.x, actor2.y, actor1.x, actor1.y), false);
					actor2.move((int) (deltaTime * 0.5f), Direction.directionTo(actor1.x, actor1.y, actor2.x, actor2.y),
							false);
				}
			}

			collisionChecker = objects.iterator();
			GameObject temp;
			while (collisionChecker.hasNext() || doneChecking) {
				temp = collisionChecker.next();
				if (isColliding(actor1, temp) && actor1 != temp) {
					temp.onCollision(actor1);
					actor1.onCollision(temp);
				}
			}			
			for (GameObject obj : Map.getTile((int) actor1.getX(), (int) actor1.getY()).getObjects()) {
				if (isColliding(actor1, obj)) {
					obj.onCollision(actor1);
				}
			}
		}
		updateList();
	}

	/**
	 * Renders all gameObjects.
	 * 
	 * @param deltaTime
	 *            - time between last frame.
	 * @param batch
	 *            - Used for rendering.
	 */
	public static void render(float deltaTime, SpriteBatch batch) {
		updater = objects.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			if (object.isAlive()) {
				object.render(batch);
			}
		}

		updater = actors.iterator();
		while (updater.hasNext()) {
			actor1 = (Actor) updater.next();
			if (actor1.isAlive()) {
				actor1.render(batch);
			}
		}

		Game.player.render(batch);

	}

	public static void debugRender(ShapeRenderer renderer) {

		renderer.setColor(Color.BLUE);
		renderer.polygon(Game.player.getBounds().getTransformedVertices());

		updater = actors.iterator();
		renderer.setColor(Color.GREEN);
		while (updater.hasNext()) {
			object = updater.next();
			object.debugRender(renderer);
		}

		updater = objects.iterator();
		renderer.setColor(Color.RED);
		while (updater.hasNext()) {
			object = updater.next();
			object.debugRender(renderer);
		}

	}

	/**
	 * Adds/removes waiting objects after the every update tick. Objects need to
	 * wait to be added/removed until the update cycle has completed.
	 * Adding/removing objects during an update tick with cause an exception.
	 */
	private static void updateList() {
		updater = addWaitList.iterator();
		GameObject object;

		while (updater.hasNext()) {
			object = updater.next();
			if (object.isAlive()) {
				if (object instanceof Actor) {
					actors.add(object);
				} else {
					objects.add(object);
				}
			}
		}
		addWaitList.clear();

		updater = remWaitList.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			if (object instanceof Actor) {
				actors.remove(object);
			} else {
				boolean deleted = objects.remove(object);
				if (!deleted) {
					Map.getTile((int) object.getX(), (int) object.getY()).remove(object);
				}
			}
		}

		remWaitList.clear();
		Collections.sort(actors);
	}

	/**
	 * Add a new GameObject to be updated/rendered.
	 * 
	 * @param object
	 *            - The new GameObject.
	 */
	public static void add(GameObject object) {
		addWaitList.add(object);
	}

	/**
	 * Remove a GameObject from the update/render list.
	 * 
	 * @param object
	 *            - GameObjkect to be removed.
	 */
	public static void remove(GameObject object) {
		object.dispose();
		remWaitList.add(object);
	}

	/**
	 * Cleanup all resources. Needs to be called before program is closed.
	 */
	public static void dispose() {
		GameObject object;

		updater = actors.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			object.dispose();
		}
		
		updater = objects.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			object.dispose();
		}
		
		updater = addWaitList.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			object.dispose();
		}

		updater = remWaitList.iterator();
		while (updater.hasNext()) {
			object = updater.next();
			object.dispose();
		}
	}

	public static LinkedList<GameObject> getAllMapObjects() {
		LinkedList<GameObject> temp = new LinkedList<GameObject>();
		temp.addAll(actors);
		temp.addAll(objects);
		return temp;
	}

	/**
	 * 
	 * @param object
	 * @param object2
	 * @return Returns true if object is colliding with object2.
	 */
	public static boolean isColliding(GameObject object, GameObject object2) {
		return Intersector.overlapConvexPolygons(object.getBounds(), object2.getBounds());
	}

	public static boolean mouseClick(int mouseX, int mouseY) {
		for (GameObject o : objects) {
			if (o.bounds.contains(mouseX, mouseY)) {
				if (o instanceof InteractableItem) {
					((InteractableItem) o).onClick(Game.player);
					return true;
				}
			}
		}

		for (GameObject o : Map.getTile(mouseX, mouseY).getObjects()) {
			if (o.bounds.contains(mouseX, mouseY)) {
				if (o instanceof InteractableItem) {
					((InteractableItem) o).onClick(Game.player);
					return true;
				}
			}
		}

		return false;
	}

	public static LinkedList<GameObject> getActors() {
		return actors;
	}

}
