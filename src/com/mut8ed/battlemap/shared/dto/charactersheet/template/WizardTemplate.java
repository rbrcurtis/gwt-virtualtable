package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class WizardTemplate extends CharacterTemplate {

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
		addPowers(1, "Arcane bond, arcane school, cantrips, Scribe Scroll");
//		addPowers(2, " ");
//		addPowers(3, " ");
//		addPowers(4, " ");
		addPowers(5, "Bonus feat");
//		addPowers(6, " ");
//		addPowers(7, " ");
//		addPowers(8, " ");
//		addPowers(9, " ");
		addPowers(10, "Bonus feat");
//		addPowers(11, " ");
//		addPowers(12, " ");
//		addPowers(13, " ");
//		addPowers(14, " ");
		addPowers(15, "Bonus feat");
//		addPowers(16, " ");
//		addPowers(17, " ");
//		addPowers(18, " ");
//		addPowers(19, " ");
		addPowers(20, "Bonus feat");
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.INT;
		
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
