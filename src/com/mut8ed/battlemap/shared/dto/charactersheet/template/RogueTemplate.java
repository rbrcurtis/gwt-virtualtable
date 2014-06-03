package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;

public class RogueTemplate extends CharacterTemplate {

	@Override
	public void apply(CharacterClassModel cs) {
		
		cs.setHitDie(8);
		cs.setSaves(false, true, false);
		cs.setSkillsPerLevel(8);
		cs.setAtkProgression(.75);
		
		updateSpells(cs);
		
		cs.addClassPowers(powers);

	}

	@Override
	void setupPowers() {
		addPowers(1, "Sneak attack +1d6, trapfinding");
		addPowers(2, "Evasion, rogue talent");
		addPowers(3, "Sneak attack +2d6, trap sense +1");
		addPowers(4, "Rogue talent, uncanny dodge");
		addPowers(5, "Sneak attack +3d6");
		addPowers(6, "Rogue talent, trap sense +2");
		addPowers(7, "Sneak attack +4d6");
		addPowers(8, "Improved uncanny dodge, rogue talent");
		addPowers(9, "Sneak attack +5d6, trap sense +3");
		addPowers(10, "Advanced talents, rogue talent");
		addPowers(11, "Sneak attack +6d6");
		addPowers(12, "Rogue talent, trap sense +4");
		addPowers(13, "Sneak attack +7d6");
		addPowers(14, "Rogue talent");
		addPowers(15, "Sneak attack +8d6, trap sense +5");
		addPowers(16, "Rogue talent");
		addPowers(17, "Sneak attack +9d6");
		addPowers(18, "Rogue talent, trap sense +6");
		addPowers(19, "Sneak attack +10d6");
		addPowers(20, "Master strike, rogue talent");
	}

	@Override
	public void updateSpells(CharacterClassModel cs) {
		cs.clearSpells();
	}

}
