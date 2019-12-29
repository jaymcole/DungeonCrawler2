package ecu.se.actions;

import java.util.Random;

import ecu.se.ObjectManager;
import ecu.se.actors.Actor;
import ecu.se.assetManager.AssetManager;
import ecu.se.objects.Projectile;
import ecu.se.stats.Stats;

public class Spell_FlameThrower extends Spell {
	public Spell_FlameThrower(Actor caster) {
		super(caster);
		
		baseCastSpeed 	= 0;
		baseCooldown 	= 0.1f;
		baseManaCost 	= 0.2f;
		baseDamage 		= 2f;
		instantCast = true;
		
		sound_cast = "sounds/effects/fire/fire_throw_03.mp3";
		sound_loop = "sounds/effects/fire/fire_burning_01.mp3";
		sound_end = "sounds/effects/explosion/explosion_01.mp3";
		texture_path = "texture/spritesheet/fireball_spritesheet.png";
		
	}
	Random random = new Random();

	@Override
	protected void cast(float deltaTime) {
		double angleInRadians = Math.atan2(targetY - caster.getY(), targetX - caster.getX()) - Math.atan2(0, 0)  + random.nextDouble() + (random.nextDouble() * 0.1f) - 0.55f;
		ObjectManager.add(new Projectile(caster.getX(), caster.getY(), angleInRadians,  caster, caster.getStat(Stats.KNOCKBACK) * 25, baseDamage, 700f, texture_path, 
				AssetManager.getSound(sound_cast).getSound(), 
				AssetManager.getSound(sound_loop).getSound(), 
				AssetManager.getSound(sound_end).getSound()));
		currentStage++;
	}
}
