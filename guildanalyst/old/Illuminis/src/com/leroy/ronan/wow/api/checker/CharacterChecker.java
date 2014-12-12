package com.leroy.ronan.wow.api.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.leroy.ronan.wow.api.bean.Character;
import com.leroy.ronan.wow.api.bean.Item;

public abstract class CharacterChecker {

	private static final Logger logger = Logger.getLogger(CharacterChecker.class);

	private Character character;

	public CharacterChecker(Character character){
		this.character = character;
	}
	
	public Character getCharacter() {
		return character;
	}

	public abstract Map<String, String> check();

	protected List<String> getGems(Item curItem) {
		List<String> list = new ArrayList<String>();
		if (curItem != null){
			int i = 0;
			for (String cur : curItem.getSockets()){
				Object o = curItem.getTooltipParams().get("gem"+i);
				if (o == null){
					list.add(cur+"-EMPTY");
				}else{
					Long id = (Long)o;
					list.add(cur+"-"+id);
				}
				i++;
			}
			Object o = curItem.getTooltipParams().get("gem"+i);
			while (o != null){
				Long id = (Long)o;
				list.add("EXTRA-"+id);
				i++;
				o = curItem.getTooltipParams().get("gem"+i);
			}
		}
		return list;
	}
	
	protected String checkEnchanting(Item item){
		return checkEnchanting(item, null);
	}
	
	protected String checkEnchanting(Item item, long[] expected){
		return checkOption("enchant", item, expected);
	}
	
	protected String checkExtraSocket(Item item) {
		return checkOption("extraSocket", item, null);
	}
	
	protected String checkOption(String option, Item item, long[] expected){
		String res = "MISSING";
		if (item != null && item.getTooltipParams() != null){
			Object o = item.getTooltipParams().get(option);
			if (o != null){
				if (expected == null){
					res = "OK";
				}else{
					Long id = (Long)o;
					res = "BAD";
					if (Arrays.binarySearch(expected, id) >= 0){
						res = "OK";
					}
				}
			}
		}
		return res;
	}
	
}
