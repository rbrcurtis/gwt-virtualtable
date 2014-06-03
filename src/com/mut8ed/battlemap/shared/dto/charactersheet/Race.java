package com.mut8ed.battlemap.shared.dto.charactersheet;

import com.mut8ed.battlemap.shared.dto.charactersheet.template.DwarfTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.ElfTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.GnomeTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.HalfElfTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.HalfOrcTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.HalflingTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.HumanTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.RaceTemplate;

public enum Race {

	DWARF,
	ELF,
	GNOME,
	HALFELF,
	HALFORC,
	HALFLING,
	HUMAN;

	public void applyTemplate(CharacterSheet cs){
		getTemplate().apply(cs);
	}

	public RaceTemplate getTemplate(){
		switch (this){
		case DWARF:return new DwarfTemplate();
		case ELF:return new ElfTemplate();
		case GNOME:return new GnomeTemplate();
		case HALFELF:return new HalfElfTemplate();
		case HALFORC:return new HalfOrcTemplate();
		case HALFLING:return new HalflingTemplate();
		case HUMAN:return new HumanTemplate();


		default: return null;
		}
	}

	public void updateLevel(CharacterSheet cs, int from, int to) {
		getTemplate().updateLevel(cs, from, to);
	}

	public void removeTemplate(CharacterSheet cs) {
		cs.clearRacePowers();
		getTemplate().remove(cs);
	}	
}
