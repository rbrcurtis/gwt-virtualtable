package com.mut8ed.battlemap.shared.dto;

import java.io.Serializable;

public class ChatMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public int gameNid;
	public String username;
	public String message;
	public String color;
	public String timestamp;
	
	public ChatMessage(){	}

	public ChatMessage(int gameNid, String username, String message, String color) {
		this.gameNid = gameNid;
		this.username = username;
		this.message = message;
		this.color = color;
	}

	@Override
	public String toString() {
		return "ChatMessage [gameNid=" + gameNid + ", username=" + username
				+ ", message=" + message + ", color=" + color + ", timestamp="
				+ timestamp + "]";
	}

}
