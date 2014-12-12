package com.leroy.ronan.wow;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.leroy.ronan.google.gdata.GoogleDriveClient;
import com.leroy.ronan.wow.api.BattleNetWoWClient;
import com.leroy.ronan.wow.api.FicheBuilder;
import com.leroy.ronan.wow.api.bean.Character;
import com.leroy.ronan.wow.api.bean.Guild;
import com.leroy.ronan.wow.api.bean.character.CharacterStub;
import com.leroy.ronan.wow.api.bean.character.Profession;
import com.leroy.ronan.wow.api.bean.data.Recipe;

public class IlluminisTool {

	private static final Logger logger = Logger.getLogger(IlluminisTool.class);

	private GoogleDriveClient updater;
	private static final String app = "Illuminis Tools by Pamynx";
	private static final String login = "pamynx@amis.tv";
	private static final String passw = "sakamein";
	private static final String doc   = "ILLUMINIS";
	private static final String roster2sheet = "ROSTER2";
	private static final String[] roster2members = {
		// Membres
		"Aksouna",
		"Alven",
		"Amnesýk",
		"Analgésique",
		"Arëgon",
		"Blöödhünter",
		"Bötöx",
		"Cataclips",
		"Chayote",
		"Elfice",
		"Hâmia",
		"Hemadri",
		"Leliel",
		"Lilandra",
		"Mctosa",
		"Médox",
		"Nakâ",
		"Pamynx",
		"Snomead",
		"Stabyle",
		"Timida",

		"Aphykith", // = Pamynx
		"Ekel",     // = Hemadri
		"Mchino",   // = Mctosa
		"Sno",      // = Snomead
		"Stalla",   // = Stabyle
	};

	private static final String cache = "/Volumes/DATA/wowtoolcache/";
	private static final String server = "eu.battle.net";
	private static final String realm = "Sargeras";
//	private static final String[] guilds = {"ILLuMiNïS", "BIG BALLZ"};
	private static final String[] guilds = {"Insãnity"};

	public static final boolean forcereload = true;

	public static BattleNetWoWClient gatherer = new BattleNetWoWClient(cache, server);
	
	public static void main(String[] args) throws Exception{

 		IlluminisTool tool = new IlluminisTool();

 		tool.update54Crafts();
 		
 		for (String guild : guilds) {
			Guild g = new Guild(realm, guild);
	        tool.update(g.getMembers(), guild);
		}
 		/*
		List<CharacterStub> roster2 = new ArrayList<CharacterStub>();
		for (String name : roster2members){
			CharacterStub cur = new CharacterStub(realm, name, null);
			roster2.add(cur);
			tool.fiche(cur);
		}
        tool.update(roster2, roster2sheet);
        Character cur = new Character(realm, "Pamynx", true);
        cur.reload();
        for (Pet pet : cur.getPets()){
        	if (pet.getCanBattle()){
	    		logger.info(
	    				pet.getLevel()+"\t"
					   +pet.getHealth()+"\t"
					   +pet.getPower()+"\t"
					   +pet.getSpeed()+"\t"
					   +pet.getQualityId()+"\t"
					   +pet.getBreed()+"\t"
					   +pet.getSpecie().getPetTypeId()+"\t"
					   +pet.getName()+"\t"
	   				);
        	}
        }
        */
	}

	public IlluminisTool() throws Exception{
		updater = new GoogleDriveClient(app, login, passw);
	}
	
	private void update(List<CharacterStub> charList, String sheet) throws Exception {
		updater.setWorksheetSize(doc, sheet, 1, 26);

		int col = 1;
        updater.writeCell(doc, sheet, 1,col++, "name");
        updater.writeCell(doc, sheet, 1,col++, "class");
        updater.writeCell(doc, sheet, 1,col++, "race");
        updater.writeCell(doc, sheet, 1,col++, "avgilvl");
        updater.writeCell(doc, sheet, 1,col++, "ilvl");

        updater.writeCell(doc, sheet, 1,col++, "quests");
        updater.writeCell(doc, sheet, 1,col++, "opti-craft");
        updater.writeCell(doc, sheet, 1,col++, "opti-enchant");
        updater.writeCell(doc, sheet, 1,col++, "opti-gem");
        updater.writeCell(doc, sheet, 1,col++, "enchant");

        updater.writeCell(doc, sheet, 1,col++, "gem");
        updater.writeCell(doc, sheet, 1,col++, "prof1");
        updater.writeCell(doc, sheet, 1,col++, "rankprof1");
        updater.writeCell(doc, sheet, 1,col++, "prof2");
        updater.writeCell(doc, sheet, 1,col++, "rankprof2");

        updater.writeCell(doc, sheet, 1,col++, "bonusprof");
        updater.writeCell(doc, sheet, 1,col++, "lastModified");
        updater.writeCell(doc, sheet, 1,col++, "realm");
        updater.writeCell(doc, sheet, 1,col++, "battlegroup");
        updater.writeCell(doc, sheet, 1,col++, "gender");

        updater.writeCell(doc, sheet, 1,col++, "level");
        updater.writeCell(doc, sheet, 1,col++, "rank");
        updater.writeCell(doc, sheet, 1,col++, "achievementPoints");
        updater.writeCell(doc, sheet, 1,col++, "thumbnail");
        updater.writeCell(doc, sheet, 1,col++, "calcClass");

        updater.writeCell(doc, sheet, 1,col++, "legendary");
        
        for (CharacterStub curStub : charList){
        	Character cur = curStub.getCharacter();
        	if (cur != null && cur.getName() != null){
	    		logger.info("inserting "+cur.getName()+" line...");
	
	    		String[][] values = 
	    			{
	    				{"name",              cur.getName()},
	    				{"class",             cur.getClazz().getName()},
	    				{"race",              cur.getRace().getName()},
	    				{"avgilvl",           String.valueOf(cur.getAverageItemLevel())},
	    				{"ilvl",              String.valueOf(cur.getAverageItemLevelEquipped())},
	
	    				{"quests",            cur.getLegendaryCheck()},
	    				{"opti-craft",        cur.getCraftingCheck()},
	    				{"opti-enchant",      cur.getEnchantingCheck()},
	    				{"opti-gem",          cur.getGemmingCheck()},
	    				{"enchant",           String.valueOf(cur.getCheckEnchants())},
	
	    				{"gem",               String.valueOf(cur.getCheckGems())},
	    				{"prof1",             cur.getPrimaryProfession(0)==null?"-":cur.getPrimaryProfession(0).getName()+" ("+cur.getPrimaryProfession(0).getNb54Recipes()+")"},
	    				{"rankprof1",         cur.getPrimaryProfession(0)==null?"-":String.valueOf(cur.getPrimaryProfession(0).getRank())},
	    				{"prof2",             cur.getPrimaryProfession(1)==null?"-":cur.getPrimaryProfession(1).getName()+" ("+cur.getPrimaryProfession(1).getNb54Recipes()+")"},
	    				{"rankprof2",         cur.getPrimaryProfession(1)==null?"-":String.valueOf(cur.getPrimaryProfession(1).getRank())},
	
	    				{"bonusprof",         String.valueOf(cur.getCheckProf())},
	    				{"lastModified",      cur.getLastModified()},
	    				{"realm",             cur.getRealm()==null?"-": cur.getRealm()},
	    				{"battlegroup",       cur.getBattlegroup()==null?"-": cur.getBattlegroup()},
	    				{"gender",            String.valueOf(cur.getGender())},
	
	    				{"level",             String.valueOf(cur.getLevel())},
	    				{"rank",              String.valueOf(cur.getRank())},
	    				{"achievementPoints", String.valueOf(cur.getAchievementPoints())},
	    				{"thumbnail",         cur.getThumbnail()},
	    				{"calcClass",         cur.getCalcClass()},
	
	    				{"legendary",         String.valueOf(cur.getCheckLegend())},
	    			};
	    		updater.insertLine(doc, sheet, values);
        	}
        }
	}
	
	private void fiche(CharacterStub character) throws Exception{
		logger.info("updating "+character.getName()+" sheet...");
		FicheBuilder builder = new FicheBuilder(updater, doc, character.getCharacter());
		builder.update();
		logger.info("updated "+character.getName()+" sheet.");
	}
	
	private void update54Crafts() throws Exception{
		String sheet = "Crafts-54";
		updater.setWorksheetSize(doc, sheet, 1, 22);

		int col = 1;
        updater.writeCell(doc, sheet, 1,col++, "Guild");
        updater.writeCell(doc, sheet, 1,col++, "Name");
        updater.writeCell(doc, sheet, 1,col++, "Profession");
        updater.writeCell(doc, sheet, 1,col++, "Count");
        for (Recipe.CRAFTS54 curRecipe : Recipe.CRAFTS54.values()){
            updater.writeCell(doc, sheet, 1,col++, curRecipe.getType()+"-"+curRecipe.getSlot()+"-"+curRecipe.getSpe());
        }
        
		for (String guild : guilds) {
			Guild g = new Guild(realm, guild);
			for (CharacterStub curCharStub : g.getMembers()){
				Character curChar = curCharStub.getCharacter();
				if (curChar != null){
					for (int i = 0; i < 2; i++){
						Profession p = curChar.getPrimaryProfession(i);
						if (p != null && p.getNb54Recipes() > 0){
				    		String[][] values = {
				    				{"Guild", guild},
				    				{"Name", curChar.getName()},
				    				{"Profession", p.getName()},
				    				{"Count",  String.valueOf((p.getNb54Recipes()-1))},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    				{"",""},
				    		};
				    		
				    		for (int j = 0; j < Recipe.CRAFTS54.values().length; j++){
				    			Recipe.CRAFTS54 curRecipe = Recipe.CRAFTS54.values()[j];
				            	values[4+j][0] = curRecipe.getType()+"-"+curRecipe.getSlot()+"-"+curRecipe.getSpe();
				            	if (curChar.hasRecipe(curRecipe.getId())){
				            		Recipe recipe = new Recipe(curRecipe.getId());
					            	values[4+j][1] = recipe.getName();
				            	}
				    		}
				    		updater.insertLine(doc, sheet, values);
						}
					}
				}
			}
		}
	}
	

}
