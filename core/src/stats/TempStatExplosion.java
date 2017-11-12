package stats;

import actors.Actor;

public class TempStatExplosion extends TempStatModifier{
	private static final float explosion_decay = 0.5f;
	private float pushX, pushY;
	
	public TempStatExplosion(Stats stat, float value, Actor parent, float pushX, float pushY) {
		super(stat, value, parent);
		this.pushX = pushX;
		this.pushY = pushY;
	}

	@Override
	public void update(float deltaTime) {
		value *= explosion_decay;
		parent.push(value * pushX * deltaTime, value * pushY * deltaTime);
		
		if (value < 1) 
			parent.removeTempStat(this);
	}
}
