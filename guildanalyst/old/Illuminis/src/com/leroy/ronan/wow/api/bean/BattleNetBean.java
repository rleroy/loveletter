package com.leroy.ronan.wow.api.bean;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.leroy.ronan.wow.IlluminisTool;

public abstract class BattleNetBean {
	
	private String path;
	private String options;
	private JSONObject data;
	
	public BattleNetBean(String path) throws Exception{
		this(path, "");
	}
	
	public BattleNetBean(String path, String options) throws Exception {
		this.path = path;
		this.options = options;
		basicload(false);
	}

	public void reload() throws Exception{
		basicload(true);
	}
	
	protected Object get(String attribute) {
		Object res = null;
		if (data != null){
			res = data.get(attribute);
		}
		return res;
	}
	
	private void basicload(boolean reload) throws Exception{
		String json = IlluminisTool.gatherer.gather(reload, path, options);
		this.data = (JSONObject)JSONValue.parse(json);
		if (this.data == null){
			throw new Exception("No data for : "+path+"?"+options);
		}
		load();
	}
	
	protected abstract void load();

}
