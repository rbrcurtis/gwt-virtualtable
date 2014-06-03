package com.mut8ed.battlemap.shared.dto.charactersheet.spell;

import java.io.Serializable;


public class SpellSlot implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean cast = false;
	private Spell spell = null;
	
	public SpellSlot(){}

	public SpellSlot(boolean cast, Spell spell) {
		this.cast = cast;
		this.spell = spell;
	}

	public boolean isCast() {
		return cast;
	}

	public void setCast(boolean cast) {
		this.cast = cast;
	}

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}

	@Override
	public String toString() {
		return "SpellSlot [spell=" + spell + ", cast=" + cast + "]";
	}
	
}
