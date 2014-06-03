package com.mut8ed.battlemap.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PlayerModel implements IsSerializable {
	
	public String userName;
	public String characterName;
	public String gravatarUrl;
	public String color;
	public boolean isCurrentUser;
	
	public PlayerModel(){}

	public PlayerModel(String userName, String chaacterName,
			String gravatarUrl, String color, boolean isCurrentUser) {
		super();
		this.userName = userName;
		this.characterName = chaacterName;
		this.gravatarUrl = gravatarUrl;
		this.color = color;
		this.isCurrentUser = isCurrentUser;
	}
	

	public PlayerModel clone(){
		return new PlayerModel(userName, characterName, gravatarUrl, color, isCurrentUser);
	}
}
