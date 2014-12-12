package com.leroy.ronan.wow.api.bean.data;

import com.leroy.ronan.wow.api.bean.BattleNetBean;

public class Spell extends BattleNetBean{

	public Spell(int id) throws Exception {
		super("spell/"+id, "");
	}

	@Override
	protected void load() {
	}
	
	public String getName(){
		return (String)get("name");
	}

}
