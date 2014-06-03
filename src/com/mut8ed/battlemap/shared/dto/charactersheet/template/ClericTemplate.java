package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class ClericTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(8);
		cs.setSaves(true, false, true);
		cs.setSkillsPerLevel(2);
		cs.setAtkProgression(.75);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Aura, channel energy 1d6, domains, orisons, spontaneous casting");
//		addPowers(2, "-");
		addPowers(3, "Channel energy 2d6");
//		addPowers(4, "-");
		addPowers(5, "Channel energy 3d6");
//		addPowers(6, "-");
		addPowers(7, "Channel energy 4d6");
//		addPowers(8, "-");
		addPowers(9, "Channel energy 5d6");
//		addPowers(10, "-");
		addPowers(11, "Channel energy 6d6");
//		addPowers(12, "-");
		addPowers(13, "Channel energy 7d6");
//		addPowers(14, "-");
		addPowers(15, "Channel energy 8d6");
//		addPowers(16, "-");
		addPowers(17, "Channel energy 9d6");
//		addPowers(18, "-");
		addPowers(19, "Channel energy 10d6");
//		addPowers(20, "-");
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.WIS;
		
		switch (cs.getLevel()){
		case 1: cs.setupSpellSlots("3	1+1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 2: cs.setupSpellSlots("4	2+1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 3: cs.setupSpellSlots("4	2+1	1+1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 4: cs.setupSpellSlots("4	3+1	2+1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 5: cs.setupSpellSlots("4	3+1	2+1	1+1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 6: cs.setupSpellSlots("4	3+1	3+1	2+1	Ñ	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 7: cs.setupSpellSlots("4	4+1	3+1	2+1	1+1	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 8: cs.setupSpellSlots("4	4+1	3+1	3+1	2+1	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 9: cs.setupSpellSlots("4	4+1	4+1	3+1	2+1	1+1	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 10: cs.setupSpellSlots("4	4+1	4+1	3+1	3+1	2+1	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 11: cs.setupSpellSlots("4	4+1	4+1	4+1	3+1	2+1	1+1	Ñ	Ñ	Ñ", castingStat); break;
		case 12: cs.setupSpellSlots("4	4+1	4+1	4+1	3+1	3+1	2+1	Ñ	Ñ	Ñ", castingStat); break;
		case 13: cs.setupSpellSlots("4	4+1	4+1	4+1	4+1	3+1	2+1	1+1	Ñ	Ñ", castingStat); break;
		case 14: cs.setupSpellSlots("4	4+1	4+1	4+1	4+1	3+1	3+1	2+1	Ñ	Ñ", castingStat); break;
		case 15: cs.setupSpellSlots("4	4+1	4+1	4+1	4+1	4+1	3+1	2+1	1+1	Ñ", castingStat); break;
		case 16: cs.setupSpellSlots("4	4+1	4+1	4+1	4+1	4+1	3+1	3+1	2+1	Ñ", castingStat); break;
		case 17: cs.setupSpellSlots("4	4+1	4+1	4+1	4+1	4+1	4+1	3+1	2+1	1+1", castingStat); break;
		case 18: cs.setupSpellSlots("4	4+1	4+1	4+1	4+1	4+1	4+1	3+1	3+1	2+1", castingStat); break;
		case 19: cs.setupSpellSlots("4	4+1	4+1	4+1	4+1	4+1	4+1	4+1	3+1	3+1", castingStat); break;
		case 20: cs.setupSpellSlots("4	4+1	4+1	4+1	4+1	4+1	4+1	4+1	4+1	4+1", castingStat); break;
		}

	}		



}
