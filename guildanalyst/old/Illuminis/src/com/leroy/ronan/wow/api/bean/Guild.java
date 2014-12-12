package com.leroy.ronan.wow.api.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.leroy.ronan.wow.IlluminisTool;
import com.leroy.ronan.wow.api.bean.character.CharacterStub;

public class Guild extends BattleNetBean{

	private static final Logger logger = Logger.getLogger(Guild.class);

	private List<CharacterStub> members;
	
	public Guild(String server, String name) throws Exception{
		super("guild/"+server+"/"+name, "fields=members");
		
		if (IlluminisTool.forcereload){
			this.reload();
		}
	}
	public Long getLastModified(){
		return (Long)get("lastModified");
	}
	public String getName(){
		return (String)get("name");
	}
	public String getRealm(){
		return (String)get("realm");
	}
	public String getBattlegroup(){
		return (String)get("battlegroup");
	}
	public Long getLevel(){
		return (Long)get("level");
	}
	public Long getSide(){
		return (Long)get("side");
	}
	public Long getAchievementPoints(){
		return (Long)get("achievementPoints");
	}
	public List<CharacterStub> getMembers() {
		return this.members;
	}
	
	@Override
	protected void load() {
		List<CharacterStub> res = new ArrayList<CharacterStub>();
		Object members = get("members");
		for (Object member : (JSONArray)members){
			JSONObject cur = (JSONObject)member;

			Long rank = (Long)cur.get("rank");
			
			JSONObject character = (JSONObject)cur.get("character");
			String realm = (String)character.get("realm");
			String name = (String)character.get("name");

			try{
				CharacterStub curChar = new CharacterStub(realm, name, rank);
				res.add(curChar);
			}catch (Exception e){
				logger.error(e.getMessage(), e);
			}
		}
		
		Collections.sort(res, new Comparator<CharacterStub>() {
			@Override
			public int compare(CharacterStub o1, CharacterStub o2) {
				try{
					if (o1.getRank() == o2.getRank()){
						return o1.getName().compareTo(o2.getName());
					}else{
						return o1.getRank().compareTo(o2.getRank());
					}
				}catch (Exception e){
					logger.error(e.getMessage());
				}
				return 0;
			}
		});
		this.members = res;
	}
}