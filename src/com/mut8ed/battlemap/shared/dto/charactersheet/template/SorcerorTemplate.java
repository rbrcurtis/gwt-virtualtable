package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class SorcerorTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(6);
		cs.setSaves(false, false, true);
		cs.setSkillsPerLevel(2);
		cs.setAtkProgression(.5);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Bloodline power, cantrips, eschew materials");
//		addPowers(2, " ");
		addPowers(3, "Bloodline power, bloodline spell");
//		addPowers(4, " ");
		addPowers(5, "Bloodline spell");
//		addPowers(6, " ");
		addPowers(7, "Bloodline feat, bloodline spell");
//		addPowers(8, " ");
		addPowers(9, "Bloodline power, bloodline spell");
//		addPowers(10, " ");
		addPowers(11, "Bloodline spell");
//		addPowers(12, " ");
		addPowers(13, "Bloodline feat, bloodline spell");
//		addPowers(14, " ");
		addPowers(15, "Bloodline power, bloodline spell");
//		addPowers(16, " ");
		addPowers(17, "Bloodline spell");
//		addPowers(18, " ");
		addPowers(19, "Bloodline feat, bloodline spell");
		addPowers(20, "Bloodline power");
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.CHA;
		
		switch (cs.getLevel()){
		case 1: cs.setupSpellSlots("*	3	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 2: cs.setupSpellSlots("*	4	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 3: cs.setupSpellSlots("*	5	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 4: cs.setupSpellSlots("*	6	3	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 5: cs.setupSpellSlots("*	6	4	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 6: cs.setupSpellSlots("*	6	5	3	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 7: cs.setupSpellSlots("*	6	6	4	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 8: cs.setupSpellSlots("*	6	6	5	3	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 9: cs.setupSpellSlots("*	6	6	6	4	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 10: cs.setupSpellSlots("*	6	6	6	5	3	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 11: cs.setupSpellSlots("*	6	6	6	6	4	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 12: cs.setupSpellSlots("*	6	6	6	6	5	3	Ñ	Ñ	Ñ", castingStat); break;
		case 13: cs.setupSpellSlots("*	6	6	6	6	6	4	Ñ	Ñ	Ñ", castingStat); break;
		case 14: cs.setupSpellSlots("*	6	6	6	6	6	5	3	Ñ	Ñ", castingStat); break;
		case 15: cs.setupSpellSlots("*	6	6	6	6	6	6	4	Ñ	Ñ", castingStat); break;
		case 16: cs.setupSpellSlots("*	6	6	6	6	6	6	5	3	Ñ", castingStat); break;
		case 17: cs.setupSpellSlots("*	6	6	6	6	6	6	6	4	Ñ", castingStat); break;
		case 18: cs.setupSpellSlots("*	6	6	6	6	6	6	6	5	3", castingStat); break;
		case 19: cs.setupSpellSlots("*	6	6	6	6	6	6	6	6	4", castingStat); break;
		case 20: cs.setupSpellSlots("*	6	6	6	6	6	6	6	6	6", castingStat); break;
		}

	}		



}
