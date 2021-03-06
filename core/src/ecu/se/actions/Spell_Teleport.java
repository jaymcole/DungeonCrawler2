package ecu.se.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import ecu.se.Lighting;
import ecu.se.ObjectManager;
import ecu.se.actors.Actor;
import ecu.se.assetManager.AssetManager;
import ecu.se.lights.LightFading;
import ecu.se.map.Map;
import ecu.se.objects.Effect;

/**
 * A Spell that teleports the caster to a new location.
 */
public class Spell_Teleport extends Spell{
	
	public Spell_Teleport(Actor caster) {
		super(caster);
		baseCastSpeed 	= 0;
		baseCooldown 	= 15;
		baseManaCost 	= 0.1f;
		
	}

	@Override
	protected void cast(float deltaTime) {
		if (!Map.getTile((int)targetX, (int)targetY).isWall) {
			Lighting.addLight(new LightFading(caster.getPosition(), Color.SKY, 5000, 0.97f, 2) );
			Lighting.addLight(new LightFading(new Vector3(targetX, targetY, 0), Color.BLUE, 5000, 0.97f, 2) );	
			ObjectManager.add(new Effect(caster.getPosition().x, caster.getPosition().y, AssetManager.getSpriteSheet("texture/spritesheet/starburst.png"), 1));
			ObjectManager.add(new Effect(targetX, targetY, AssetManager.getSpriteSheet("texture/spritesheet/starburst.png"), 1));
			caster.setPosition((int)targetX, (int)targetY);
		}
		currentStage++;
	}

}
