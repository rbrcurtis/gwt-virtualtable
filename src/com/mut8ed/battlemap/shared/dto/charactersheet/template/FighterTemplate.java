package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;

public class FighterTemplate extends CharacterTemplate {

	@Override
	public void apply(CharacterClassModel cs) {
		
		cs.setHitDie(10);
		cs.setSaves(true, false, false);
		cs.setSkillsPerLevel(2);
		cs.setAtkProgression(1);
		
		updateSpells(cs);
		
		cs.addClassPowers(powers);

	}

	@Override
	void setupPowers() {
		addPowers(1, "Bonus feat");
		addPowers(2, "Bonus feat, bravery");
		addPowers(3, "Armor training 1");
		addPowers(4, "Bonus feat");
		addPowers(5, "Weapon training");
		addPowers(6, "Bonus feat");
		addPowers(7, "Armor training 2");
		addPowers(8, "Bonus feat");
		addPowers(9, "Weapon training");
		addPowers(10, "Bonus feat");
		addPowers(11, "Armor training 3");
		addPowers(12, "Bonus feat");
		addPowers(13, "Weapon training");
		addPowers(14, "Bonus feat");
		addPowers(15, "Armor training 4");
		addPowers(16, "Bonus feat");
		addPowers(17, "Weapon training");
		addPowers(18, "Bonus feat");
		addPowers(19, "Armor mastery");
		addPowers(20, "Bonus feat, Weapon mastery");
	}

	@Override
	public void updateSpells(CharacterClassModel cs) {
		cs.clearSpells();
	}

}
