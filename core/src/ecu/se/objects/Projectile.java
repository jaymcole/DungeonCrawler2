package ecu.se.objects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import actors.Actor;
import actors.Team;
import assetManager.Animation;
import assetManager.AssetManager;
import ecu.se.GameObject;
import ecu.se.Lighting;
import ecu.se.ObjectManager;
import ecu.se.Utils;
import ecu.se.map.Map;

public class Projectile extends GameObject {

	private Actor parent;
	private float knockback;
	private static float lifespan = 10; //in seconds
	private float timeAlive;
	private Animation animation;
	private Light light;
	private double angle;
	private float moveX, moveY;
	private float damage;
	//TODO: Check collision using a line from current position to next.
	
	public Projectile(float x, float y, double angleRAD, Actor parent, float knockback, float damage, float speed, String file) {
		super(x, y);
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.team = parent.team;
		this.knockback = knockback;
		angle = angleRAD;
		bounds = Utils.getRectangleBounds(x, y, 10, 10, Utils.ALIGN_CENTERED);
		this.damage = damage;
		animation = new Animation(0, 0, 0, AssetManager.getSpriteSheet(file));
		animation.setRow(0);
		animation.setIdle(false);
		animation.setRotation((float)Math.toDegrees(angle));
		timeAlive = 0;
		light = new Light(this.getPosition());
		light.setColor(Color.ORANGE);
		
		light.setIntensity(500);
		light.setParent(this);
		Lighting.addLight(light);
		setSpeed(speed);
	}
	
	@Override 
	public void collision(GameObject otherObject) {
		if (!alive) {
			return;
		}
		
		if (otherObject != this && !Team.isFriendly(team, otherObject.team)) {
			otherObject.defend(null, damage);
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
	
	protected void kill() {
		ObjectManager.add(new Explosion(this.x, this.y, knockback, 0, parent));
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
