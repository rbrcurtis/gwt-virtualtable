package com.mut8ed.battlemap.server.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mortbay.util.ajax.Continuation;

import com.mut8ed.battlemap.shared.event.MapEvent;
import com.mut8ed.battlemap.shared.exception.NotInvitedException;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Logger logger = Logger.getLogger(User.class);

	private String accountLevel = null;
	transient private Continuation continuation;
	private Date createDate;
	private Game currentGame;
	private String email;
	private int id;
	transient private List<MapEvent> mapEvents = new LinkedList<MapEvent>();
	private String sessionId;
	private String userName;
	private String color;
	private String characterName;
	private boolean online = false;
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getCharacterName() {
		return (characterName!=null)?characterName:userName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public User(){}

	public User(String sessionId){
		this.sessionId = sessionId;
	}

	public User(
			String accountLevel, Timestamp createDate, String email,
			Integer id, 
			String username
			) {
		this.accountLevel = accountLevel;
		this.createDate = createDate;
		this.email = email;
		this.id = id;
		this.userName = username;
	}

	public static String hashPassword(String password) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16);
		} catch (NoSuchAlgorithmException nsae) {
			// ignore
		}
		return hashword;
	}

	public synchronized void addEvent(MapEvent mapEvent){
		mapEvents.add(mapEvent);
		logger.debug("added event "+mapEvent+" to user "+userName);
		if (continuation!=null && continuation.isPending()){
			logger.info("resuming continuation");
			continuation.resume();
			continuation = null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}


	public String getAccountLevel() {
		return accountLevel;
	}


	public Continuation getContinuation(){
		return continuation;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Game getCurrentGame(){
		return currentGame;
	}

	public String getEmail() {
		return email;
	}

	public List<MapEvent> getEvents() {
		return mapEvents;
	}

	public int getId() {
		return id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setAccountLevel(String accountLevel) {
		this.accountLevel = accountLevel;
	}

	public void setContinuation(Continuation continuation) {
		this.continuation = continuation;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public void setCurrentGame(Game game) {
		if (game==null)return;
		try {
			game.addCurrentPlayer(this);
			this.currentGame = game;
			logger.debug(Thread.currentThread().getName()+" set game for user "+this+" to "+game);
		} catch (NotInvitedException e) {
			logger.error(e,e);
		}
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEvents(List<MapEvent> mapEvents) {
		this.mapEvents = mapEvents;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	@Override
	public String toString(){
		return "["+this.getUserName()+":"+this.id+"]";
	}

	public boolean isOnline() {
		return online;
	}
	
	public void setOnline(boolean online){
		this.online = online;
	}


}
