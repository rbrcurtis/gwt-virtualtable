package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;

public class MonkTemplate extends CharacterTemplate {

	@Override
	public void apply(CharacterClassModel cs) {
		
		cs.setHitDie(8);
		cs.setSaves(true, true, true);
		cs.setSkillsPerLevel(4);
		cs.setAtkProgression(1);
		
		updateSpells(cs);
		
		cs.addClassPowers(powers);

	}

	@Override
	void setupPowers() {
		addPowers(1, "Bonus feat, flurry of blows,stunning fist, unarmed strike");
		addPowers(2, "Bonus feat, Evasion");
		addPowers(3, "Fast movement, maneuver training, still mind");
		addPowers(4, "Ki pool (magic), slow fall 20");
		addPowers(5, "High jump, purity of body");
		addPowers(6, "Bonus feat, slow fall 30");
		addPowers(7, "Wholeness of body");
		addPowers(8, "Slow fall 40");
		addPowers(9, "Improved evasion");
		addPowers(10, "Bonus feat, ki pool (lawful),slow fall 50");
		addPowers(11, "Diamond body");
		addPowers(12, "Abundant step, slow fall 60");
		addPowers(13, "Diamond soul");
		addPowers(14, "Bonus feat, slow fall 70");
		addPowers(15, "Quivering palm");
		addPowers(16, "Ki pool (adamantine),slow fall 80");
		addPowers(17, "Timeless body,tongue of the sun and moon");
		addPowers(18, "Bonus feat, slow fall 90");
		addPowers(19, "Empty body");
		addPowers(20, "Perfect self,slow fall °");
	}

	@Override
	public void updateSpells(CharacterClassModel cs) {
		cs.clearSpells();
	}

}
