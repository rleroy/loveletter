package com.leroy.ronan.wow.api.checker;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.leroy.ronan.wow.api.bean.Character;
import com.leroy.ronan.wow.api.bean.data.LegendaryQuest;

public class LegendaryChecker extends CharacterChecker{

	private static final Logger logger = Logger.getLogger(LegendaryChecker.class);

	public LegendaryChecker(Character character) {
		super(character);
	}

	public Map<String, String> check(){
		Map<String, String> messages = new HashMap<String, String>();
		
		for (LEGENDARY_LINE line: LEGENDARY_LINE.values()){
			messages.putAll(check(line));
		}
	
		return messages;
	}

	public Map<String, String> check(LEGENDARY_LINE line) {
		Map<String, String> res = new HashMap<String, String>();
		if (getCharacter().getQuests().contains(line.end.getId())){
			res.put(line.name(), "UNLOCKED : "+line.end.getDesc());
		}else{
			for (LegendaryQuest cur : line.path){
				if (cur.getId() != line.end.getId()){
					if (getCharacter().getQuests().contains(cur.getId())){
						res.put(line.name()+"."+cur.getId(), "DONE : "+cur.getDesc());
					}else{
						res.put(line.name()+"."+cur.getId(), "TODO : "+cur.getDesc());
					}
				}
			}
		}
		return res;
	}
	
	private static LegendaryQuest[] part1;
	private static LegendaryQuest[] part2;
	private static LegendaryQuest[] part3;
	private static LegendaryQuest[] part4;

	static {
		try{
			part1 = new LegendaryQuest[4];
			part1[0] = new LegendaryQuest(31468L, "The Black Prince (Honored)");
			part1[1] = new LegendaryQuest(31473L, "Sigil of Power (10) + Sigil of Wisdom (10)");
			part1[2] = new LegendaryQuest(31481L, "Chimera of Fear (Sha of Fear)");
			part1[3] = new LegendaryQuest(31482L, "Crystallized Gem");
				
			part2 = new LegendaryQuest[5];
			part2[0] = new LegendaryQuest(32476L, "3000 Valor Earned");
			part2[1] = new LegendaryQuest(32429L, "The Black Prince (Revered)");
			part2[2] = new LegendaryQuest(32431L, "Win a Battle at the Temple of Kotmogu + Win a Battle in the Silvershard Mines");
			part2[3] = new LegendaryQuest(32430L, "High Marshal Twinbraid slain");
			part2[4] = new LegendaryQuest(32432L, "Eye of the Black Prince");
				
			part3 = new LegendaryQuest[5];
			part3[0] = new LegendaryQuest(32591L, "Trillium Bar (40) + Secrets of the Empire (20)");
			part3[1] = new LegendaryQuest(32592L, "The Black Prince (Exalted)");
			part3[2] = new LegendaryQuest(32593L, "Unlock the Thunder Forge + Complete Wrathion's Task (Sha of the Thunder Forge)");
			part3[3] = new LegendaryQuest(32594L, "Tempered Lightning Lance (Nalak)");
			part3[4] = new LegendaryQuest(32595L, "Primal Diamond");
				
			part4 = new LegendaryQuest[4];
			part4[0] = new LegendaryQuest(32596L, "Titan Runestone (12)");
			part4[1] = new LegendaryQuest(32597L, "Heart of the Thunder King");
			part4[2] = new LegendaryQuest(32805L, "Complete one of the Celestial's Challenges");
			part4[3] = new LegendaryQuest(32861L, "Cloak of Virtue");
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
	public enum LEGENDARY_LINE{
		line1(part1[3], part1),
		line2(part2[4], part2),
		line3(part3[4], part3),
		line4(part4[3], part4),
		;
		private LegendaryQuest end;
		private LegendaryQuest[] path;
		private LEGENDARY_LINE(LegendaryQuest end, LegendaryQuest[] path){
			this.end = end;
			this.path = path;
		}
		public LegendaryQuest getEnd() {
			return end;
		}
		public LegendaryQuest[] getPath() {
			return path;
		}
	}
	

}
