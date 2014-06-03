package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Feat;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Misc;

public class HumanTemplate extends RaceTemplate {

	@Override
	public void apply(CharacterSheet cs) {
		
		cs.getScore(CombatStatType.MOVE).setBase(30);
		
		cs.addModifier(new Modifier("human-skills", 1, "racial", Misc.MAX_SKILLS));
		
		cs.addFeat(new Feat(0, "race", "*** human bonus feat ***"));

		cs.addRacePowers(powers);
	}
	
	@Override
	public void remove(CharacterSheet cs){
		cs.removeModifier("human-skills");
	}

	@Override
	void setupPowers() {

	}

	@Override
	public void updateSpells(CharacterSheet cs) {
//		cs.clearSpells();
	}

}
