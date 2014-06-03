package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Feat;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;

public class HalfElfTemplate extends RaceTemplate {

	@Override
	public void apply(CharacterSheet cs) {
		
		cs.addModifier(new Modifier("elf-perception", 2, "racial", Skill.Type.PERCEPTION));
		cs.addFeat(new Feat(0, "race", "skill focus ***"));
		
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
				"elf blood" +
				"immune [sleep]," +
				"+2 save vs enchantment"
				);
	}

	@Override
	public void updateSpells(CharacterSheet cs) {
//		cs.clearSpells();
	}

}
