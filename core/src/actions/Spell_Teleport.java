package actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import actors.Actor;
import ecu.se.Lighting;
import ecu.se.map.Map;
import ecu.se.objects.FadingLight;
import ecu.se.objects.Light;

public class Spell_Teleport extends Spell{

	private FadingLight light;
	
	public Spell_Teleport(Actor caster) {
		super(caster);
		baseCastSpeed 	= 0;
		baseCooldown 	= 0;
		baseManaCost 	= 0.1f;
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
