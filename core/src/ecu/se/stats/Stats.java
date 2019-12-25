package ecu.se.stats;

import ecu.se.Logger;

public enum Stats {
	//TODO: Add nice presentation text for all stats
	
	// Base Player Stats
	BASE_CONSTITUION			(true, 1f, new Stats[]{}),
	BASE_PHYSICAL_RESISTANCE	(true, 1f, new Stats[]{}),
	BASE_MAGICAL_RESISTANCE		(true, 1f, new Stats[]{}),
	BASE_STRENGTH				(true, 1f, new Stats[]{}),
	BASE_DEXTERITY				(true, 1f, new Stats[]{}),
	BASE_INTELLIGENCE			(true, 1f, new Stats[]{}),
	BASE_SPEED					(true, 1f, new Stats[]{}),
//	LUCK					(true, 1f, new Stats[]{}),
	
	HEALTH					(false, 1f, new Stats[]{BASE_CONSTITUION}),
	HEALTH_REGEN			(false, 0.01f, new Stats[]{BASE_CONSTITUION}),
	MANA					(false, 1f, new Stats[]{BASE_INTELLIGENCE}),
	MANA_REGEN				(false, 0.1f, new Stats[]{BASE_INTELLIGENCE}),
	MOVEMENT_SPEED			(false, 1f, new Stats[]{BASE_SPEED}),
	MOVEMENT_DRAG			(false, 1f, new Stats[]{BASE_SPEED}),
	MOVEMENT_ACCELERATION	(false, 1f, new Stats[]{BASE_SPEED}),
	SIZE					(false, 1f, new Stats[]{}),
	
	// Physical Ranged
	RANGED_WEAPON_ATTACK	(false, 1f, new Stats[]{BASE_DEXTERITY}),
	RANGED_WEAPON_RANGE		(false, 1f, new Stats[]{BASE_DEXTERITY}),
	RANGED_WEAPON_SPEED		(false, 1f, new Stats[]{BASE_DEXTERITY}),
	RANGED_ATTACK_SPEED		(false, 1f, new Stats[]{BASE_DEXTERITY}),
	PROJECTILE_SPEED		(false, 1f, new Stats[]{BASE_DEXTERITY}),
	
	// Physical Melee
	MELEE_WEAPON_ATTACK		(false, 1f, new Stats[]{BASE_STRENGTH}),
	MELEE_WEAPON_RANGE		(false, 1f, new Stats[]{BASE_STRENGTH}),
	MELEE_WEAPON_SPEED		(false, 1f, new Stats[]{BASE_STRENGTH}),
	MELEE_ATTACK_SPEED		(false, 1f, new Stats[]{BASE_STRENGTH}),
	PIERCING_ATTACK			(false, 1f, new Stats[]{BASE_STRENGTH}),
	PIERCING_DEFENSE		(false, 1f, new Stats[]{BASE_STRENGTH}),
	BLUNT_ATTACK			(false, 1f, new Stats[]{BASE_STRENGTH}),
	BLUNT_DEFENSE			(false, 1f, new Stats[]{BASE_STRENGTH}),
	
	KNOCKBACK				(false, 1f, new Stats[]{BASE_STRENGTH}),

	// Magic
	MAGIC_ATTACK			(false, 1f, new Stats[]{BASE_INTELLIGENCE}),
	MANA_COST				(false, 0.01f, new Stats[]{BASE_INTELLIGENCE}),
	SPELL_CAST_SPEED		(false, 0.01f, new Stats[]{BASE_INTELLIGENCE}),
	SPELL_COOLDOWN			(false, 0.01f, new Stats[]{BASE_INTELLIGENCE}),
	
	// Resistances
	POISON_RESISTANCE		(false, 1f, new Stats[]{BASE_PHYSICAL_RESISTANCE}),
	RANGED_WEAPON_RESISTANCE(false, 1f, new Stats[]{BASE_PHYSICAL_RESISTANCE}),
	MELEE_WEAPON_RESISTANCE	(false, 1f, new Stats[]{BASE_PHYSICAL_RESISTANCE}),
	FIRE_RESISTANCE			(false, 1f, new Stats[]{BASE_MAGICAL_RESISTANCE}),
	COLD_RESISTANCE			(false, 1f, new Stats[]{BASE_MAGICAL_RESISTANCE}),
	SHOCK_RESISTANCE		(false, 1f, new Stats[]{BASE_MAGICAL_RESISTANCE}),
		
	// MISC
	XP_GAIN					(false, 0.1f, new Stats[]{}),
	RARITY_CHANCE			(false, 0.1f, new Stats[]{}),
	;

	public boolean upgradeable;
	public float multiplier;
	public Stats[] grouping;
	private Stats(boolean upgradeable, float multiplier, Stats[] group) {
		this.upgradeable = upgradeable;
		this.multiplier = multiplier;
		grouping = group;
	}
	
	public static void print(float[] stats) {
		for(int i = 0; i < Stats.values().length; i++) {
			Logger.Debug("NA", "NA",Stats.values()[i].name() + ": " + stats[i]);
		}
    }
}