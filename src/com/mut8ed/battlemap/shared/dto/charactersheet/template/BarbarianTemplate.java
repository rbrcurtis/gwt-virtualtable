package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;

public class BarbarianTemplate extends CharacterTemplate {

	@Override
	public void apply(CharacterClassModel cs) {
		
		cs.setHitDie(12);
		cs.setSaves(true, false, false);
		cs.setSkillsPerLevel(6);
		cs.setAtkProgression(1);
		
		updateSpells(cs);
		
		cs.addClassPowers(powers);

	}

	@Override
	void setupPowers() {
		addPowers(1, "Fast movement, rage");
		addPowers(2, "rage power, uncanny dodge");
		addPowers(3, "Trap sense +1");
		addPowers(4, "rage power");
		addPowers(5, "Improved uncanny dodge");
		addPowers(6, "rage power, trap sense +2");
		addPowers(7, "Damage reduction 1/-");
		addPowers(8, "rage power");
		addPowers(9, "Trap sense +3");
		addPowers(10, "Damage reduction 2/-, rage power");
		addPowers(11, "Greater rage");
		addPowers(12, "rage power, trap sense +4");
		addPowers(13, "Damage reduction 3/-");
		addPowers(14, "Indomitable will, rage power");
		addPowers(15, "Trap sense +5");
		addPowers(16, "Damage reduction 4/-, rage power");
		addPowers(17, "Tireless rage");
		addPowers(18, "rage power, trap sense +6");
		addPowers(19, "Damage reduction 5/-");
		addPowers(20, "Mighty rage, rage power");
	}

	@Override
	public void updateSpells(CharacterClassModel cs) {
		cs.clearSpells();
	}

}
