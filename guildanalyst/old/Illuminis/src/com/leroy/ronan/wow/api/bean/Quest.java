package com.leroy.ronan.wow.api.bean;

import com.leroy.ronan.wow.api.bean.data.LegendaryQuest;


public class Quest extends BattleNetBean{
/*
		// 5.0
		31488, // Stranger in a Strange Land
		31489, // Stranger in a Strange Land (rogue)
		31454, // A Legend in the Making
		31468, // Trial of the Black Prince (Honoré Black Prince)
		31473, // The Strength of One's Foes (10 sigil of power + 10 sigil of wisdom)
		31481, // Fear Itself (Tuer le sha)
		31482, // Breath of the Black Prince (Reward : Gemme +500 sur arme).
		// 5.1
		31483, // Incoming...
		32108, // Domination Point (horde)
		32427, // The Measure of a Leader (horde)
		32476, // A Test of Valor (horde) (3000 Valor points)
		32429, // The Prince's Pursuit (horde) (Revered Black Prince)
		32431, // Glory to the Horde (horde) (Gagner les 2 nouveaux BG)
		32430, // A Change of Command (horde) (Tuer le boss ally)
		32432, // The Soul of the Horde (horde) (Reward : Socket sur arme)
		// 5.2
		32457, // The Thunder King
		32590, // Meet Me Upstairs
		32591, // Secrets of the First Empire (
		32593, // The Thunder Forge (Fight avec wrathion ?)
		32277, // To the Skies! (horde)
		32594, // Spirit of the Storm Lord (Nalak)
		32279, // The Fall of Shan Bu (horde)
		32595, // The Crown of Heaven (Reward : Meta gemme)
		32596, // Echoes of the Titans (12 Titan runestone)
		32597, // Heart of the Thunder King (Tuer le thunder king)
		// 5.3
		32805, // Celestial Blessings (Challenge)
		32861, // Cloak of Virtue  (Reward : Cape)
		32870, // Preparing to Strike 
*/
	
	public Quest(Long id) throws Exception{
		super("quest/"+id, "");
	}

	public Long getId(){
		return (Long)get("id");
	}
	public Long getReqLevel(){
		return (Long)get("reqLevel");
	}
	public Long getSuggestedPartyMembers(){
		return (Long)get("suggestedPartyMembers");
	}
	public Long getLevel(){
		return (Long)get("level");
	}
	public String getTitle(){
		return (String)get("title");
	}
	public String geCategory(){
		return (String)get("category");
	}

	@Override
	protected void load() {
		// TODO Auto-generated method stub
		
	}
}
