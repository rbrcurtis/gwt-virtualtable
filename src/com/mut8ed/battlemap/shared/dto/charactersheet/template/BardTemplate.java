package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class BardTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(8);
		cs.setSaves(false, true, true);
		cs.setSkillsPerLevel(6);
		cs.setAtkProgression(.75);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Bardic knowledge, bardic performance, cantrips, countersong, distraction, fascinate, inspire courage +1");
		addPowers(2, "Versatile performance, well-versed");
		addPowers(3, "Inspire competence +2");
		addPowers(4, "");
		addPowers(6, "Suggestion, Versatile performance");
		addPowers(7, "Inspire competence +3");
		addPowers(8, "Dirge of Doom");
		addPowers(9, "Inspire greatness");
		addPowers(10, "Jack-of-all-Trades, Versatile performance");
		addPowers(11, "Inspire competence +4, inspire courage +3, lore master 2/day");
		addPowers(12, "Soothing performance");
		addPowers(13, "");
		addPowers(15, "Inspire competence +5, inspire heroics");
		addPowers(16, "");
		addPowers(18, "Mass suggestion, Versatile performance");
		addPowers(19, "Inspire competence +6");
		addPowers(20, "Deadly performance");		
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.CHA;
		
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
