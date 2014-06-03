package com.mut8ed.battlemap.server.dto;

import com.mut8ed.battlemap.server.util.Util;

public class Invite {
	
	public final String code;
	public final String email;
	public final int gameNid;
	public String id;
	
	public Invite(String email, int gameNid) {
		this.email = email;
		this.gameNid = gameNid;
		code = Util.getStringMD5(""+((int)(Math.random()*1000000000)));
	}

	public Invite(String id, String email, int gameNid, String code) {
		this.id = id;
		this.email = email;
		this.gameNid = gameNid;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public String getEmail() {
		return email;
	}

	public int getGameNid() {
		return gameNid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Invite [id=" + id + ", email=" + email + ", gameNid=" + gameNid
				+ ", code=" + code + "]";
	}
	
	
	
}
