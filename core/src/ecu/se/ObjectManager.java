package ecu.se;

import java.util.ArrayList;
import java.util.Iterator;

import javax.rmi.CORBA.Util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Box2D;

import actors.Actor;
import actors.Player;

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
    
    private int sizeOfActors = 0;
    private int counter;
    
    public ObjectManager() {
        objects = new ArrayList<GameObject>();
        waitList = new ArrayList<GameObject>();
        actors = new ArrayList<GameObject>();
    }
    
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
                Utils.println(this, "Player is colliding with Object");
                player.revert();
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
                
                if(isColliding(actor1, actor2))
                {
                    actor1.revert();
                    actor2.revert();
                }

            }
        }
        updateList();
    }
    
    
    public void render(float deltaTime, SpriteBatch batch) {
        updater = objects.iterator();        
        while(updater.hasNext()) {
            object = updater.next();
            if (object.alive) {
                object.render(deltaTime, batch);
            }
        }
        
        updater = actors.iterator();
        while(updater.hasNext()) {
            actor1 = (Actor) updater.next();
            if (actor1.alive) {
                actor1.render(deltaTime, batch);
            }
        }
        
    }
    
    public void updateList() {
        updater = waitList.iterator();
        GameObject object;
        
        while(updater.hasNext()) {
            object = updater.next();
            if(object.alive) {
            	if(object instanceof Actor) {
            		actors.add(object);
            		sizeOfActors++;
            	} else
            		objects.add(object);
            } else {
            	if(object instanceof Actor) {
            		actors.remove(object);
            		sizeOfActors--;
            	} else
            		objects.remove(object);
            }
        }
        waitList.clear();
    }
    
    public void add(GameObject object) {
        object.alive = true;
        object.objectManager = this;
        waitList.add(object);
    }
    
    
    public void remove(GameObject object) {
        object.alive = false;
        waitList.add(object);
    }
    
    
    public void dispose() {
        updater = waitList.iterator();
        GameObject object;
        
        while(updater.hasNext()) {
            object = updater.next();
            object.alive = false;
            object.dispose();
        }
    }
    
    public boolean isColliding(GameObject object, GameObject object2)
    {
        return Intersector.overlapConvexPolygons(object.getBounds(), object2.getBounds());
    }
    
    public Player setPlayer(Player player)
    {
        return this.player = player;
    }
}
