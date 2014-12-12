package com.leroy.ronan.wow.api.bean.data;

import com.leroy.ronan.wow.api.bean.Quest;

public class LegendaryQuest extends Quest{

	private String desc;
	public LegendaryQuest(Long id, String desc) throws Exception {
		super(id);
		this.desc = desc;
	}
	public String getDesc() {
		return desc;
	}
	
}
