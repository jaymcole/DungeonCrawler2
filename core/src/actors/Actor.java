package actors;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ecu.se.GameObject;
import ecu.se.Utilities;
import ecu.se.map.Direction;
import ecu.se.map.Map;

public abstract class Actor extends GameObject{

    protected String name;
    //for the different types of enemies
    //protected Creature creature;
    protected int HP;
    protected int attack;
    protected int defense;
    protected Vector2 currentSpeed;
    protected float topSpeed;
    protected float acceleration;
    protected float drag;
    protected Texture texture;
    protected Map map;
    protected float oldx = 0;
    protected float oldy = 0;
    
    public Actor(float x, float y, float z, Map map) {
        super(x, y, z);    
        this.map = map;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    //Health
    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }
    
    //Attack
    public int getattack() {
        return attack;
    }

    public void setattack(int attack) {
        this.attack = attack;
    }

    //Defense
    public int getdefense() {
        return defense;
    }

    public void setdefense(int defense) {
        this.defense = defense;
    }
    public void move(float deltaTime, Direction direction)
    {
        currentSpeed.x += (acceleration * deltaTime) * direction.x;
        currentSpeed.x = Utilities.clamp(-topSpeed, topSpeed, currentSpeed.x);        
        currentSpeed.y += (acceleration * deltaTime) * direction.y;
        currentSpeed.y = Utilities.clamp(-topSpeed, topSpeed, currentSpeed.y);
    }
    public void revert()
    {
    	x = oldx;
    	y = oldy;
    }
    
    
}
