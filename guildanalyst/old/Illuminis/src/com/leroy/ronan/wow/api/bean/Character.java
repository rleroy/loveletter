package com.leroy.ronan.wow.api.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.leroy.ronan.wow.IlluminisTool;
import com.leroy.ronan.wow.api.bean.character.Pet;
import com.leroy.ronan.wow.api.bean.character.Profession;
import com.leroy.ronan.wow.api.bean.data.Class;
import com.leroy.ronan.wow.api.bean.data.Race;
import com.leroy.ronan.wow.api.bean.data.Recipe;
import com.leroy.ronan.wow.api.bean.data.Stat;
import com.leroy.ronan.wow.api.checker.EnchantChecker;
import com.leroy.ronan.wow.api.checker.GemChecker;
import com.leroy.ronan.wow.api.checker.LegendaryChecker;
import com.leroy.ronan.wow.api.checker.ProfessionChecker;


public class Character extends BattleNetBean{

	private static final Logger logger = Logger.getLogger(Character.class);
	
	private Long rank = null;

	private List<Profession> primary;
	private List<Profession> secondary;
	private List<Pet> pets = null;
	
	private Long averageItemLevel;
	private Long averageItemLevelEquipped;
	private Map<String, Item> items;
	private List<Long> quests;
	
	private Map<String, String> checkGems;
	private Map<String, String> checkEnchants;
	private Map<String, String> checkProf;
	private Map<String, String> checkLegend;
	
	public Character(String server, String name) throws Exception{
		this(server, name, true);
	}

	public Character(String server, String name, boolean withpets) throws Exception{
		super("character/"+server+"/"+name, "fields=professions&fields=items&fields=quests&fields=talents&fields=stats"+(withpets?"&fields=pets":""));

		if (IlluminisTool.forcereload){
			this.reload();
		}

		GemChecker gem = new GemChecker(this);
		checkGems = gem.check();

    	EnchantChecker    enchant = new EnchantChecker(this);
    	checkEnchants = enchant.check();
    	
    	ProfessionChecker prof = new ProfessionChecker(this);
    	checkProf = prof.check();

    	LegendaryChecker  legend  = new LegendaryChecker(this);
    	checkLegend   = legend.check();
	}

	public Long getRank() {
		return rank;
	}
	public void setRank(Long rank) {
		this.rank = rank;
	}
	public String getLastModified(){
		Date d = new Date((Long)get("lastModified"));
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:ss");
		return df.format(d);
	}
	public Long getTimestamp(){
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
	public Class getClazz(){
		Class res = null;
		try{
			res = Class.getClass((Long)get("class"));
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return res;
	}
	public Race getRace(){
		Race res = null;
		try{
			res = Race.getRace((Long)get("race"));
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return res;
	}
	public Long getGender(){
		return (Long)get("gender"); // 0 = Male / 1 = Female
	}
	public Long getLevel(){
		return (Long)get("level");
	}
	public Long getAchievementPoints(){
		return (Long)get("achievementPoints");
	}
	public String getThumbnail(){
		return (String)get("thumbnail");
	}
	public String getCalcClass(){
		return (String)get("calcClass");
	}
	public String getStat(Stat stat){
		String res;
		JSONObject stats = (JSONObject)get("stats");
		if (stat.isNumber()){
			Long val = (Long)stats.get(stat.getName());
			res = String.valueOf(val);
		}else{
			res = (String)stats.get(stat.getName());
		}
		return res;
	}
	public String getSpec(){
		for (Object cur : (JSONArray)get("talents")){
			JSONObject obj = (JSONObject)cur;
			Object selected = obj.get("selected");
			if (selected != null && Boolean.TRUE.equals(selected)){
				JSONObject spec = (JSONObject)obj.get("spec");
				return (String)spec.get("name");
			}
		}
		return "-";
	}
	public String getRole(){
		for (Object cur : (JSONArray)get("talents")){
			JSONObject obj = (JSONObject)cur;
			Object selected = obj.get("selected");
			if (selected != null && Boolean.TRUE.equals(selected)){
				JSONObject spec = (JSONObject)obj.get("spec");
				return (String)spec.get("role");
			}
		}
		return "-";
	}
	public List<Pet> getPets(){
		if (pets == null){
			loadPets();
		}
		return this.pets;
	}
	public Profession getPrimaryProfession(int index){
		if (this.primary.size() > index){
			return this.primary.get(index);
		}
		return null;
	}
	public Profession getSecondaryProfession(int index){
		if (this.secondary.size() > index){
			return this.secondary.get(index);
		}
		return null;
	}
	public Long getAverageItemLevel() {
		return averageItemLevel;
	}
	public Long getAverageItemLevelEquipped() {
		return averageItemLevelEquipped;
	}
	public Item getItem(Item.TYPE type){
		Item res = null;
		if (this.items != null){
			res = this.items.get(type.name());
		}
		return res;
	}
	
	public List<Long> getQuests(){
		return this.quests;
	}
	
	public Map<String, String> getCheckGems() {
		return checkGems;
	}

	public Map<String, String> getCheckEnchants() {
		return checkEnchants;
	}

	public Map<String, String> getCheckProf() {
		return checkProf;
	}

	public Map<String, String> getCheckLegend() {
		return checkLegend;
	}

	public String getGemmingCheck() {
		String gemmingCheck = "OK";
		if (!checkGems.isEmpty()){
			gemmingCheck = "Gemming not OK !";
		}
		return gemmingCheck;
	}

	public String getEnchantingCheck() {
		String enchantingCheck = "OK";
    	if (!checkEnchants.isEmpty()){
    		enchantingCheck = "Enchanting not OK !";
    	}
		return enchantingCheck;
	}

	public String getCraftingCheck() {
    	String craftingCheck = "OK";
    	if (!checkProf.isEmpty()){
    		craftingCheck = "Crafting not OK !";
    	}
		return craftingCheck;
	}

	public String getLegendaryCheck() {
		String legendaryCheck;
		if (checkLegend.containsKey("line4")){
    		legendaryCheck = "4 - CLOAK";
    	}else if (checkLegend.containsKey("line3")){
    		legendaryCheck = "3 - META";
    	}else if (checkLegend.containsKey("line2")){
    		legendaryCheck = "2 - EYE";
    	}else if (checkLegend.containsKey("line1")){
    		legendaryCheck = "1 - SHA-TOUCHED";
    	}else{
    		legendaryCheck = "0 - NOTHING";
    	}
		return legendaryCheck;
	}

	@Override
	protected void load() {
		loadProfessions();
		loadItems();
		loadQuests();
	}
	
	private void loadProfessions() {
		primary = new ArrayList<Profession>();
		secondary = new ArrayList<Profession>();
		
		JSONObject types = (JSONObject)get("professions");
		if (types != null){
			for (Object cur : (JSONArray)types.get("primary")){
				Profession prof = new Profession((JSONObject)cur);
				primary.add(prof);
			}
	
			for (Object cur : (JSONArray)types.get("secondary")){
				Profession prof = new Profession((JSONObject)cur);
				secondary.add(prof);
			}
		}
		Collections.sort(primary, new Comparator<Profession>() {
			@Override
			public int compare(Profession p1, Profession p2) {
				return p1.getName().compareTo(p2.getName());
			}
		});
		Collections.sort(secondary, new Comparator<Profession>() {
			@Override
			public int compare(Profession p1, Profession p2) {
				return p1.getName().compareTo(p2.getName());
			}
		});
	}
	
	
	private void loadPets(){
		pets = new ArrayList<Pet>();
		JSONObject petsbloc = (JSONObject)get("pets");
		if (petsbloc != null){
			for (Object cur : (JSONArray)petsbloc.get("collected")){
				Pet pet = new Pet((JSONObject)cur);
				pets.add(pet);
			}
		}
	}
	
	private void loadItems() {
		JSONObject items = (JSONObject)get("items");
		if (items != null){
			averageItemLevel = (Long)items.get("averageItemLevel");
			averageItemLevelEquipped = (Long)items.get("averageItemLevelEquipped");

			this.items = new HashMap<String, Item>();
			for (Item.TYPE slot : Item.TYPE.values()){
				if (items.get(slot.name()) != null){
					JSONObject item = (JSONObject)items.get(slot.name());
					Long id = (Long)item.get("id");
					JSONObject tooltipParams = (JSONObject)item.get("tooltipParams");
					JSONArray stats = (JSONArray)item.get("stats");
					try{
						Item cur = new Item(id, tooltipParams, stats);
						this.items.put(slot.name(), cur);
					}catch(Exception e){
						logger.error("Item not found : id="+id+"/"+item.toJSONString());
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
	}
	
	private void loadQuests(){
		quests = new ArrayList<Long>();
		if (get("quests") != null){
			for (Object curObj : (JSONArray)get("quests")){
				Long cur = (Long)curObj;
				if (cur != null){
					quests.add(cur);
				}
			}
		}
	}

	public boolean hasRecipe(Long id) {
		boolean res = false;
		try{
			Recipe recipe = new Recipe(id);
			for (int i = 0; i < 2; i++){
				Profession profession = getPrimaryProfession(i);
				if (profession.getName().equals(recipe.getProfession())){
					for (Long curId : profession.getRecipes()){
						if (curId.equals(id)){
							res = true;
						}
					}
				}
			}
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		return res;
	}

}
