package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class MagusTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(8);
		cs.setSaves(false, true, true);
		cs.setSkillsPerLevel(2);
		cs.setAtkProgression(.75);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Arcane pool, cantrips, spell combat");
		addPowers(2, "Spellstrike");
		addPowers(3, "Magus arcana");
		addPowers(4, "Spell recall");
		addPowers(5, "Bonus feat");
		addPowers(6, "Magus arcana");
		addPowers(7, "Knowledge pool, medium armor");
		addPowers(8, "Improved spell combat");
		addPowers(9, "Magus arcana");
		addPowers(10, "Fighter training");
		addPowers(11, "Bonus feat, improved spell recall");
		addPowers(12, "Magus arcana");
		addPowers(13, "Heavy armor");
		addPowers(14, "Greater spell combat");
		addPowers(15, "Magus arcana");
		addPowers(16, "Counterstrike");
		addPowers(17, "Bonus feat");
		addPowers(18, "Magus arcana");
		addPowers(19, "Greater spell access");
		addPowers(20, "True magus");		
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.INT;
		
		switch (cs.getLevel()){
		case 1: cs.setupSpellSlots("*	1	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 2: cs.setupSpellSlots("*	2	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 3: cs.setupSpellSlots("*	3	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 4: cs.setupSpellSlots("*	3	1	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 5: cs.setupSpellSlots("*	4	2	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 6: cs.setupSpellSlots("*	4	3	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 7: cs.setupSpellSlots("*	4	3	1	Ñ	Ñ	Ñ", castingStat); break;
		case 8: cs.setupSpellSlots("*	4	4	2	Ñ	Ñ	Ñ", castingStat); break;
		case 9: cs.setupSpellSlots("*	5	4	3	Ñ	Ñ	Ñ", castingStat); break;
		case 10: cs.setupSpellSlots("*	5	4	3	1	Ñ	Ñ", castingStat); break;
		case 11: cs.setupSpellSlots("*	5	4	4	2	Ñ	Ñ", castingStat); break;
		case 12: cs.setupSpellSlots("*	5	5	4	3	Ñ	Ñ", castingStat); break;
		case 13: cs.setupSpellSlots("*	5	5	4	3	1	Ñ", castingStat); break;
		case 14: cs.setupSpellSlots("*	5	5	4	4	2	Ñ", castingStat); break;
		case 15: cs.setupSpellSlots("*	5	5	5	4	3	Ñ", castingStat); break;
		case 16: cs.setupSpellSlots("*	5	5	5	4	3	1", castingStat); break;
		case 17: cs.setupSpellSlots("*	5	5	5	4	4	2", castingStat); break;
		case 18: cs.setupSpellSlots("*	5	5	5	5	4	3", castingStat); break;
		case 19: cs.setupSpellSlots("*	5	5	5	5	5	4", castingStat); break;
		case 20: cs.setupSpellSlots("*	5	5	5	5	5	5", castingStat); break;
		}

	}


}
