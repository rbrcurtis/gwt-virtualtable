package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;

public class HalfOrcTemplate extends RaceTemplate {

	@Override
	public void apply(CharacterSheet cs) {
		
		cs.addModifier(new Modifier("orc-scary", 2, "racial", Skill.Type.INTIMIDATE));
		
		cs.addRacePowers(powers);
	}
	
	@Override
	public void remove(CharacterSheet cs){
		cs.removeModifier("orc-scary");
	}

	@Override
	void setupPowers() {
		addPowers(1, 
				"darkvision 60," +
				"orc blood" +
				"orc ferocity," +
				"orc weapon familiarity"
				);
	}

	@Override
	public void updateSpells(CharacterSheet cs) {
//		cs.clearSpells();
	}

}
