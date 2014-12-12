package com.leroy.ronan.wow.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.util.ServiceException;
import com.leroy.ronan.google.gdata.GoogleDriveClient;
import com.leroy.ronan.google.gdata.SpreadSheetManager;
import com.leroy.ronan.wow.api.bean.Character;
import com.leroy.ronan.wow.api.bean.Item;
import com.leroy.ronan.wow.api.bean.data.LegendaryQuest;
import com.leroy.ronan.wow.api.checker.LegendaryChecker.LEGENDARY_LINE;

public class FicheBuilder {

	private static class RowCol{
		public int row;
		public int col;
		private RowCol(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
	
	private static final RowCol SIZE = new RowCol(21, 33);
	private static final RowCol SPE1 = new RowCol(2, 4);
	private static final RowCol SPE2 = new RowCol(2, 10);
	private static final RowCol LEGENDARY = new RowCol(2, 16);
	private static final RowCol HISTORY = new RowCol(2, 22);
	private static final RowCol HISTORY_LAST_TIMESTAMP = new RowCol(4, 33);

	private Character character;
	private SpreadSheetManager manager;
	
	public FicheBuilder(GoogleDriveClient client, String doc, Character character) throws IOException, ServiceException{
		this.character = character;
		this.manager = new SpreadSheetManager(client, doc, character.getName(), SIZE.row, SIZE.col);
	}
	
	public void update() throws Exception {
		this.ficheId();
		this.ficheSpec();
		this.ficheLegendary();
		this.ficheHistory();
		this.manager.update();
	}
	
	private void ficheId() throws IOException, ServiceException {
		String[][] charid = {
				{"Nom", character.getName(), "-", "", "", "", "", "", "-", "", "", "", "", "", "-", "", "", "", "", "", "-"},
				{"Classe (lvl)" 	, character.getClazz().getName()+" ("+character.getLevel()+")"				},
				{"Race (gender)"	, character.getRace().getName()+" ("+(character.getGender()==0?"M":"F")+")"	},
				{"Max ilvl"			, String.valueOf(character.getAverageItemLevel())							},
				{"Profession"		, "Rank"																	},
				{""					, ""																	},
				{character.getPrimaryProfession(0)!=null?character.getPrimaryProfession(0).getName():"-", 
				 character.getPrimaryProfession(0)!=null?String.valueOf(character.getPrimaryProfession(0).getRank()):"-"
				},
				{character.getPrimaryProfession(1)!=null?character.getPrimaryProfession(1).getName():"-", 
				 character.getPrimaryProfession(1)!=null?String.valueOf(character.getPrimaryProfession(1).getRank()):"-"
				},
				{"Opti ?"		, character.getCraftingCheck()													},
		};
		this.manager.push(1, 1, charid);
	}

	private void ficheSpec() throws Exception {
		String spec = character.getSpec() + " ("+character.getRole()+")";

		List<List<String>> charspe = new ArrayList<List<String>>();
		List<String> title = new ArrayList<String>();
		title.add("Spé");
		title.add("ilvl");
		title.add("Date");
		title.add("");
		title.add("");
		charspe.add(title);

		List<String> values = new ArrayList<String>();
		values.add(spec);
		values.add(String.valueOf(character.getAverageItemLevelEquipped()));
		values.add(character.getLastModified());
		values.add("");
		values.add("");
		charspe.add(values);

		List<String> subtitle = new ArrayList<String>();
		subtitle.add("Items");
		subtitle.add("ilvl");
		subtitle.add("gem");
		subtitle.add("enchant");
		subtitle.add("reforge");
		charspe.add(subtitle);

        for (Item.TYPE type : Item.TYPE.values()){
        	Item item = character.getItem(type);
    		List<String> line = new ArrayList<String>();
    		line.add(type.name());
            if (item != null){
	    		line.add(String.valueOf(item.getItemLevel()+item.getUpgrade())+" (+"+item.getUpgrade()+")");
	    		line.add(item.getSockets().size() + " / " + item.getGems().toString());
	    		line.add(item.getEnchant());
	    		line.add(item.getReforge());
            }else{
	    		line.add("-");
	    		line.add("");
	    		line.add("");
	    		line.add("");
            }
    		charspe.add(line);
        }
        if (spec.equals(this.manager.get(SPE1.row+1, SPE1.col))){
    		this.manager.push(SPE1.row, SPE1.col, charspe);
        }else{
    		this.manager.push(SPE2.row, SPE2.col, charspe);
        }
	}

	private void ficheLegendary() throws IOException, ServiceException{
		List<List<String>> legendary = new ArrayList<List<String>>();

		List<String> title = new ArrayList<String>();
		title.add("Legendary");
		legendary.add(title);
		
		List<String> subtitle = new ArrayList<String>();
		subtitle.add("Status");
		subtitle.add("Date");
		subtitle.add("Unlocks");
		subtitle.add("Goal");
		subtitle.add("Quest");
		legendary.add(subtitle);
		
		for (LEGENDARY_LINE line : LEGENDARY_LINE.values()){
			for (LegendaryQuest cur : line.getPath()){
				List<String> quest = new ArrayList<String>();

				String existing = this.manager.get(LEGENDARY.row+legendary.size(), LEGENDARY.col+1);
				if (character.getCheckLegend().containsKey(line.name())){
					quest.add("DONE");
					if (existing == null || existing.length() < 2){
						quest.add(character.getLastModified());
					}else{
						quest.add(existing);
					}
				}else{
					String status = character.getCheckLegend().get(line.name()+"."+cur.getId());

					if (status == null || status.startsWith("TODO")){
						quest.add("TODO");
						quest.add("-");
					}else{
						quest.add("DONE");
						if (existing == null || existing.length() < 2){
							quest.add(character.getLastModified());
						}else{
							quest.add(existing);
						}
					}
				}
				
				quest.add(line.getEnd().getDesc());
				quest.add(cur.getDesc());
				quest.add(cur.getTitle());
				legendary.add(quest);
			}
		}
		this.manager.push(LEGENDARY.row, LEGENDARY.col, legendary);
	}
	
	private void ficheHistory() throws IOException, ServiceException {
		List<List<String>> history = new ArrayList<List<String>>();
		List<String> title = new ArrayList<String>();
		title.add("Historique");
		history.add(title);

		List<String> subtitle = new ArrayList<String>();
		subtitle.add("Date");
		subtitle.add("Spec");
		subtitle.add("ilvl");
		subtitle.add("opti-craft");
		subtitle.add("opti-enchant");
		subtitle.add("opti-gem");
		subtitle.add("prof1");
		subtitle.add("rankprof1");
		subtitle.add("prof2");
		subtitle.add("rankprof2");
		subtitle.add("achievement");
		subtitle.add("timestamp");
		history.add(subtitle);

		List<String> lastLine = new ArrayList<String>();
		lastLine.add(character.getLastModified());
		lastLine.add(character.getSpec() + " ("+character.getRole()+")");
		lastLine.add(String.valueOf(character.getAverageItemLevel()));
		lastLine.add(character.getCraftingCheck());
		lastLine.add(character.getEnchantingCheck());
		lastLine.add(character.getGemmingCheck());
		lastLine.add(character.getPrimaryProfession(0)==null?"-":character.getPrimaryProfession(0).getName());
		lastLine.add(character.getPrimaryProfession(0)==null?"-":String.valueOf(character.getPrimaryProfession(0).getRank()));
		lastLine.add(character.getPrimaryProfession(1)==null?"-":character.getPrimaryProfession(1).getName());
		lastLine.add(character.getPrimaryProfession(1)==null?"-":String.valueOf(character.getPrimaryProfession(1).getRank()));
		lastLine.add(String.valueOf(character.getAchievementPoints()));
		lastLine.add(String.valueOf(character.getTimestamp()));
		history.add(lastLine);

		String timestamp = manager.get(HISTORY_LAST_TIMESTAMP.row, HISTORY_LAST_TIMESTAMP.col);

		if (!String.valueOf(character.getTimestamp()).equals(timestamp)){
			int row = HISTORY_LAST_TIMESTAMP.row;
			String date = manager.get(row, HISTORY.col);
			while (date != null && date.length() > 0 && history.size() < 19){
				List<String> newline = new ArrayList<String>();
				for (int col = HISTORY.col; col <= HISTORY_LAST_TIMESTAMP.col; col++){
					newline.add(manager.get(row, col));
				}
				// On ajoute cette ligne que si elle est différentes de la derniere ligne ajoutée, sinon elle viendra la remplacer.
				boolean same = true;
				List<String> lastline = history.get(history.size()-1);
				for (int i = 1; i < lastLine.size() -1; i++){
					if (!lastLine.get(i).equals(newline.get(i))){
						same = false;
						break;
					}
				}
				if (same){
					history.remove(history.size()-1);
				}
				
				history.add(newline);
				row++;
				date = manager.get(row, HISTORY.col);
			}
			this.manager.push(HISTORY.row, HISTORY.col, history);
		}
	}


}
