package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;

public class CavalierTemplate extends CharacterTemplate {

	@Override
	public void apply(CharacterClassModel cs) {
		
		cs.setHitDie(10);
		cs.setSaves(true, false, false);
		cs.setSkillsPerLevel(4);
		cs.setAtkProgression(1);
		
		updateSpells(cs);
		
		cs.addClassPowers(powers);

	}

	@Override
	void setupPowers() {
		addPowers(1, "Challenge 1/day, mount, order, tactician");
		addPowers(2, "Order ability");
		addPowers(3, "Cavalier's Charge");
		addPowers(4, "Challenge 2/day, expert trainer");
		addPowers(5, "Banner");
		addPowers(6, "Bonus feat");
		addPowers(7, "Challenge 3/day");
		addPowers(8, "Order ability");
		addPowers(9, "Greater tactician");
		addPowers(10, "Challenge 4/day");
		addPowers(11, "Mighty charge");
		addPowers(12, "Bonus feat, demanding challenge");
		addPowers(13, "Challenge 5/day");
		addPowers(14, "Greater banner");
		addPowers(15, "Order ability");
		addPowers(16, "Challenge 6/day");
		addPowers(17, "Master tactician");
		addPowers(18, "Bonus feat");
		addPowers(19, "Challenge 7/day");
		addPowers(20, "Supreme charge");
	}

	@Override
	public void updateSpells(CharacterClassModel cs) {
		cs.clearSpells();
	}

}
