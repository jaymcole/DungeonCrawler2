package stats;

public enum Stats {
	//TODO: Add nice presentation text for all stats
	
	// Base Player Stats
	STRENGTH,
	HEALTH,
	HEALTH_REGEN,
	MANA,
	MANA_REGEN,
	MOVEMENT_SPEED,
	MOVEMENT_DRAG,
	MOVEMENT_ACCELERATION,
	SIZE,
	
	// Physical Ranged
	RANGED_WEAPON_ATTACK,
	RANGED_WEAPON_RANGE,
	RANGED_WEAPON_SPEED,
	RANGED_ATTACK_SPEED,
	PROJECTILE_SPEED,
	
	// Physical Melee
	MELEE_WEAPON_ATTACK,
	MELEE_WEAPON_RANGE,
	MELEE_WEAPON_SPEED,
	MELEE_ATTACK_SPEED,
	PIERCING_ATTACK,
	PIERCING_DEFENSE,
	BLUNT_ATTACK,
	BLUNT_DEFENSE,
	
	KNOCKBACK,

	// Magic
	MAGIC_ATTACK,
	MAGIC_DEFENSE,
	MANA_COST,
	SPELL_CAST_SPEED,
	SPELL_COOLDOWN,
	
	// Resistances
	POISON_RESISTANCE,
	MANA_RESISTANCE,
	RANGED_WEAPON_RESISTANCE,
	MELEE_WEAPON_RESISTANCE,
	FIRE_RESISTANCE,
	COLD_RESISTANCE,
	SHOCK_RESISTANCE,
		
	// MISC
	XP_GAIN,
	RARITY_CHANCE,
	
	
	
	;

	Stats() {}
	
	public static void print(float[] stats) {
		for(int i = 0; i < Stats.values().length; i++) {
			System.out.println(Stats.values()[i].name() + ": " + stats[i]);
		}
    }
}