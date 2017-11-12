package actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import actors.Actor;
import ecu.se.Lighting;
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
//		light = new FadingLight(caster.getPosition());
//		light.setColor(Color.BLUE);
//		light.setIntensity(500);
//		light.setParent(caster);
//		Lighting.addLight(light);
		//TODO: Make a teleport animation
		//TODO: Check that teleport location is valid (on walkable tile)
		
		Lighting.addLight(new FadingLight(caster.getPosition(), Color.SKY, 5000, 0.6f) );
		Lighting.addLight(new FadingLight(new Vector3(targetX, targetY, 0), Color.BLUE, 5000, 0.6f) );
		
		caster.setPosition((int)targetX, (int)targetY);
		currentStage++;
	}

}
