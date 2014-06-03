package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class DruidTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(8);
		cs.setSaves(true, false, true);
		cs.setSkillsPerLevel(4);
		cs.setAtkProgression(.75);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Nature bond, nature sense, orisons, wild empathy");
		addPowers(2, "Woodland stride");
		addPowers(3, "Trackless step");
		addPowers(4, "Resist nature's lure, wild shape (1/day)");
		addPowers(5, " ");
		addPowers(6, "Wild shape (2/day)");
		addPowers(7, " ");
		addPowers(8, "Wild shape (3/day)");
		addPowers(9, "Venom immunity");
		addPowers(10, "Wild shape (4/day)");
		addPowers(11, " ");
		addPowers(12, "Wild shape (5/day)");
		addPowers(13, "A thousand faces");
		addPowers(14, "Wild shape (6/day)");
		addPowers(15, "Timeless body");
		addPowers(16, "Wild shape (7/day)");
		addPowers(17, " ");
		addPowers(18, "Wild shape (8/day)");
		addPowers(19, " ");
		addPowers(20, "Wild shape (at will)");
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.WIS;
		
		switch (cs.getLevel()){
		case 1: cs.setupSpellSlots("3	1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 2: cs.setupSpellSlots("4	2	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 3: cs.setupSpellSlots("4	2	1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 4: cs.setupSpellSlots("4	3	2	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 5: cs.setupSpellSlots("4	3	2	1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 6: cs.setupSpellSlots("4	3	3	2	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 7: cs.setupSpellSlots("4	4	3	2	1	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 8: cs.setupSpellSlots("4	4	3	3	2	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 9: cs.setupSpellSlots("4	4	4	3	2	1	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 10: cs.setupSpellSlots("4	4	4	3	3	2	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 11: cs.setupSpellSlots("4	4	4	4	3	2	1	Ñ	Ñ	Ñ", castingStat); break;
		case 12: cs.setupSpellSlots("4	4	4	4	3	3	2	Ñ	Ñ	Ñ", castingStat); break;
		case 13: cs.setupSpellSlots("4	4	4	4	4	3	2	1	Ñ	Ñ", castingStat); break;
		case 14: cs.setupSpellSlots("4	4	4	4	4	3	3	2	Ñ	Ñ", castingStat); break;
		case 15: cs.setupSpellSlots("4	4	4	4	4	4	3	2	1	Ñ", castingStat); break;
		case 16: cs.setupSpellSlots("4	4	4	4	4	4	3	3	2	Ñ", castingStat); break;
		case 17: cs.setupSpellSlots("4	4	4	4	4	4	4	3	2	1", castingStat); break;
		case 18: cs.setupSpellSlots("4	4	4	4	4	4	4	3	3	2", castingStat); break;
		case 19: cs.setupSpellSlots("4	4	4	4	4	4	4	4	3	3", castingStat); break;
		case 20: cs.setupSpellSlots("4	4	4	4	4	4	4	4	4	4", castingStat); break;
		}

	}		

}
