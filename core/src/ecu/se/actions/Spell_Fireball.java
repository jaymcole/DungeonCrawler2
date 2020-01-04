package ecu.se.actions;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.Lighting;
import ecu.se.ObjectManager;
import ecu.se.actors.Actor;
import ecu.se.assetManager.AssetManager;
import ecu.se.lights.Light;
import ecu.se.lights.LightFading;
import ecu.se.objects.Explosion;
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
		baseCooldown 	= 15.0f;
		baseManaCost 	= 5f;
		baseDamage 		= 25f;
		
		sound_cast = AssetManager.getSound("sounds/effects/fire/fire_throw_03.mp3").getSound();
		sound_loop = AssetManager.getSound("sounds/effects/fire/fire_burning_01.mp3").getSound();
		sound_end = AssetManager.getSound("sounds/effects/explosion/explosion_01.mp3").getSound();
		spritesheet = AssetManager.getSpriteSheet("texture/spritesheet/fireball_spritesheet.png");
		
			
	}

	
	@Override
	protected void cast(float deltaTime) {
		double angleInRadians = Math.atan2(targetY - caster.getY(), targetX - caster.getX()) - Math.atan2(0, 0);

		Light light = new Light(new Vector3(targetX, targetY, 0));
		light.setColor(Color.ORANGE);
		
		light.setIntensity(500);
		light.setParent(caster);
		light.type = 2;
		
		ObjectManager.add(
				new Projectile(
						caster.getX(), 
						caster.getY(), 
						angleInRadians,  
						caster, 
						baseDamage, 
						700f, 
						spritesheet, 
						sound_cast, 
						sound_loop, 
						sound_end, 
						light,
						new Explosion(0, 0, caster.getStat(Stats.KNOCKBACK) * 100, 100, caster),
						new LightFading(Vector3.Zero, Color.ORANGE, light.intensity * 40, 0.85f, 2)
						)
				);
		currentStage++;
	}

}
