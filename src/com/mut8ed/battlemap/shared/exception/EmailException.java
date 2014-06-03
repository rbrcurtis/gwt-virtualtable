package com.mut8ed.battlemap.shared.exception;

public class EmailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailException(Exception e) {
		super(e);
	}
}
