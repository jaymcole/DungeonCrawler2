package ecu.se.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import ecu.se.Lighting;
import ecu.se.actors.Actor;
import ecu.se.map.Map;
import ecu.se.objects.FadingLight;

/**
 * A Spell that teleports the caster to a new location.
 */
public class Spell_Teleport extends Spell{
	
	public Spell_Teleport(Actor caster) {
		super(caster);
		baseCastSpeed 	= 0;
		baseCooldown 	= 1;
		baseManaCost 	= 0.1f;
		
//		sound_cast = "sounds/effects/fire/fire_throw_03.mp3";
//		sound_loop = "sounds/effects/fire/fire_burning_01.mp3";
//		sound_end = "sounds/effects/explosion/explosion_01.mp3";
		texture_path = "texture/spritesheet/starburst.png";
		
	}

	@Override
	protected void cast(float deltaTime) {
		//TODO: Make a teleport animation
		if (!Map.getTile((int)targetX, (int)targetY).isWall) {
			Lighting.addLight(new FadingLight(caster.getPosition(), Color.SKY, 5000, 0.8f, 2) );
			Lighting.addLight(new FadingLight(new Vector3(targetX, targetY, 0), Color.BLUE, 5000, 0.8f, 2) );	
			
			caster.setPosition((int)targetX, (int)targetY);
		}
		currentStage++;
	}

}
