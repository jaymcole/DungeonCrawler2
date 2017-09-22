package ecu.se;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import actors.Actor;

public class ObjectManager {

    private ArrayList<GameObject> objects;
    private Iterator<GameObject> updater;
    private ArrayList<GameObject> waitList;
    private ArrayList<GameObject> actors;

    
    public ObjectManager() {
        objects = new ArrayList<GameObject>();
        waitList = new ArrayList<GameObject>();
        actors = new ArrayList<GameObject>();
    }
    
    public void update(float deltaTime) {
        updater = objects.iterator();
        GameObject object;
        
        while(updater.hasNext()) 
        {
            object = updater.next();
            if (object.alive) {
                object.update(deltaTime);
            } else {
                this.remove(object);
            }
        }
        
        updater = actors.iterator();
        while(updater.hasNext())
        {
        	object = updater.next();
            if (object.alive) {
                object.update(deltaTime);
            } else {
                this.remove(object);
            }
        }
        
        for(int i = 0; i < actors.size(); i++)
        {
        	for(int j = i + 1; j < actors.size(); j++)
        	{
        		if(isColliding(actors.get(i), actors.get(j)))
        		{
        			((Actor) actors.get(i)).revert();
        			((Actor) actors.get(j)).revert();
        		}
        	}
        }
        	
        updateList();
    }
    
    public void render(float deltaTime, SpriteBatch batch) {
        updater = objects.iterator();
        GameObject object;
        
        while(updater.hasNext()) {
            object = updater.next();
            if (object.alive) {
                object.render(deltaTime, batch);
            }
        }
    }
    
    public void updateList() {
        updater = waitList.iterator();
        GameObject object;
        
        while(updater.hasNext()) {
            object = updater.next();
            if(object.alive) {
            	if(object instanceof Actor)
            		actors.add(object);
            	else
            		objects.add(object);
            } else {
            	if(object instanceof Actor)
            		actors.remove(object);
            	else
            		objects.remove(object);
            }
        }
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
    
    public boolean isColliding(GameObject object, GameObject otherObject)
    {
    	return object.getBounds().getBoundingRectangle().overlaps(otherObject.getBounds().getBoundingRectangle());
    }
}
