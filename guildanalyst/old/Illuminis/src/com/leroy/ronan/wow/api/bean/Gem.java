package com.leroy.ronan.wow.api.bean;

import org.json.simple.JSONObject;



public class Gem extends BattleNetBean{

	private String name;
	
	public Gem(Long id) throws Exception {
		super("item/"+id, "");
	}

	@Override
	protected void load() {
		JSONObject gemInfo = (JSONObject)get("gemInfo");
		JSONObject bonus = (JSONObject)gemInfo.get("bonus");
		name = (String)bonus.get("name");
	}
	
	public String getName(){
		return name;
	}

}
