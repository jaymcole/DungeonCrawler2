package actions;

import actors.Actor;
import ecu.se.ObjectManager;
import ecu.se.objects.Projectile;
import stats.Stats;

/**
 * 
 * A spell that creates a fireball.
 */
public class Spell_Fireball extends Spell{

	public Spell_Fireball(Actor caster) {
		super(caster);
		
		baseCastSpeed 	= 0;
		baseCooldown 	= 0.01f;
		baseManaCost 	= 5f;
		baseDamage 		= 25f;
	}

	@Override
	protected void cast(float deltaTime) {
		double angleInRadians = Math.atan2(targetY - caster.getY(), targetX - caster.getX()) - Math.atan2(0, 0);
		ObjectManager.add(new Projectile(caster.getX(), caster.getY(), angleInRadians,  caster, caster.getStat(Stats.KNOCKBACK) * 100, baseDamage, 700f, "texture/spritesheet/fireball_spritesheet.png"));
		currentStage++;
	}

}
