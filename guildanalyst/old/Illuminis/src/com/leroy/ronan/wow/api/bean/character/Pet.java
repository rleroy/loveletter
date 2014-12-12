package com.leroy.ronan.wow.api.bean.character;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.leroy.ronan.wow.api.bean.Specie;

public class Pet {
	
	private static final Logger logger = Logger.getLogger(Pet.class);

	private JSONObject data;
	private Specie specie;
	
	public Pet(JSONObject cur) {
		this.data = cur;
		try{
			if (getSpeciesId() > 0){
				this.specie = new Specie(getSpeciesId());
			}
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
	public String getName(){
		return (String)data.get("name");
	}
	public Long getSpellId(){
		return (Long)data.get("spellId");
	}
	public Long getCreatureId(){
		return (Long)data.get("creatureId");
	}
	public Long getItemId(){
		return (Long)data.get("itemId");
	}
	public Long getQualityId(){
		return (Long)data.get("qualityId");
	}
	public String getIcon(){
		return (String)data.get("icon");
	}
	public String getBattlePetGuid(){
		return (String)data.get("battlePetGuid");
	}
	public Boolean isFavorite(){
		return (Boolean)data.get("isFavorite");
	}
	public Boolean isFirstAbilitySlotSelected(){
		return (Boolean)data.get("isFirstAbilitySlotSelected");
	}
	public Boolean isSecondAbilitySlotSelected(){
		return (Boolean)data.get("isSecondAbilitySlotSelected");
	}
	public Boolean isThirdAbilitySlotSelected(){
		return (Boolean)data.get("isThirdAbilitySlotSelected");
	}
	public String getCreatureName(){
		return (String)data.get("creatureName");
	}
	public Boolean getCanBattle(){
		return (Boolean)data.get("canBattle");
	}
	
	public Long getSpeciesId(){
		return (Long)((JSONObject)data.get("stats")).get("speciesId");
	}
	public Long getBreedId(){
		return (Long)((JSONObject)data.get("stats")).get("breedId");
	}
	public String getBreed(){
		String res = "?/?";
		switch(Integer.valueOf(String.valueOf(getBreedId()))){
			case 4:
			case 14:
				res = "P/P";
				break;
			case 5:
			case 15:
				res = "S/S";
				break;
			case 6:
			case 16:
				res = "H/H";
				break;
			case 7:
			case 17:
				res = "H/P";
				break;
			case 8:
			case 18:
				res = "P/S";
				break;
			case 9:
			case 19:
				res = "H/S";
				break;
			case 10:
			case 20:
				res = "P/B";
				break;
			case 11:
			case 21:
				res = "S/B";
				break;
			case 12:
			case 22:
				res = "H/B";
				break;
			case 3:
			case 13:
				res = "B/B";
				break;
		}
		return res;
	}
	public Long getPetQualityId(){
		return (Long)((JSONObject)data.get("stats")).get("petQualityId");
	}
	public Long getLevel(){
		return (Long)((JSONObject)data.get("stats")).get("level");
	}
	public Long getHealth(){
		return (Long)((JSONObject)data.get("stats")).get("health");
	}
	public Long getPower(){
		return (Long)((JSONObject)data.get("stats")).get("power");
	}
	public Long getSpeed(){
		return (Long)((JSONObject)data.get("stats")).get("speed");
	}
	public Specie getSpecie(){
		return this.specie;
	}

/*
	[
	 	{
	 		"name":"Clockwork Gnome",
	 		"spellId":90523,
	 		"creatureId":48609,
	 		"itemId":64372,
	 		"qualityId":3,
	 		"icon":"inv_misc_head_clockworkgnome_01",
	 		"stats":
	 			{
	 				"speciesId":277,
	 				"breedId":11,
	 				"petQualityId":3,
	 				"level":25,
	 				"health":1546,
	 				"power":257,
	 				"speed":289
	 			},
	 		"battlePetGuid":"00000000004210F4",
	 		"isFavorite":true,
	 		"isFirstAbilitySlotSelected":false,
	 		"isSecondAbilitySlotSelected":false,
	 		"isThirdAbilitySlotSelected":false,
	 		"creatureName":"Clockwork Gnome",
	 		"canBattle":true
	 	},
	 	{
	 		"name":"Frigid Frostling",
	 		"spellId":74932,
	 		"creatureId":40198,
	 		"itemId":53641,
	 		"qualityId":3,
	 		"icon":"spell_frost_frozencore",
	 		"stats":
	 			{
	 				"speciesId":253,
	 				"breedId":3,
	 				"petQualityId":3,
	 				"level":25,
	 				"health":1481,
	 				"power":276,
	 				"speed":276
	 			},
	 		"battlePetGuid":"00000000004210ED",
	 		"isFavorite":true,
	 		"isFirstAbilitySlotSelected":false,
	 		"isSecondAbilitySlotSelected":true,
	 		"isThirdAbilitySlotSelected":true,
	 		"creatureName":"Frigid Frostling",
	 		"canBattle":true
	 	},
	 ...
	]
*/
}
