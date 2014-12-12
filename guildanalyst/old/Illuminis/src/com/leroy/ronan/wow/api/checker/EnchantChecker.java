package com.leroy.ronan.wow.api.checker;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.leroy.ronan.wow.api.bean.Character;
import com.leroy.ronan.wow.api.bean.Item;

public class EnchantChecker extends CharacterChecker{
	
	private static final Logger logger = Logger.getLogger(EnchantChecker.class);

	public EnchantChecker(Character character) {
		super(character);
	}

	public Map<String, String> check(){
		Map<String, String> messages = new HashMap<String, String>();
		
		for (Item.TYPE cur : Item.TYPE.values()){
			Item item = getCharacter().getItem(cur);
			if (item != null){
				if (cur.isEnchantable()){
					String message = checkEnchanting(getCharacter().getItem(cur));
					if (!"OK".equals(message)){
						messages.put(cur.name()+ ".enchant", message);
					}
				}
			}
		}
		
		return messages;
	}

}
