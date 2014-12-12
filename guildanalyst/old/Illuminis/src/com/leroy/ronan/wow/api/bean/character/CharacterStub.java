package com.leroy.ronan.wow.api.bean.character;

import org.apache.log4j.Logger;

import com.leroy.ronan.wow.api.bean.Character;

public class CharacterStub {

	private static final Logger logger = Logger.getLogger(CharacterStub.class);

	private String server;
	private String name;
	private Long rank;
	public CharacterStub(String server, String name, Long rank) {
		super();
		this.server = server;
		this.name = name;
		this.rank = rank;
	}
	public String getServer() {
		return server;
	}
	public String getName() {
		return name;
	}
	public Long getRank() {
		return rank;
	}
	public Character getCharacter() {
		Character res = null;
		try {
			res = new Character(server, name);
			res.setRank(rank);
		} catch (Exception e) {
			logger.error("Pb getting character "+name+" : "+e.getMessage());
			res = null;
		}
		return res;
	}
}
