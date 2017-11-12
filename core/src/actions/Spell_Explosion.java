package actions;

import actors.Actor;
import ecu.se.ObjectManager;
import ecu.se.objects.Explosion;

public class Spell_Explosion extends Spell{

	private float force = 500;
	
	public Spell_Explosion(Actor caster) {
		super(caster);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void cast(float deltaTime) {
		ObjectManager.add(new Explosion(targetX, targetY, force, 0, caster));
		currentStage++;
		
	}

}
