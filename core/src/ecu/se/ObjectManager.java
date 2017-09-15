package ecu.se;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ObjectManager {

    private ArrayList<GameObject> objects;
    private Iterator<GameObject> updater;
    private ArrayList<GameObject> waitList;
    
    public ObjectManager() {
        objects = new ArrayList<GameObject>();
        waitList = new ArrayList<GameObject>();
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
                objects.add(object);
            } else {
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
}
