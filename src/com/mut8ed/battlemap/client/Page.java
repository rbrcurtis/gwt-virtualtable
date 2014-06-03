package com.mut8ed.battlemap.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

public enum Page {
	ADD_MAP_OBJECT,
	CREATE_GAME,
	LIST_GAMES, 
	INVITE,
	LANDING, 
	LOGIN, 
	MAP, 
	MAP_LIST, 
	OPTIONS, 
	SELECT_FIGURE, 
	TEST, 
	CHARACTER, 
	CHAT; 

	/**
	 * tell the browser to go to the associated page
	 */
	public void redirect() {
		if (this==LOGIN){
			String hash = History.getToken();
			if (hash.toLowerCase().equals("login"))hash="landing";
			String host = Window.Location.getHostName();
			String path = Window.Location.getPath();
			if (path.startsWith("/"))path = path.substring(1);
			String target = "http://"+host+"/user/login?destination="+path+"?page="+hash;
			Window.Location.assign(target);
		} else {
			History.newItem(this.name().toLowerCase());
		}
	}
}