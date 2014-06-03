package com.mut8ed.battlemap.shared.event;


/** 
 * tell the browser that it needs to login
 * @author ryan
 *
 */
public class LoginEvent extends MapEvent {

	private String message;

	public LoginEvent(String playerName) {
		message = playerName+" has joined the game";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
