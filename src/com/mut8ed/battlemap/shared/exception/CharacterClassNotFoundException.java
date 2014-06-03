package com.mut8ed.battlemap.shared.exception;

import java.io.Serializable;

public class CharacterClassNotFoundException extends RuntimeException implements Serializable {
	public CharacterClassNotFoundException(String characterClass) {
		super("CharacterClass "+characterClass+" was not found in the given character sheet");
	}

	private static final long serialVersionUID = 1L;

}
