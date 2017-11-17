package ecu.se;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;

import actors.Actor;
import actors.Player;
import ecu.se.map.Direction;

public class ObjectManager {
	
    private static LinkedList<GameObject> objects;
    private static LinkedList<GameObject> waitList;
    private static LinkedList<GameObject> actors;
    private static Iterator<GameObject> updater;
    private static Iterator<GameObject> collisionChecker;
    private static Player player;

    private static GameObject object;
    private static Actor actor1;
    private static Actor actor2;
    private static Boolean doneChecking;
    
    public ObjectManager() {
        objects = new LinkedList<GameObject>();
        waitList = new LinkedList<GameObject>();
        actors = new LinkedList<GameObject>();
    }
    
    /**
     * Uses deltaTime to update all GameObjects
     * @param deltaTime - The time between frames.
     */
    public static void update(float deltaTime) {
        // UPDATE OBJECTS
        updater = objects.iterator();
        while(updater.hasNext()) 
        {
            object = updater.next();
            if (object.alive) {
                object.update(deltaTime);
            } else {
                remove(object);
            }
        }
        
        
//        // CHECK PLAYER COLLISION
        updater = actors.iterator();
        while(updater.hasNext())
        {
        	object = updater.next();
            if (object.alive) {
                object.update(deltaTime);            
            if(isColliding(player, object))
            {
//                player.move(deltaTime, Direction.directionTo(object.x, object.y, player.x, player.y), true);
//                if (object instanceof Actor) {
            		((Actor)object).act(deltaTime);
//                	((Actor)object).move(deltaTime, Direction.directionTo(player.x, player.y, object.x, object.y), true);
                	
//                }
            }
            } else {
                remove(object);
            }
        }
        
        // CHECK ACTOR COLLISION
        updater = actors.iterator();
        while(updater.hasNext()) {
            
            actor1 = (Actor) updater.next();
            collisionChecker = actors.iterator();
            doneChecking = false;
            while(collisionChecker.hasNext() || doneChecking) {
                actor2 = (Actor) collisionChecker.next();
                if(isColliding(actor1, actor2) && actor1 != actor2)
                {
                	actor1.move(deltaTime, Direction.directionTo(actor2.x, actor2.y, actor1.x, actor1.y), false);
                	actor2.move((int)(deltaTime * 0.5f), Direction.directionTo(actor1.x, actor1.y, actor2.x, actor2.y), false);
                }

            }
            
            collisionChecker = objects.iterator();
            GameObject temp;
            while(collisionChecker.hasNext() || doneChecking) {
                temp = collisionChecker.next();
                if(isColliding(actor1, temp) && actor1 != temp)
                {
                	temp.collision(actor1);
                	actor1.collision(temp);
                }

            }
        }
        updateList();
    }
    
    /**
     * Renders all gameObjects.
     * @param deltaTime - time between last frame.
     * @param batch - Used for rendering.
     */
    public static void render(float deltaTime, SpriteBatch batch) {
        updater = objects.iterator();        
        while(updater.hasNext()) {
            object = updater.next();
            if (object.alive) {
                object.render(batch);
            }
        }
        
        updater = actors.iterator();
        while(updater.hasNext()) {
            actor1 = (Actor) updater.next();
            if (actor1.alive) {
                actor1.render(batch);
            } 
        }
        
        player.render(batch);
        
    }
    
    public void debugRender(ShapeRenderer renderer) {

    	renderer.setColor(Color.BLUE);
    	renderer.polygon(player.getBounds().getTransformedVertices());
           	
        updater = actors.iterator();
        renderer.setColor(Color.GREEN);
        while(updater.hasNext()) 
        {
            object = updater.next();
            object.debugRender(renderer);
//            renderer.polygon(object.getBounds().getTransformedVertices());
        }
        
        
        updater = objects.iterator();
        renderer.setColor(Color.RED);
        while(updater.hasNext()) 
        {
            object = updater.next();
            object.debugRender(renderer);
//            renderer.polygon(object.getBounds().getTransformedVertices());
        }

        
        
    }
    
    /**
     * Adds/removes waiting objects after the every update tick. Objects need to wait to be added/removed until the update cycle has completed. 
     * Adding/removing objects during an update tick with cause an exception.
     */
    private static void updateList() {
        updater = waitList.iterator();
        GameObject object;
        
        while(updater.hasNext()) {
            object = updater.next();
            if(object.alive) {
            	if(object instanceof Actor) {
            		actors.add(object);
            	} else
            		objects.add(object);
            } else {
            	if(object instanceof Actor) {
            		actors.remove(object);
            	} else
            		objects.remove(object);
            }
        }
        waitList.clear();
        Collections.sort(actors);
    }
    
    /**
     * Add a new GameObject to be updated/rendered.
     * @param object - The new GameObject.
     */
    public static void add(GameObject object) {
        object.alive = true;
        waitList.add(object);
    }
    
    /**
     * Remove a GameObject from the update/render list.
     * @param object - GameObjkect to be removed.
     */
    public static void remove(GameObject object) {
        object.alive = false;
        waitList.add(object);
    }
    
    /**
     * Cleanup all resources. Needs to be called before program is closed.
     */
    public static void dispose() {
        updater = waitList.iterator();
        GameObject object;
        
        while(updater.hasNext()) {
            object = updater.next();
            object.alive = false;
            object.dispose();
        }
    }
    
    public static LinkedList<Actor> getNearbyActors() {
    	//TODO: Finish this method
		return null;
    }
    
    /**
     * 
     * @param object
     * @param object2
     * @return Returns true if object is colliding with object2.
     */
    public static boolean isColliding(GameObject object, GameObject object2)
    {
//    	return Intersector.intersectPolygons(object.getBounds(), object2.getBounds(), null);
        return Intersector.overlapConvexPolygons(object.getBounds(), object2.getBounds());
    }
    
    /**
     * Sets the current player.
     * @param player
     * @return
     */
    public static void setPlayer(Player player)
    {
        ObjectManager.player = player;
        actors.add(player);
    }
    
    public static LinkedList<GameObject> getActors() {
		return actors;
    }
    
}
