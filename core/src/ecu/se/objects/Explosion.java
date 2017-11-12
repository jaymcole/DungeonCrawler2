package ecu.se.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import actors.Actor;
import actors.Team;
import ecu.se.GameObject;
import ecu.se.ObjectManager;
import ecu.se.map.Direction;
import stats.Stats;
import stats.TempStatExplosion;

public class Explosion extends GameObject{
	
	protected float force;
	protected float damage;
	protected Actor caster;

	public Explosion(float x, float y, float force, float damage, Actor caster) {
		super(x, y);
		this.team = caster.team;
		this.force = force;
		this.damage = damage;
		this.caster = caster;
	}

	@Override
	public void update(float deltaTime) {
		float distance;
		float tempForce;
		float angle;
		for(GameObject a : ObjectManager.getActors()) {
			distance = Vector2.dst(this.getPositionV2().x, this.getPositionV2().y, a.getPositionV2().x, a.getPositionV2().y);
			if (distance < 1)
				distance = 1f;
			
			if (distance <= force * 2) {
				tempForce = 10 + (force / distance);
				angle = Direction.angleRad(this.getPositionV2(), a.getPositionV2());
				if (!Team.isFriendly(((Actor)a).team, this.team) )
				((Actor)a).addTempStat(new TempStatExplosion(Stats.MOVEMENT_SPEED, force, (Actor)a, (float)(Math.cos(angle) * tempForce), (float)(Math.sin(angle) * tempForce)));
			}
		}
		this.kill();
	}

	@Override
	public void render(SpriteBatch batch) {
	}

	@Override
	public void dispose() {		
	}

}
