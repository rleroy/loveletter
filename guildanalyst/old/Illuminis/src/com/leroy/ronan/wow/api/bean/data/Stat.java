package com.leroy.ronan.wow.api.bean.data;

public enum Stat {
	health,
	powerType(false),
	power,
	str,
	agi,
	sta,
	intel("int"),
	spr,
	attackPower,
	rangedAttackPower,
	pvpResilienceBonus,
	mastery,
	masteryRating,
	crit,
	critRating,
	hitPercent,
	hitRating,
	haste,
	hasteRating,
	hasteRatingPercent,
	expertiseRating,
	spellPower,
	spellPen,
	spellCrit,
	spellCritRating,
	spellHitPercent,
	spellHitRating,
	mana5,
	mana5Combat,
	spellHaste,
	spellHasteRating,
	spellHasteRatingPercent,
	armor,
	dodge,
	dodgeRating,
	parry,
	parryRating,
	block,
	blockRating,
	pvpResilience,
	pvpResilienceRating,
	mainHandDmgMin,
	mainHandDmgMax,
	mainHandSpeed,
	mainHandDps,
	mainHandExpertise,
	offHandDmgMin,
	offHandDmgMax,
	offHandSpeed,
	offHandDps,
	offHandExpertise,
	rangedDmgMin,
	rangedDmgMax,
	rangedSpeed,
	rangedDps,
	rangedExpertise,
	rangedCrit,
	rangedCritRating,
	rangedHitPercent,
	rangedHitRating,
	rangedHaste,
	rangedHasteRating,
	rangedHasteRatingPercent,
	pvpPower,
	pvpPowerRating,
	pvpPowerDamage,
	pvpPowerHealing,
	;
	private String name;
	private boolean number;
	private Stat(){
		this.name = this.name();
		this.number = true;
	}
	private Stat(boolean number){
		this();
		this.number = number;
	}
	private Stat(String name){
		this();
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public boolean isNumber(){
		return this.number;
	}
}
