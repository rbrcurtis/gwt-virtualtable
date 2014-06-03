package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class InquisitorTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(8);
		cs.setSaves(true, false, true);
		cs.setSkillsPerLevel(6);
		cs.setAtkProgression(.75);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Domain, judgment 1/day, monster lore, orisons, stern gaze");
		addPowers(2, "Cunning initiative, detect alignment, track");
		addPowers(3, "Solo tactics, teamwork feat");
		addPowers(4, "Judgment 2/day");
		addPowers(5, "Bane, discern lies");
		addPowers(6, "Teamwork feat");
		addPowers(7, "Judgment 3/day");
		addPowers(8, "Second judgment");
		addPowers(9, "Teamwork feat");
		addPowers(10, "Judgment 4/day");
		addPowers(11, "Stalwart");
		addPowers(12, "Greater bane, teamwork feat");
		addPowers(13, "Judgment 5/day");
		addPowers(14, "Exploit weakness");
		addPowers(15, "Teamwork feat");
		addPowers(16, "Judgment 6/day, third judgment");
		addPowers(17, "Slayer");
		addPowers(18, "Teamwork feat");
		addPowers(19, "Judgment 7/day");
		addPowers(20, "True judgment");
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.WIS;
		
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
