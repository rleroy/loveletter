package com.leroy.ronan.wow.api.bean.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.leroy.ronan.wow.IlluminisTool;

public class Race {

	private static Map<Long, Race> all = null;
	public static Race getRace(Long key) throws Exception{
		if (all == null){
			Map<Long, Race> res = new ConcurrentHashMap<Long, Race>();
			String json = IlluminisTool.gatherer.gather(false, "data/character/races");
			JSONObject dataObj = (JSONObject)JSONValue.parse(json);
			JSONArray data = (JSONArray)(dataObj.get("races"));

			for (Object obj : data){
				JSONObject cur = (JSONObject)obj;
				Race r = new Race(cur);
				res.put(r.getId(), r);
			}
			all = res;
		}
		return all.get(key);
	}

	private JSONObject data;
	private Race(JSONObject data) {
		this.data = data;
	}
	
	public Long getId(){
		return (Long)data.get("id");
	}
	public Long getMask(){
		return (Long)data.get("mask");
	}
	public String getSide(){
		return (String)data.get("side");
	}
	public String getName(){
		return (String)data.get("name");
	}
}
