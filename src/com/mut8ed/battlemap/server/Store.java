package com.mut8ed.battlemap.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.mut8ed.battlemap.server.dto.Game;
import com.mut8ed.battlemap.server.dto.User;

public class Store {

	public static final Logger logger = Logger.getLogger(Store.class);
	
	private static Store instance = null;
	
	private Map<Integer, Game> games = new ConcurrentHashMap<Integer, Game>();
	private Map<String, User> users = new ConcurrentHashMap<String, User>();
	
	private Store(){}
	
	public static synchronized Store getInstance(){
		if (instance==null){
			instance = new Store();
		}
		return instance;
	}

	public Map<Integer, Game> getGames() {
		return games;
	}

	public void setGames(Map<Integer, Game> games) {
		this.games = games;
	}

	public Map<String, User> getUsers() {
		return users;
	}

	public void setUsers(Map<String, User> users) {
		this.users = users;
	}

	public synchronized User addUser(String sessionId, User user) {
		logger.debug("add user called for "+sessionId+" / "+user);
		if (users.get(sessionId)!=null)return users.get(sessionId);
		else {
			users.put(sessionId, user);
			return user; 
		}
	}

	public synchronized Game addGame(int nid, Game game) {
		logger.debug("add game called for "+nid+" / "+game);
		if (games.get(nid)!=null)return games.get(nid);
		else {
			games.put(nid, game);
			return game;
		}
	}
	
}
