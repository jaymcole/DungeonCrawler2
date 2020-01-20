package ecu.se.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import ecu.se.ObjectManager;
import ecu.se.actors.Actor;
import ecu.se.assetManager.AssetManager;
import ecu.se.lights.Light;
import ecu.se.lights.LightFading;
import ecu.se.lights.LightTimed;
import ecu.se.objects.Projectile;

public class Spell_Light extends Spell{

	public Spell_Light(Actor caster) {
		super(caster);
		baseCastSpeed 	= 0;
		baseCooldown 	= 15;
		baseManaCost 	= 0.1f;
		
		sound_cast = AssetManager.getSound("sounds/effects/fire/fire_throw_03.mp3").getSound();
		sound_loop = AssetManager.getSound("sounds/effects/fire/fire_burning_01.mp3").getSound();
		sound_end = AssetManager.getSound("sounds/effects/explosion/explosion_01.mp3").getSound();
		spritesheet = AssetManager.getSpriteSheet("texture/spritesheet/bleh.png");
	}

	@Override
	protected void cast(float deltaTime) {
		
		double angleInRadians = Math.atan2(targetY - caster.getY(), targetX - caster.getX()) - Math.atan2(0, 0);

		Light light = new Light(new Vector3(targetX, targetY, 0));
		light.setColor(Color.LIME);		
		light.setIntensity(9000);
		light.setParent(caster);
		light.type = 2;
		
		ObjectManager.add(
				new Projectile(
						caster.getX(), 
						caster.getY(), 
						angleInRadians,  
						caster, 
						0, 
						250f, 
						spritesheet, 
						sound_cast, 
						sound_loop, 
						sound_end, 
						light,
						null,
						new LightTimed(Vector3.Zero, light.getColor(), light.intensity, 10, 2, 
								new LightFading(Vector3.Zero, light.getColor(), light.intensity, 0.99f, 2)
								)
						)
				);

		currentStage++;
	}

}
