package actions;

import actors.Actor;

/**
 * 
 * Potion - does *something* to caster.
 * Example: Health potions restore health to the caster.
 */
public class Potion extends Action{
	public static final int POTION_HEALTH = 0;
	public static final int POTION_MANA = 1;
	
	/**
	 * The type of potion.
	 */
	private int potionType;
	
	/**
	 * The number of times this potion can be used before being depleted.
	 */
	private int uses;
	
	/**
	 * The amount this potion should provide.
	 */
	private int value;

	
	public Potion(Actor caster, int potionType) {
		super(caster);
		this.potionType = potionType;
		uses = 1;
		value = 50;
	}

	@Override
	public void update(float deltaTime) {}

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

	/**
	 * Sets this.value to value.
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @return this potions type.
	 */
	public int getPotionType() {
		return potionType;
	}


}
