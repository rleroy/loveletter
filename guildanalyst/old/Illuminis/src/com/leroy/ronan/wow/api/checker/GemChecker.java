package com.leroy.ronan.wow.api.checker;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.leroy.ronan.wow.api.bean.Character;
import com.leroy.ronan.wow.api.bean.Item;

public class GemChecker extends CharacterChecker{
	
	private static final Logger logger = Logger.getLogger(GemChecker.class);

	public GemChecker(Character character) {
		super(character);
	}

	public Map<String, String> check(){
		Map<String, String> messages = new HashMap<String, String>();
		
		for (Item.TYPE cur : Item.TYPE.values()){
			Item item = getCharacter().getItem(cur);
			if (item != null){
				if (cur == Item.TYPE.waist){
					String message = checkExtraSocket(getCharacter().getItem(cur));
					if (!"OK".equals(message)){
						messages.put(cur.name()+ ".socket", message);
					}
				}
				
				String message = checkGemming(getCharacter().getItem(cur));
				if (!"OK".equals(message)){
					messages.put(cur.name()+ ".gemming", message);
				}
			}
		}
		
		return messages;
	}
	
	private String checkGemming(Item item){
		String res = "OK";
		for (String cur : getGems(item)){
			if (cur.contains("EMPTY")){
				res = "MISSING";
				break;
			}
		}
		return res;
	}
	
}
