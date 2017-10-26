package ecu.se;

import java.util.ArrayList;
import java.util.Iterator;

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

    private ArrayList<GameObject> objects;
    private ArrayList<GameObject> waitList;
    private ArrayList<GameObject> actors;
    private Iterator<GameObject> updater;
    private Iterator<GameObject> collisionChecker;
    private Player player;

    private GameObject object;
    private Actor actor1;
    private Actor actor2;
    private Boolean doneChecking;
    
    public ObjectManager() {
        objects = new ArrayList<GameObject>();
        waitList = new ArrayList<GameObject>();
        actors = new ArrayList<GameObject>();
    }
    
    /**
     * Uses deltaTime to update all GameObjects
     * @param deltaTime - The time between frames.
     */
    public void update(float deltaTime) {
        // UPDATE OBJECTS
        updater = objects.iterator();
        while(updater.hasNext()) 
        {
            object = updater.next();
            if (object.alive) {
                object.update(deltaTime);
            } else {
                this.remove(object);
            }
        }
        
        
        // CHECK PLAYER COLLISION
        updater = actors.iterator();
        while(updater.hasNext())
        {
        	object = updater.next();
            if (object.alive) {
                object.update(deltaTime);            
            if(isColliding(player, object))
            {
//                Utils.println(this, "Player is colliding with Object");
                player.move(deltaTime, Direction.directionTo(object.x, object.y, player.x, player.y));
                if (object instanceof Actor) {
                	((Actor)object).move(deltaTime, Direction.directionTo(player.x, player.y, object.x, object.y));
                	
                }
            }
            } else {
                this.remove(object);
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
                	actor1.move(deltaTime, Direction.directionTo(actor2.x, actor2.y, actor1.x, actor1.y));
//                	actor2.move(deltaTime, Direction.directionTo(actor1.x, actor1.y, actor2.x, actor2.y));
//                    actor1.revert();
//                    actor2.revert();
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
    public void render(float deltaTime, SpriteBatch batch) {
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
//            else {
//                Utils.println(this, "Actor is not rendering.");
//            }
        }
        
        player.render(batch);
        
    }
    
    ShapeRenderer debugRenderer = new ShapeRenderer();
    public void debugRender(Matrix4 projection) {

        debugRenderer.begin(ShapeType.Line);
        debugRenderer.setProjectionMatrix(projection);
        debugRenderer.setColor(Color.BLUE);
        debugRenderer.polygon(player.getBounds().getTransformedVertices());
        
        updater = actors.iterator();
        debugRenderer.setColor(Color.GREEN);
        while(updater.hasNext()) 
        {
            object = updater.next();
            debugRenderer.polygon(object.getBounds().getTransformedVertices());
        }
        
        
        updater = objects.iterator();
        debugRenderer.setColor(Color.RED);
        while(updater.hasNext()) 
        {
            object = updater.next();
            debugRenderer.polygon(object.getBounds().getTransformedVertices());
        }

        debugRenderer.end();
        
        
    }
    
    /**
     * Adds/removes waiting objects after the every update tick. Objects need to wait to be added/removed until the update cycle has completed. 
     * Adding/removing objects during an update tick with cause an exception.
     */
    public void updateList() {
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
    }
    
    /**
     * Add a new GameObject to be updated/rendered.
     * @param object - The new GameObject.
     */
    public void add(GameObject object) {
        object.alive = true;
        object.objectManager = this;
        waitList.add(object);
    }
    
    /**
     * Remove a GameObject from the update/render list.
     * @param object - GameObjkect to be removed.
     */
    public void remove(GameObject object) {
        object.alive = false;
        waitList.add(object);
    }
    
    /**
     * Cleanup all resources. Needs to be called before program is closed.
     */
    public void dispose() {
        updater = waitList.iterator();
        GameObject object;
        
        while(updater.hasNext()) {
            object = updater.next();
            object.alive = false;
            object.dispose();
        }
    }
    /**
     * 
     * @param object
     * @param object2
     * @return Returns true if object is colliding with object2.
     */
    public boolean isColliding(GameObject object, GameObject object2)
    {
        return Intersector.overlapConvexPolygons(object.getBounds(), object2.getBounds());
    }
    
    /**
     * Sets the current player.
     * @param player
     * @return
     */
    public void setPlayer(Player player)
    {
        this.player = player;
    }
    
    /* Who wrote this??
     public Player setPlayer(Player player)
    {
        return this.player = player;
    }
     */
}
