package com.leroy.ronan.wow.api.bean.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.leroy.ronan.wow.IlluminisTool;

public class Class {
	
	private static Map<Long, Class> all = null;
	public static Class getClass(Long key) throws Exception{
		if (all == null){
			Map<Long, Class> res = new ConcurrentHashMap<Long, Class>();
			String json = IlluminisTool.gatherer.gather(false, "data/character/classes");
			JSONObject dataObj = (JSONObject)JSONValue.parse(json);
			JSONArray data = (JSONArray)(dataObj.get("classes"));

			for (Object obj : data){
				JSONObject cur = (JSONObject)obj;
				Class c = new Class(cur);
				res.put(c.getId(), c);
			}
			all = res;
		}
		return all.get(key);
	}

	private JSONObject data;
	private Class(JSONObject data) {
		this.data = data;
	}
	
	public Long getId(){
		return (Long)data.get("id");
	}
	public Long getMask(){
		return (Long)data.get("mask");
	}
	public String getPowerType(){
		return (String)data.get("powerType");
	}
	public String getName(){
		return (String)data.get("name");
	}

}
