package ecu.se.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.GameObject;
import ecu.se.ObjectManager;
import ecu.se.Utils;
import ecu.se.actors.Actor;
import ecu.se.actors.Team;
import ecu.se.assetManager.Animation;
import ecu.se.assetManager.AssetManager;
import ecu.se.map.Direction;
import ecu.se.stats.Stats;
import ecu.se.stats.TempStatExplosion;

public class Explosion extends GameObject {

	protected float force;
	protected float damage;
	protected Actor caster;
	protected Animation animation;

	public Explosion(float x, float y, float force, float damage, Actor caster) {
		super(x, y);
		this.team = caster.team;
		this.force = force;
		this.damage = damage;
		this.caster = caster;
		
		animation = new Animation(x, y, 0, AssetManager.getSpriteSheet("texture/spritesheet/explosion.png"));
		animation.setPosition((int)x, (int)y);
		animation.setXY((int) x, (int) y);

		animation.setRow(0);
		animation.setIdle(false);
		animation.setRotation(Utils.getRandomInt(360));
		animation.setRow(0);
		animation.setScale(1, 1);
		shockwave();
	}

	@Override
	public void update(float deltaTime) {
		if (animation.getCurrentColumn() >= animation.getTotalColumns() - 1) {
			this.kill();
		}
		animation.setXY((int)x, (int)y);
		animation.update(deltaTime);
	}
	
	private void shockwave() {
		float distance;
		float tempForce;
		float angle;
		for (GameObject a : ObjectManager.getActors()) {
			distance = Vector2.dst(this.getPositionV2().x, this.getPositionV2().y, a.getPositionV2().x,
					a.getPositionV2().y);
			if (distance < 1)
				distance = 1f;

			if (distance <= force * 2) {
				tempForce = 10 + (force / distance);
				angle = Direction.angleRad(this.getPositionV2(), a.getPositionV2());
				if (!Team.isFriendly(((Actor) a).team, this.team)) {
					((Actor) a).addTempStat(new TempStatExplosion(Stats.MOVEMENT_SPEED, force, (Actor) a,
							(float) (Math.cos(angle) * tempForce), (float) (Math.sin(angle) * tempForce)));
					((Actor)a).defend(caster, Stats.BLUNT_DEFENSE, tempForce);
				}
			}
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		animation.render(batch);
	}

	@Override
	public void dispose() {
	}

}
