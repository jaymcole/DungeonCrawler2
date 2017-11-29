package actions;

import actors.Actor;

public class Potion extends Action{
	public static final int POTION_HEALTH = 0;
	public static final int POTION_MANA = 1;
	
	
	
	private int potionType;
	private int uses;
	private int value;

	public Potion(Actor caster, int potionType) {
		super(caster);
		this.potionType = potionType;
		uses = 1;
		value = 50;
	}

	@Override
	public void act(int x, int y) {
		if (uses > 0) {
			uses--;
			if (potionType == POTION_HEALTH) {
				caster.setHealth(value);
			} else if (potionType == POTION_MANA){
				caster.setMana(value);
			}
			
			if (uses <= 0)
				this.delete = true;
		} else {
			this.delete = true;
		}
	}

	@Override
	public void update(float deltaTime) {
//		if (uses <= 0)
//			this.delete = true;
		
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getPotionType() {
		return potionType;
	}

}
