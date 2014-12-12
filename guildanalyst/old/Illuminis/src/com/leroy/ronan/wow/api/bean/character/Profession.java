package com.leroy.ronan.wow.api.bean.character;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.leroy.ronan.wow.api.bean.data.Recipe;

public class Profession {

	private static final Logger logger = Logger.getLogger(Profession.class);

	public enum TYPE{
		Alchemy,
		Blacksmithing,
		Enchanting,
		Engineering,
		Herbalism,
		Inscription,
		Jewelcrafting,
		Leatherworking,
		Mining,
		Skinning,
		Tailoring
	}
	
	private JSONObject data;
	private List<Long> recipes;
	
	public Profession(JSONObject data) {
		super();
		this.data = data;
		recipes = new ArrayList<Long>();
		for (Object curObj : (JSONArray)data.get("recipes")){
			Long cur = (Long)curObj;
			if (cur != null){
				recipes.add(cur);
			}
		}
	}
	
	public Long getId(){
		return (Long)data.get("id");
	}
	public Long getMax(){
		return (Long)data.get("max");
	}
	public Long getRank(){
		return (Long)data.get("rank");
	}
	public String getName(){
		return (String)data.get("name");
	}
	public String getIcon(){
		return (String)data.get("icon");
	}
	public List<Long> getRecipes(){
		return this.recipes;
	}
	
	public int getNb54Recipes(){
		int res = 0;
		for (Long cur : getRecipes()){
			try{
				Recipe recipe = new Recipe(cur);
				if (recipe.isAddedIn54()){
					res++;
				}
			}catch(Exception e){
				logger.error("Pbm loading recipe "+cur+". "+e.getMessage());
			}
		}
		return res;
	}
}
