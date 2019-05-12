package ecu.se.actions;

import ecu.se.ObjectManager;
import ecu.se.actors.Actor;
import ecu.se.assetManager.AssetManager;
import ecu.se.objects.Projectile;
import ecu.se.stats.Stats;

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
		
		sound_cast = "sounds/effects/fire/fire_throw_03.mp3";
		sound_loop = "sounds/effects/fire/fire_burning_01.mp3";
		sound_end = "sounds/effects/explosion/explosion_01.mp3";
		texture_path = "texture/spritesheet/fireball_spritesheet.png";
		
	}

	@Override
	protected void cast(float deltaTime) {
		double angleInRadians = Math.atan2(targetY - caster.getY(), targetX - caster.getX()) - Math.atan2(0, 0);
		ObjectManager.add(new Projectile(caster.getX(), caster.getY(), angleInRadians,  caster, caster.getStat(Stats.KNOCKBACK) * 100, baseDamage, 700f, texture_path, 
				AssetManager.getSound(sound_cast).getSound(), 
				AssetManager.getSound(sound_loop).getSound(), 
				AssetManager.getSound(sound_end).getSound()));
		currentStage++;
	}

}
