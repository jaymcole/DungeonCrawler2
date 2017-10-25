package actors;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import assetManager.Animation;
import assetManager.AssetManager;
import assetManager.SpriteAsset;
import ecu.se.GameObject;
import ecu.se.Utils;
import ecu.se.map.Direction;
import ecu.se.map.Map;

public abstract class Actor extends GameObject{
	
    protected String name;
    protected String spriteSheet;
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
    protected Direction direction;
    
    protected Animation animation;
    protected int spriteWidth;// = 40;
    protected int spriteHeight;// = 48;
    protected int spriteSequences = 5;
    protected TextureRegion textureRegion;
    protected float[] baseStats  = new float[Stats.values().length];
    protected float[] modifierStats  = new float[Stats.values().length];
    protected float[] currentStats  = new float[Stats.values().length];
    
    public Actor(float x, float y, float z, Map map, String spriteSheet) {
        super(x, y, z);    
        this.map = map;
        SpriteAsset asset = AssetManager.getSpriteSheet(spriteSheet);
        animation = asset.getAnimation();
        spriteWidth = asset.getSpriteWidth();
        spriteHeight = asset.getSpriteHeight();      

        
        
        textureRegion = asset.getTexture().getTextureRegion();
        texture = asset.getTexture().getTexture();
        
        bounds = Utils.getRectangleBounds(x, y, (int)(spriteWidth*0.5), spriteHeight, Utils.ALIGN_BOTTOM_CENTER);
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
        currentSpeed.x = Utils.clamp(-topSpeed, topSpeed, currentSpeed.x);        
        currentSpeed.y += (acceleration * deltaTime) * direction.y;
        currentSpeed.y = Utils.clamp(-topSpeed, topSpeed, currentSpeed.y);
        animation.rowSelect (Direction.valueOf(direction.name()).ordinal());
        idle = false;

    }
    public void revert()
    {
    	x = oldx;
    	y = oldy;
    }
    
    public void setStats()
    {
    	for(int i = 0; i < Stats.values().length; i++)
    	{
    		baseStats[i] = 1;
    		modifierStats[i] = 0;
    		currentStats[i] = baseStats[i] + modifierStats[i];
    		
    	}
    }
    
    
}
