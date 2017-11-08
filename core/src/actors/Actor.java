package actors;


import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import assetManager.Animation;
import assetManager.AssetManager;
import assetManager.SpriteAsset;
import ecu.se.GameObject;
import ecu.se.ObjectManager;
import ecu.se.Utils;
import ecu.se.map.Direction;
import ecu.se.map.Map;

public abstract class Actor extends GameObject{
	
    protected String name;
    protected String spriteSheet;
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
    protected boolean awake;
    
    
    /**
     * baseStats: All permanent stats.
     * i.e. Allocated skill points from leveling up
     */
    protected float[] baseStats  = new float[Stats.values().length];
    
    /**
     * modifierStats: Stats the change but do not require regular updates.
     * i.e. Stat changes from equipped items.
     */
    protected float[] modifierStats  = new float[Stats.values().length];
    
    /**
     * currentStats: The actual stats the game pulls from.
     */
    protected float[] currentStats  = new float[Stats.values().length];
    
    /**
     * tempStatModifiers: Stats requiring regular updates.
     * i.e. A poison debuff needs to damage the player every tic 
     * 			and likely has a limited life span that needs to be
     * 			checked/updated every tic.
     */
    protected LinkedList<TempStatModifier> tempStatModifiers;
    protected Vector2 currentSpeed;
    protected float currentHealth;
    
    
    private Texture debugHealthBarTexture;

    
    public Actor(float x, float y, float z, Map map, String spriteSheet) {
        super(x, y, z);    
        this.map = map;
        SpriteAsset asset = AssetManager.getSpriteSheet(spriteSheet);
        animation = new Animation(0,0,0,asset);        
        spriteWidth = asset.getSpriteWidth();
        spriteHeight = asset.getSpriteHeight();      
        textureRegion = asset.getTexture().getTextureRegion();
        texture = asset.getTexture().getTexture();
        
        debugHealthBarTexture = AssetManager.getTexture("texture/misc/white.png").getTexture();
        
        
        bounds = Utils.getRectangleBounds(x, y, (int)(spriteWidth*0.5), spriteHeight, Utils.ALIGN_BOTTOM_CENTER);
        tempStatModifiers = new LinkedList<TempStatModifier>();
        setDefaults();
        updateStats();
        
        currentHealth = currentStats[Stats.HEALTH.ordinal()];
        Random random = new Random();
        currentHealth = random.nextInt(100);
        this.awake = false;
    }
    
    /**
     * update: Handles low level functionality of Actors.
     * 			Can be overridden if you're making a special Actor
     * 				Ex. An Actor that teleports would need to override this function to replace the movement
     * 					parts with specialized code.
     * @param deltaTime: Time between each frame.
     */
    public void update(float deltaTime) {
    	act(deltaTime);
    	textureRegion.setRegion(0, 0, spriteWidth, spriteHeight);
        x += currentSpeed.x;
        y += currentSpeed.y;
        currentSpeed.x *= currentStats[Stats.MOVEMENT_DRAG.ordinal()]  *deltaTime;
        currentSpeed.y *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] *deltaTime;        
        bounds.setPosition(x,y);
        animation.setIdle(idle);
        animation.update(deltaTime);
        animation.setXY((int) x,(int) y);
        
        for(TempStatModifier stat : tempStatModifiers) {
        	stat.update(deltaTime);
        }
        
        if(currentHealth <= 0) {
        	this.kill();
        }
        
        idle = true;
    }
    
    
    
    /**
     * How and Actor needs to behave. Overhead (i.e. movement, animation, etc updates) are already handled by the Actor.java.
     * @param deltaTime: Time between each frame.
     */
    public abstract void act(float deltaTime);
    
    private static final int borderWidth = 1;
    private static final int barHeight = 2;
    
	@Override
	public void render(SpriteBatch batch) {
		 animation.render(batch);
		 
		 // Renders a healthbar
		 batch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
		 batch.draw(debugHealthBarTexture, x - (int)(spriteWidth * 0.5f) - borderWidth, y + spriteHeight - borderWidth, spriteWidth + borderWidth*2, barHeight*2 + borderWidth*2);
		 batch.setColor(1.0f, 0f, 0f, 0.5f);
		 batch.draw(debugHealthBarTexture, x - (int)(spriteWidth * 0.5f), y + spriteHeight , spriteWidth * (currentHealth / (currentStats[Stats.HEALTH.ordinal()]+0.0f)), barHeight*2);
		 
		 
		 batch.setColor(Color.WHITE);
	}
	
	public float defend(Stats type, float damage) {
    	currentHealth -= damage;
    	return damage;
    }
    
    /**
     * 
     * @return the name of the Actor.
     */
    // TODO: Added name generator for Actors
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this Actor.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * 
     * @return the current health of this Actor.
     */
    public float getHealth() {
        return currentHealth;
    }
    
    public int getHealthRounded() {
    	return (int) currentHealth;
    }

    /**
     * Sets the HP of this Actor
     * 	Caps the max based on the Actors Health stat.
     * @param currentHealth
     */
    public void setHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    //Defense
    public float getStat(Stats stat) {
        return currentStats[stat.ordinal()];
    }
    
    public void setIdle(boolean idle) {
    	this.idle = idle;
    }
    
    public void move(float deltaTime, Direction direction, boolean updateDirection)
    {
        currentSpeed.x += (currentStats[Stats.MOVEMENT_ACCELERATION.ordinal()] * deltaTime) * direction.x;
        currentSpeed.x = Utils.clamp(-currentStats[Stats.MOVEMENT_SPEED.ordinal()], currentStats[Stats.MOVEMENT_SPEED.ordinal()], currentSpeed.x);        
        currentSpeed.y += (currentStats[Stats.MOVEMENT_ACCELERATION.ordinal()] * deltaTime) * direction.y;
        currentSpeed.y = Utils.clamp(-currentStats[Stats.MOVEMENT_SPEED.ordinal()], currentStats[Stats.MOVEMENT_SPEED.ordinal()], currentSpeed.y);
        if(updateDirection)
        	animation.rowSelect (Direction.valueOf(direction.name()).ordinal());
        setIdle(false);
    }
    
    
    //TODO: Add different defaults for different classes (Could be done by subclasses)
    public void setDefaults() {
    	for(int i = 0; i < Stats.values().length; i++) {
    		baseStats[i] = 1;
    		modifierStats[i] = 0;
    	}
    	
    	baseStats[Stats.HEALTH.ordinal()] = 100; 
    	baseStats[Stats.MOVEMENT_DRAG.ordinal()] = 0.3f;
    	baseStats[Stats.MOVEMENT_SPEED.ordinal()] = 50f;
    	baseStats[Stats.MOVEMENT_ACCELERATION.ordinal()] = 200f;
    }
    
    //TODO: Calculate stats based on equipped items / other modifiers
    public void updateStats()
    {
    	for(int i = 0; i < Stats.values().length; i++)
    	{
    		currentStats[i] = baseStats[i] + modifierStats[i];
    	}
    }
    
    public void removeTempStat(TempStatModifier stat) {
    	tempStatModifiers.remove(stat);
    }
    
    public void addTempStat(TempStatModifier stat) {
    	tempStatModifiers.add(stat);
    }
    

    
    //TODO: Place corpse texture (needs to fade eventually)
    //		Drop Loot.
    public void die() {
    	//Drop loot
    	// Set Boolean to dead
    	
    	// Change update() to do the following
    		// Subtract deltaTime from timer.
    		// Upon timer running out, add self to remove list in ObjectManager
    	
    	// Change render() to do the following
    		// Render corpse instead of alive actor.
    }    
    
    @Override
	public void dispose() {
    	
	}
}
