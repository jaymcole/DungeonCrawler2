package stats;

public enum Stats {
	//TODO: Add nice presentation text for all stats
	
	// Base Player Stats
	CONSTITUION				(true, 1f, new Stats[]{}),
	PHYSICAL_RESISTANCE		(true, 1f, new Stats[]{}),
	MAGICAL_RESISTANCE		(true, 1f, new Stats[]{}),
	STRENGTH				(true, 1f, new Stats[]{}),
	DEXTERITY				(true, 1f, new Stats[]{}),
	INTELLIGENCE			(true, 1f, new Stats[]{}),
	SPEED					(true, 1f, new Stats[]{}),
//	LUCK					(true, 1f, new Stats[]{}),
	
	HEALTH					(false, 1f, new Stats[]{CONSTITUION}),
	HEALTH_REGEN			(false, 0.01f, new Stats[]{CONSTITUION}),
	MANA					(false, 1f, new Stats[]{INTELLIGENCE}),
	MANA_REGEN				(false, 0.01f, new Stats[]{INTELLIGENCE}),
	MOVEMENT_SPEED			(false, 1f, new Stats[]{SPEED}),
	MOVEMENT_DRAG			(false, 1f, new Stats[]{SPEED}),
	MOVEMENT_ACCELERATION	(false, 1f, new Stats[]{SPEED}),
	SIZE					(true, 1f, new Stats[]{}),
	
	// Physical Ranged
	RANGED_WEAPON_ATTACK	(false, 1f, new Stats[]{DEXTERITY}),
	RANGED_WEAPON_RANGE		(false, 1f, new Stats[]{DEXTERITY}),
	RANGED_WEAPON_SPEED		(false, 1f, new Stats[]{DEXTERITY}),
	RANGED_ATTACK_SPEED		(false, 1f, new Stats[]{DEXTERITY}),
	PROJECTILE_SPEED		(false, 1f, new Stats[]{DEXTERITY}),
	
	// Physical Melee
	MELEE_WEAPON_ATTACK		(false, 1f, new Stats[]{STRENGTH}),
	MELEE_WEAPON_RANGE		(false, 1f, new Stats[]{STRENGTH}),
	MELEE_WEAPON_SPEED		(false, 1f, new Stats[]{STRENGTH}),
	MELEE_ATTACK_SPEED		(false, 1f, new Stats[]{STRENGTH}),
	PIERCING_ATTACK			(false, 1f, new Stats[]{STRENGTH}),
	PIERCING_DEFENSE		(false, 1f, new Stats[]{STRENGTH}),
	BLUNT_ATTACK			(false, 1f, new Stats[]{STRENGTH}),
	BLUNT_DEFENSE			(false, 1f, new Stats[]{STRENGTH}),
	
	KNOCKBACK				(false, 1f, new Stats[]{STRENGTH}),

	// Magic
	MAGIC_ATTACK			(false, 1f, new Stats[]{INTELLIGENCE}),
	MAGIC_DEFENSE			(false, 1f, new Stats[]{INTELLIGENCE}),
	MANA_COST				(false, 0.01f, new Stats[]{INTELLIGENCE}),
	SPELL_CAST_SPEED		(false, 0.01f, new Stats[]{INTELLIGENCE}),
	SPELL_COOLDOWN			(false, 0.01f, new Stats[]{INTELLIGENCE}),
	
	// Resistances
	POISON_RESISTANCE		(false, 1f, new Stats[]{PHYSICAL_RESISTANCE}),
	MANA_RESISTANCE			(false, 1f, new Stats[]{PHYSICAL_RESISTANCE}),
	RANGED_WEAPON_RESISTANCE(false, 1f, new Stats[]{PHYSICAL_RESISTANCE}),
	MELEE_WEAPON_RESISTANCE	(false, 1f, new Stats[]{PHYSICAL_RESISTANCE}),
	FIRE_RESISTANCE			(false, 1f, new Stats[]{MAGICAL_RESISTANCE}),
	COLD_RESISTANCE			(false, 1f, new Stats[]{MAGICAL_RESISTANCE}),
	SHOCK_RESISTANCE		(false, 1f, new Stats[]{MAGICAL_RESISTANCE}),
		
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
			System.out.println(Stats.values()[i].name() + ": " + stats[i]);
		}
    }
}