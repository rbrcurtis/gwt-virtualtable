package com.mut8ed.battlemap.shared.dto.charactersheet.spell;

import java.io.Serializable;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Range;

public class Spell implements Serializable {
	private static final long serialVersionUID = 1L;


	private AreaOfEffect area;
	private CharacterSheet characterSheet;
	private int level;
	private String name;
	private Range range;
	
	public Spell(){}

	public Spell(int level, String name, Range range, AreaOfEffect area,
			CharacterSheet characterSheet) {
		this.level = level;
		this.name = name;
		this.range = range;
		this.area = area;
		this.characterSheet = characterSheet;
	}

	public Spell(int level, String name) {
		this.level = level;
		this.name = name;
	}

	public Spell(int level) {
		this.level = level;
	}

	public AreaOfEffect getArea() {
		return area;
	}

	public CharacterSheet getCharacterSheet() {
		return characterSheet;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public Range getRange() {
		return range;
	}

	public void setArea(AreaOfEffect area) {
		this.area = area;
	}

	public void setCharacterSheet(CharacterSheet characterSheet) {
		this.characterSheet = characterSheet;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRange(Range range) {
		this.range = range;
	}
	
}
