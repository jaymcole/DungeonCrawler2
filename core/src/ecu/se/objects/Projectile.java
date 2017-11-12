package ecu.se.objects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import actors.Team;
import assetManager.Animation;
import assetManager.AssetManager;
import ecu.se.GameObject;
import ecu.se.Lighting;
import ecu.se.ObjectManager;
import ecu.se.Utils;
import ecu.se.map.Direction;
import ecu.se.map.Map;

public class Projectile extends GameObject {

	private GameObject parent;
	private static float lifespan = 10; //in seconds
	private float timeAlive;
	private Animation animation;
	private Light light;
	private double angle;
	private float moveX, moveY;
	
	//TODO: Check collision using a line from current position to next.
	
	public Projectile(float x, float y, double angleRAD, GameObject parent) {
		super(x, y);
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.team = parent.team;
		angle = angleRAD;
		bounds = Utils.getRectangleBounds(x, y, 10, 10, Utils.ALIGN_CENTERED);
		
		animation = new Animation(0, 0, 0, AssetManager.getSpriteSheet("texture/spritesheet/fireball_spritesheet.png"));
		animation.rowSelect(0);
		animation.setIdle(false);
		animation.setRotation((float)Math.toDegrees(angle));
		timeAlive = 0;
		light = new Light(this.getPosition());
		light.setColor(Color.ORANGE);
		
		light.setIntensity(500);
		light.setParent(this);
		Lighting.addLight(light);
		setSpeed(700.0f);
	}
	
	@Override 
	public void collision(GameObject otherObject) {
		if (!alive) {
			return;
		}
		
		if (otherObject != this && !Team.isFriendly(team, otherObject.team)) {
			otherObject.defend(null, 25);
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
		
		if(Map.currentTile((int)x, (int)y) == null) {
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
	
	protected void kill() {
//		Lighting.addLight(new FadingLight(this.getPosition(), new Color(0.0f, 1.0f, 0.0f, 1.0f), light.intensity * 15, 0.7f) );
		Lighting.addLight(new FadingLight(this.getPosition(), Color.CHARTREUSE, light.intensity * 20, 0.7f) );
		this.alive = false;
		ObjectManager.remove(this);
		dispose();
	}

	@Override
	public void render(SpriteBatch batch) {
		animation.render(batch);

	}

	@Override
	public void dispose() {
		Lighting.removeLight(light);
		light = null;
	}
}
