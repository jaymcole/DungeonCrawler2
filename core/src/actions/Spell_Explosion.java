package actions;

import actors.Actor;
import ecu.se.ObjectManager;
import ecu.se.objects.Explosion;

/**
 * 
 * A Spell tha creates and explosion.
 */
public class Spell_Explosion extends Spell{
	
	/**
	 * The force of the explosion.
	 */
	private float force = 500;
	
	public Spell_Explosion(Actor caster) {
		super(caster);
	}

	@Override
	protected void cast(float deltaTime) {
		ObjectManager.add(new Explosion(targetX, targetY, force, 0, caster));
		currentStage++;
		
	}

}
