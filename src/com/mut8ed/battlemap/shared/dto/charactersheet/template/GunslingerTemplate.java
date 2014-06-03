package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;

public class GunslingerTemplate extends CharacterTemplate {

	@Override
	public void apply(CharacterClassModel cs) {
		
		cs.setHitDie(10);
		cs.setSaves(true, true, false);
		cs.setSkillsPerLevel(4);
		cs.setAtkProgression(1);
		
		updateSpells(cs);
		
		cs.addClassPowers(powers);

	}

	@Override
	void setupPowers() {
		addPowers(1, "Deeds, grit, gunsmith");
		addPowers(2, "Nimble +1");
		addPowers(3, "Deeds");
		addPowers(4, "Bonus feat");
		addPowers(5, "Gun training 1");
		addPowers(6, "Nimble +2");
		addPowers(7, "Deeds");
		addPowers(8, "Bonus feat");
		addPowers(9, "Gun training 2");
		addPowers(10, "Nimble +3");
		addPowers(11, "Deeds");
		addPowers(12, "Bonus feat");
		addPowers(13, "Gun training 3");
		addPowers(14, "Nimble +4");
		addPowers(15, "Deeds");
		addPowers(16, "Bonus feat");
		addPowers(17, "Gun training 4");
		addPowers(18, "Nimble +5");
		addPowers(19, "Deeds");
		addPowers(20, "Bonus feat, true grit");
	}

	@Override
	public void updateSpells(CharacterClassModel cs) {
		cs.clearSpells();
	}

}
