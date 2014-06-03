package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class OracleTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(8);
		cs.setSaves(false, false, true);
		cs.setSkillsPerLevel(4);
		cs.setAtkProgression(.75);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Mystery, oracle’s curse, orisons, Revelation");
		addPowers(2, "Mystery spell");
		addPowers(3, "Revelation");
		addPowers(4, "Mystery spell");
//		addPowers(5, " ");
		addPowers(6, "Mystery spell");
		addPowers(7, "Revelation");
		addPowers(8, "Mystery spell");
//		addPowers(9, " ");
		addPowers(10, "Mystery spell");
		addPowers(11, "Revelation");
		addPowers(12, "Mystery spell");
//		addPowers(13, " ");
		addPowers(14, "Mystery spell");
		addPowers(15, "Revelation");
		addPowers(16, "Mystery spell");
//		addPowers(17, " ");
		addPowers(18, "Mystery spell");
		addPowers(19, "Revelation");
		addPowers(20, "Final Revelation");
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.CHA;
		
		switch (cs.getLevel()){
		case 1: cs.setupSpellSlots("*	3	—	—	—	—	—	—	—	—", castingStat); break;
		case 2: cs.setupSpellSlots("*	4	—	—	—	—	—	—	—	—", castingStat); break;
		case 3: cs.setupSpellSlots("*	5	—	—	—	—	—	—	—	—", castingStat); break;
		case 4: cs.setupSpellSlots("*	6	3	—	—	—	—	—	—	—", castingStat); break;
		case 5: cs.setupSpellSlots("*	6	4	—	—	—	—	—	—	—", castingStat); break;
		case 6: cs.setupSpellSlots("*	6	5	3	—	—	—	—	—	—", castingStat); break;
		case 7: cs.setupSpellSlots("*	6	6	4	—	—	—	—	—	—", castingStat); break;
		case 8: cs.setupSpellSlots("*	6	6	5	3	—	—	—	—	—", castingStat); break;
		case 9: cs.setupSpellSlots("*	6	6	6	4	—	—	—	—	—", castingStat); break;
		case 10: cs.setupSpellSlots("*	6	6	6	5	3	—	—	—	—", castingStat); break;
		case 11: cs.setupSpellSlots("*	6	6	6	6	4	—	—	—	—", castingStat); break;
		case 12: cs.setupSpellSlots("*	6	6	6	6	5	3	—	—	—", castingStat); break;
		case 13: cs.setupSpellSlots("*	6	6	6	6	6	4	—	—	—", castingStat); break;
		case 14: cs.setupSpellSlots("*	6	6	6	6	6	5	3	—	—", castingStat); break;
		case 15: cs.setupSpellSlots("*	6	6	6	6	6	6	4	—	—", castingStat); break;
		case 16: cs.setupSpellSlots("*	6	6	6	6	6	6	5	3	—", castingStat); break;
		case 17: cs.setupSpellSlots("*	6	6	6	6	6	6	6	4	—", castingStat); break;
		case 18: cs.setupSpellSlots("*	6	6	6	6	6	6	6	5	3", castingStat); break;
		case 19: cs.setupSpellSlots("*	6	6	6	6	6	6	6	6	4", castingStat); break;
		case 20: cs.setupSpellSlots("*	6	6	6	6	6	6	6	6	6", castingStat); break;
		}

	}		



}
