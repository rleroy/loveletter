package com.leroy.ronan.wow.api.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.leroy.ronan.wow.api.bean.data.EnchantUtils;
import com.leroy.ronan.wow.api.bean.data.Spell;
import com.leroy.ronan.wow.api.bean.data.Stat;

public class Item extends BattleNetBean{
	
	public static final long[] META_LEGENDARY = {95344, 95345, 95346, 95347};

	public enum TYPE{
		mainHand(true),
		offHand(true),
		head(false),
		neck(false),
		shoulder(true),
		back(true),
		chest(true),
		wrist(true),
		hands(true),
		waist(false),
		legs(true),
		feet(true),
		finger1(false),
		finger2(false),
		trinket1(false),
		trinket2(false),
		;
		
		private boolean enchantable;
		private TYPE(boolean enchantable){
			this.enchantable = enchantable;
		}
		public boolean isEnchantable() {
			return enchantable;
		}
	}
	
	private JSONObject tooltipParams;
	private JSONArray stats;
	
	public Item(Long id, JSONObject tooltipParams, JSONArray stats) throws Exception{
		super("item/"+id, "");
		this.tooltipParams = tooltipParams;
		this.stats = stats;
		
/*
"head":{
	"id":95846,
	"name":"Cover of the Haunted Forest",
	"icon":"inv_helm_leather_raiddruid_m_01",
	"quality":4,
	"itemLevel":502,
	"tooltipParams":{
		"gem0":95347,
		"gem1":76697,
		"set":[95846,95849,95248,95247,95245],
		"reforge":116,
		"transmogItem":32235,
		"upgrade":{
			"current":0,
			"total":2,
			"itemLevelIncrement":0
		}
	},
	"stats":[
		{"stat":5,"amount":1054},
		{"stat":36,"amount":700},
		{"stat":6,"amount":461,"reforgedAmount":-307},
		{"stat":7,"amount":1941},
		{"stat":32,"amount":307,"reforged":true}
	],"armor":2405
},
*/
		
	}

	@Override
	protected void load() {
		// Attention, je ne peux pas load tooltipParams ni stats
	}

	public Long getId(){
		return (Long)get("id");
	}
	public String getName(){
		return (String)get("name");
	}
	public String getIcon(){
		return (String)get("icon");
	}
	public String getQuality(){
		return (String)get("quality");
	}
	public Long getArmor(){
		return (Long)get("armor");
	}
	public Long getItemLevel(){
		return (Long)get("itemLevel");
	}
	public Long getUpgrade(){
		JSONObject upgrade  = (JSONObject)this.tooltipParams.get("upgrade");
		Long res = 0L;
		if (upgrade != null){
			res = (Long)upgrade.get("itemLevelIncrement");
		}
		return res;
	}
	public JSONObject getTooltipParams() {
		return tooltipParams;
	}
	public JSONArray getStats() {
		return stats;
	}
	public List<String> getSockets(){
		List<String> res = new ArrayList<String>();
		JSONObject socketInfo = (JSONObject)get("socketInfo");
		if (socketInfo != null){
			JSONArray sockets = (JSONArray)socketInfo.get("sockets");
			for (Object obj : sockets){
				JSONObject cur = (JSONObject)obj;
				String color = (String)cur.get("type");
				res.add(color);
			}
		}
		return res;
	}
	public List<String> getGems() throws Exception {
		List<String> res = new ArrayList<String>();
		int i = 0;
		Object o = getTooltipParams().get("gem"+i);
		while (o != null){
			Long id = (Long)o;
			Gem gem = new Gem(id);
			res.add(gem.getName());
			i++;
			o = getTooltipParams().get("gem"+i);
		}
		return res;
	}

	public String getEnchant() {
		String res = "-";
		try{
			long enchantId = (Long)getTooltipParams().get("enchant");
			Spell spell = new Spell(EnchantUtils.getSpellId((int)enchantId));
			res = spell.getName();
		}catch(Exception e){
			// RAS
		}
		return res;
	}

	private static String statIdToLabel(int statId){
		String res = statId+"?";
		switch (statId){
			case 3 : return "Agility";
			case 4 : return "Strength";
			case 5 : return "Intellect";
			case 6 : return "Spirit";
			case 7 : return "Stamina";
			case 13 : return "Dodge";
			case 14 : return "Parry";
			case 31 : return "Hit";
			case 32 : return "Crit";
			case 36 : return "Haste"; 
			case 37 : return "Expertise"; 
			case 45 : return "Spell";
			case 49 : return "Mastery";
		}
		return res;
	}
	
	public String getReforge() {
		Long amount = null;
		long sourceStat = -1;
		long targetStat = -1;
		for (Object cur : getStats()){
			JSONObject stat = (JSONObject)cur;
			if (stat.get("reforgedAmount") != null){
				amount = (Long)stat.get("reforgedAmount") * -1;
				sourceStat = (Long)stat.get("stat");
			}else if (stat.get("reforged") != null){
				targetStat = (Long)stat.get("stat");
			}
		}
		String res = "-";
		if (amount != null){
			res = statIdToLabel((int)sourceStat) + " -> " + statIdToLabel((int)targetStat) + " ("+amount+")";
		}
		return res;
	}
}
