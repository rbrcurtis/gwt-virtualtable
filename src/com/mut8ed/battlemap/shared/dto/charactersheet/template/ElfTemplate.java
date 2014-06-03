package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;

public class ElfTemplate extends RaceTemplate {

	@Override
	public void apply(CharacterSheet cs) {
		
		cs.getScore(CombatStatType.MOVE).setBase(30);		
		cs.addModifier(new Modifier("elf-perception", 2, "racial", Skill.Type.PERCEPTION));

		cs.addRacePowers(powers);
	}
	
	@Override
	public void remove(CharacterSheet cs){
		cs.removeModifier("elf-perception");
	}

	@Override
	void setupPowers() {
		addPowers(1, 
				"low light vision," +
				"immune [sleep]," +
				"+2 save vs enchantment," +
				"+2 SR Penetration," +
				"+2 spellcraft to identify," +
				"elven weapon familiarity"
				);
	}

	@Override
	public void updateSpells(CharacterSheet cs) {
//		cs.clearSpells();
	}

}
