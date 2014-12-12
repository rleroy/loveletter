package com.leroy.ronan.wow.api.bean.data;

import com.leroy.ronan.wow.api.bean.BattleNetBean;

public class Recipe extends BattleNetBean{

	public enum CRAFTS54{
		CLOTH_BELT_HEAL_54(142960L, "CLOTH", "BELT", "HEAL"),
		CLOTH_BELT_DPS_54 (142964L, "CLOTH", "BELT", "DPS"),
		CLOTH_PANT_HEAL_54(142951L, "CLOTH", "PANT", "HEAL"),
		CLOTH_PANT_DPS_54 (142955L, "CLOTH", "PANT", "DPS"),

		LEATHER_BELT_INT_54(142961L, "LEATHER", "BELT", "INT"),
		LEATHER_BELT_AGI_54(142965L, "LEATHER", "BELT", "AGI"),
		LEATHER_PANT_INT_54(142952L, "LEATHER", "PANT", "INT"),
		LEATHER_PANT_AGI_54(142956L, "LEATHER", "PANT", "AGI"),
		MAIL_BELT_INT_54(142962L, "MAIL", "BELT", "INT"),
		MAIL_BELT_AGI_54(142966L, "MAIL", "BELT", "AGI"),
		MAIL_PANT_INT_54(142953L, "MAIL", "PANT", "INT"),
		MAIL_PANT_AGI_54(142957L, "MAIL", "PANT", "AGI"),

		PLATE_BELT_HEAL_54(142963L, "PLATE", "BELT", "HEAL"),
		PLATE_BELT_DPS_54 (142968L, "PLATE", "BELT", "DPS"),
		PLATE_BELT_TANK_54(142967L, "PLATE", "BELT", "TANK"),
		PLATE_PANT_HEAL_54(142954L, "PLATE", "PANT", "HEAL"),
		PLATE_PANT_DPS_54 (142959L, "PLATE", "PANT", "DPS"),
		PLATE_PANT_TANK_54(142958L, "PLATE", "PANT", "TANK"),
		;
		
		private Long id;
		private String type;
		private String slot;
		private String spe;
		private CRAFTS54(Long id, String type, String slot, String spe) {
			this.id = id;
			this.type = type;
			this.slot = slot;
			this.spe = spe;
		}
		public Long getId() {
			return id;
		}
		public String getType() {
			return type;
		}
		public String getSlot() {
			return slot;
		}
		public String getSpe() {
			return spe;
		}
	}
	
	private Long TAILORING_CD_ACC_54 = 146925L;
	private Long TAILORING_CD_NRM_54 = 143011L;
	private Long LEATHERWORKING_CD_ACC_54 = 146923L;
	private Long LEATHERWORKING_CD_NRM_54 = 142976L;
	private Long BLACKSMITHING_CD_ACC_54 = 146921L;
	private Long BLACKSMITHING_CD_NRM_54 = 143255L;

	public Recipe(Long id) throws Exception {
		super("recipe/"+id, "");
	}

	@Override
	protected void load() {
	}
	
	public Long getId(){
		return (Long)get("id");
	}

	public String getName(){
		return (String)get("name");
	}
	
	public String getProfession(){
		return (String)get("profession");
	}
	
	public boolean isAddedIn54(){
		boolean res = false;
		if (this.getName().startsWith("Crafted Malevolent Gladiator's")){
			res = true;
		} else {
			for (CRAFTS54 cur : CRAFTS54.values()){
				if (cur.getId().equals(this.getId())){
					res = true;
				}
			}
		}
		return res;
	}
}
