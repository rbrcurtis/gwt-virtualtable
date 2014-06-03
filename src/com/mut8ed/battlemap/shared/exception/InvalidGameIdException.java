package com.mut8ed.battlemap.shared.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvalidGameIdException extends RuntimeException implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public InvalidGameIdException(int gameId) {
		super("The specified game does not exist:"+gameId);
	}
	
	public InvalidGameIdException(){
		super("The specified game does not exist");
	}

}
