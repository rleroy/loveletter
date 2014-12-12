package com.leroy.ronan.wow.api.bean;


public class Specie extends BattleNetBean{
	
	public Specie(Long id) throws Exception{
		super("battlePet/species/"+id, "");
		
/*
{
    "abilities": [
        {
            "cooldown": 0, 
            "icon": "spell_shadow_plaguecloud", 
            "id": 640, 
            "isPassive": false, 
            "name": "Toxic Smoke", 
            "order": 1, 
            "petTypeId": 9, 
            "requiredLevel": 2, 
            "rounds": 1, 
            "showHints": false, 
            "slot": 1
        }, 
        {
            "cooldown": 0, 
            "icon": "ability_racial_rocketbarrage", 
            "id": 777, 
            "isPassive": false, 
            "name": "Missile", 
            "order": 0, 
            "petTypeId": 9, 
            "requiredLevel": 1, 
            "rounds": 1, 
            "showHints": false, 
            "slot": 0
        }, 
        {
            "cooldown": 0, 
            "icon": "inv_mace_02", 
            "id": 455, 
            "isPassive": false, 
            "name": "Batter", 
            "order": 3, 
            "petTypeId": 9, 
            "requiredLevel": 10, 
            "rounds": 1, 
            "showHints": false, 
            "slot": 0
        }, 
        {
            "cooldown": 0, 
            "icon": "inv_misc_pyriumgrenade", 
            "id": 636, 
            "isPassive": false, 
            "name": "Sticky Grenade", 
            "order": 2, 
            "petTypeId": 9, 
            "requiredLevel": 4, 
            "rounds": 1, 
            "showHints": false, 
            "slot": 2
        }, 
        {
            "cooldown": 0, 
            "icon": "ability_mount_rocketmount", 
            "id": 293, 
            "isPassive": false, 
            "name": "Launch Rocket", 
            "order": 5, 
            "petTypeId": 9, 
            "requiredLevel": 20, 
            "rounds": 1, 
            "showHints": false, 
            "slot": 2
        }, 
        {
            "cooldown": 0, 
            "icon": "inv_misc_bomb_09", 
            "id": 634, 
            "isPassive": false, 
            "name": "Minefield", 
            "order": 4, 
            "petTypeId": 9, 
            "requiredLevel": 15, 
            "rounds": 1, 
            "showHints": false, 
            "slot": 1
        }
    ], 
    "canBattle": true, 
    "creatureId": 42078, 
    "description": "Powerful artillery of the terran army. The Thor is always the first one in and the last one out!", 
    "icon": "t_roboticon", 
    "petTypeId": 9, 
    "source": "Promotion: Starcraft 2: Wings of Liberty Collector's Edition", 
    "speciesId": 258
}*/
		
	}

	@Override
	protected void load() {
		// TODO : Load abilities
	}

	public Boolean getCanBattle(){
		return (Boolean)get("canBattle");
	}
	public Long getCreatureId(){
		return (Long)get("creatureId");
	}
	public String getDescription(){
		return (String)get("description");
	}
	public String getIcon(){
		return (String)get("icon");
	}
	public Long getPetTypeId(){
		return (Long)get("petTypeId");
	}
	public String getSource(){
		return (String)get("source");
	}
	public Long getSpeciesId(){
		return (Long)get("speciesId");
	}
}
