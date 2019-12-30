package ecu.se.objects;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.GameObject;
import ecu.se.Lighting;
import ecu.se.ObjectManager;
import ecu.se.Utils;
import ecu.se.actors.Actor;
import ecu.se.actors.Team;
import ecu.se.assetManager.Animation;
import ecu.se.assetManager.AssetManager;
import ecu.se.assetManager.SoundManager;
import ecu.se.assetManager.SoundManagerTask;
import ecu.se.assetManager.SoundTask;
import ecu.se.assetManager.SpriteAsset;
import ecu.se.lights.Light;
import ecu.se.map.Map;

public class Projectile extends GameObject {

	private Actor parent;
	private static float lifespan = 10; //in seconds
	private float timeAlive;
	private Animation animation;
	private Light light;
	private double angle;
	private float moveX, moveY;
	private float damage;
	
	private Sound startSound;
	private long startSoundID;
	private Sound movingSound;
	private long movingSoundID;
	private Sound endSound;
	private long endSoundID;
	
	
	private GameObject ItemToSpawnOnDeath;
	private Light LightToSpawnOnDeath;
	
	//TODO: Check collision using a line from current position to next.
	
//	public Projectile(float x, float y, double angleRAD, Actor parent, float damage, float speed, String file, Light light) {
//		super(x, y);
//		this.x = x;
//		this.y = y;
//		this.parent = parent;
//		this.team = parent.team;
//		angle = angleRAD;
//		bounds = Utils.getRectangleBounds(x, y, 10, 10, Utils.ALIGN_CENTERED);
//		this.damage = damage;
//		animation = new Animation(0, 0, 0, AssetManager.getSpriteSheet(file));
//		animation.setRow(0);
//		animation.setIdle(false);
//		animation.setRotation((float)Math.toDegrees(angle));
//		timeAlive = 0;
//		
//		
//		this.light = light;
//		Lighting.addLight(light);
//		
////		light = new Light(this.getPosition());
////		light.setColor(Color.ORANGE);
////		
////		light.setIntensity(500);
////		light.setParent(this);
////		light.type = 2;
////		Lighting.addLight(light);
//		setSpeed(speed);
//	}
	
	public Projectile(float x, float y, double angleRAD, Actor parent, float damage, float speed, SpriteAsset spritesheet, Sound startSound, Sound movingSound, Sound endSound, Light light, GameObject objectToSpawnOnDeath, Light LightToSpawnOnDeath) {
		super(x, y);
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.team = parent.team;
		angle = angleRAD;
		bounds = Utils.getRectangleBounds(x, y, 10, 10, Utils.ALIGN_CENTERED);
		this.damage = damage;
		animation = new Animation(0, 0, 0, spritesheet);
		animation.setRow(0);
		animation.setIdle(false);
		animation.setRotation((float)Math.toDegrees(angle));
		timeAlive = 0;
		
		this.light = light;
		light.setParent(this);
		if (light != null)
			Lighting.addLight(light);
		
		setSpeed(speed);		
		this.startSound = startSound;
		startSoundID = startSound.play();
		SoundManager.addTask(new SoundManagerTask(startSound, startSoundID, 1.5f, 0.0f, SoundTask.LEAVE));
		this.movingSound = movingSound;
		movingSoundID = movingSound.loop();
		this.endSound = endSound;
		
		
		this.ItemToSpawnOnDeath = objectToSpawnOnDeath;
		this.LightToSpawnOnDeath = LightToSpawnOnDeath;
		
	}
	
	@Override 
	public void onCollision(GameObject otherObject) {
		if (!isAlive()) {
			return;
		}
		if (otherObject != this && !Team.isFriendly(team, otherObject.team)) {
			otherObject.defend(parent, null, damage);
			this.kill();
		}
	}

	@Override
	public void update(float deltaTime) {
		this.x += moveX * deltaTime;
		this.y += moveY * deltaTime;
		bounds.setPosition(x, y);
		animation.update(deltaTime);
		animation.setXY((int) x, (int) y);
				
		if(Map.getTile((int)x, (int)y) == null || Map.getTile((int)x, (int)y).isWall) {
			kill();
		}
		
		timeAlive += deltaTime;
		if (timeAlive > lifespan) {
			kill();
		}
	}
	
	public void setSpeed(float speed) {
		moveX += Math.cos(angle) * speed;
		moveY += Math.sin(angle) * speed;
	}
	
	@Override
	protected void die() {
		if (ItemToSpawnOnDeath != null) {
			ItemToSpawnOnDeath.setPosition(new Vector2(x, y));
			ItemToSpawnOnDeath.setPosition((int)x, (int)y);
			ObjectManager.add(ItemToSpawnOnDeath);
		}
		
		if (LightToSpawnOnDeath != null) {
			LightToSpawnOnDeath.setPosition(new Vector3(this.x, this.y, 0));
			Lighting.addLight(LightToSpawnOnDeath);
			
		}
		
		if (endSound != null) {			
			endSoundID = endSound.play();
			SoundManager.addTask(new SoundManagerTask(endSound, endSoundID, 0.5f, 1.0f, SoundTask.STOP));
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		animation.render(batch);

	}

	@Override
	public void dispose() {
		startSound.stop(startSoundID);
		movingSound.stop(movingSoundID);
		if (light != null)
			Lighting.removeLight(light);
	}
}
