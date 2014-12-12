package com.leroy.ronan.wow.api.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leroy.ronan.wow.api.bean.Character;
import com.leroy.ronan.wow.api.bean.Item;
import com.leroy.ronan.wow.api.bean.character.Profession;

public class ProfessionChecker extends CharacterChecker{

	public ProfessionChecker(Character character) {
		super(character);
	}
	
	public Map<String, String> check(){
		Map<String, String> messages = new HashMap<String, String>();

		messages.putAll(check(0));
		messages.putAll(check(1));
		
		return messages;
	}

	public Map<String, String> check(int i) {
		Profession prof = getCharacter().getPrimaryProfession(i);
		Map<String, String> messages = new HashMap<String, String>();
		if (prof == null){
			messages.put("prof", "EMPTY");
		}else{
			if (   Profession.TYPE.Herbalism.name().equals(prof.getName())
				|| Profession.TYPE.Skinning.name().equals(prof.getName())
				|| Profession.TYPE.Mining.name().equals(prof.getName())
					){
				String message = checkGathering(prof);
				if (!"OK".equals(message)){
					messages.put("prof."+prof.getName(), message);
				}
			}else if (Profession.TYPE.Alchemy.name().equals(prof.getName())){
				// Connaitre toutes les flasques de MoP
				for (long cur : ALCHEMY_FLASKS){
					if (!prof.getRecipes().contains(cur)){
						messages.put("prof."+prof.getName(), "MISSING");
						break;
					}
				}
			}else if (Profession.TYPE.Blacksmithing.name().equals(prof.getName())){
				// Extra socket sur bracer et gloves
				String message;
				message = checkExtraSocket(getCharacter().getItem(Item.TYPE.wrist));
				if (!"OK".equals(message)){
					messages.put("wrist.socket", message);
				}
				message = checkExtraSocket(getCharacter().getItem(Item.TYPE.hands));
				if (!"OK".equals(message)){
					messages.put("hands.socket", message);
				}
			}else if (Profession.TYPE.Enchanting.name().equals(prof.getName())){
				// Enchantements d'anneau.
				String message;
				message = checkEnchanting(getCharacter().getItem(Item.TYPE.finger1));
				if (!"OK".equals(message)){
					messages.put("finger1.enchant", message);
				}
				message = checkEnchanting(getCharacter().getItem(Item.TYPE.finger2));
				if (!"OK".equals(message)){
					messages.put("finger2.enchant", message);
				}
			}else if (Profession.TYPE.Engineering.name().equals(prof.getName())){
				// Truc sur gants et ceinture
				String message;
				message = checkTinker(getCharacter().getItem(Item.TYPE.hands));
				if (!"OK".equals(message)){
					messages.put("hands.tinker", message);
				}
				message = checkTinker(getCharacter().getItem(Item.TYPE.waist));
				if (!"OK".equals(message)){
					messages.put("waist.tinker", message);
				}
			}else if (Profession.TYPE.Inscription.name().equals(prof.getName())){
				// Enchant epau spéciaux
				String message = checkEnchanting(getCharacter().getItem(Item.TYPE.shoulder), INSCRIPTION_SPECIAL);
				if (!"OK".equals(message)){
					messages.put("shoulder.enchant", message);
				}
			}else if (Profession.TYPE.Jewelcrafting.name().equals(prof.getName())){
				// 2 gemmes speciales
				String message = checkJewelcrafting();
				if (!"OK".equals(message)){
					messages.put("prof."+prof.getName(), message);
				}
			}else if (Profession.TYPE.Leatherworking.name().equals(prof.getName())){
				// Enchant bracelets spéciaux
				String message = checkEnchanting(getCharacter().getItem(Item.TYPE.wrist), LEATHERWORKING_SPECIAL);
				if (!"OK".equals(message)){
					messages.put("wrist.enchant", message);
				}
			}else if (Profession.TYPE.Tailoring.name().equals(prof.getName())){
				// Enchant cape spécial
				String message = checkEnchanting(getCharacter().getItem(Item.TYPE.back), TAILORING_SPECIAL);
				if (!"OK".equals(message)){
					messages.put("back.enchant", message);
				}
			}else{
				messages.put("prof."+prof.getName(), "NOTSUPPORTED");
			}
		}
		return messages;
	}

	private static final long[] TAILORING_SPECIAL = {4892, 4893, 4894};
	private static final long[] LEATHERWORKING_SPECIAL = {4875, 4877, 4878, 4879};
	private static final long[] INSCRIPTION_SPECIAL = {4913, 4914, 4915, 4916}; 
	private static final long[] ALCHEMY_FLASKS = {114769, 114770, 114771, 114772, 114773};
	private static final long[] JEWELCRAFTING_SPECIAL = {83141, 83142, 83143, 83144, 83145, 83146, 83147, 83148, 83149, 83150, 83151, 83152};

	private String checkGathering(Profession prof){
		String res = "OK";
		if (prof.getRank() < 600){
			res = "NOTMAX";
		}
		return res;
	}
	
	private String checkTinker(Item item){
		return checkOption("tinker", item, null);
	}
	
	protected List<String> getGems(){
		List<String> list = new ArrayList<String>();
		for (Item.TYPE curType : Item.TYPE.values()){
			Item curItem = getCharacter().getItem(curType);
			list.addAll(getGems(curItem));
		}
		return list;
	}
	
	private String checkJewelcrafting(){
		int nbSpecial = 0;
		for (String cur : getGems()){
			String[] curTab = cur.split("-");
			try{
				long idGem = Long.valueOf(curTab[1]);
				if (Arrays.binarySearch(JEWELCRAFTING_SPECIAL, idGem) >= 0){
					nbSpecial++;
				}
			}catch(Exception e){
				// RAS
			}
			
		}
		
		String res = "MISSING";
		if (nbSpecial >= 2){
			res = "OK";
		}
		return res;
	}
}
