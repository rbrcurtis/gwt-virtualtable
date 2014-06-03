package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.io.Serializable;

import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;


public class Range implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Type {

		PERSONAL,
		TOUCH,
		CLOSE,
		MEDIUM,
		LONG,
		INFINITE,
		FEET

	}
	
	public Type type;
	public int feet;
	public Spell spell;
	
	public Range(){}

	public Range(Type type, int feet) {
		super();
		this.type = type;
		this.feet = feet;
	}

	public Range(Type type, int feet, Spell spell) {
		super();
		this.type = type;
		this.feet = feet;
		this.spell = spell;
	}
	

	
}
