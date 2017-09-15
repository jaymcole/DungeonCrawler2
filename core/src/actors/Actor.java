package actors;

import ecu.se.GameObject;

public abstract class Actor extends GameObject{

    protected String name;
    //for the different types of enemies
    //protected Creature creature;
    protected int HP;
    protected int attack;
    protected int defense;
    
    
    public Actor(float x, float y, float z) {
        super(x, y, z);    
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
}
