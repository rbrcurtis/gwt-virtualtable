package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class SummonerTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(8);
		cs.setSaves(false, false, true);
		cs.setSkillsPerLevel(2);
		cs.setAtkProgression(.75);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Cantrips, Eidolon, Life link, Summon monster I");
		addPowers(2, "Bond senses");
		addPowers(3, "Summon monster II");
		addPowers(4, "Shield ally");
		addPowers(5, "Summon monster III");
		addPowers(6, "+5");
		addPowers(7, "Summon monster IV");
		addPowers(8, "Transposition");
		addPowers(9, "Summon monster V");
		addPowers(10, "Aspect");
		addPowers(11, "Summon monster VI");
		addPowers(12, "Greater shield ally");
		addPowers(13, "Summon monster VII");
		addPowers(14, "Life bond");
		addPowers(15, "Summon monster VIII");
		addPowers(16, "Merge forms");
		addPowers(17, "Summon monster IX");
		addPowers(18, "Greater aspect");
		addPowers(19, "Gate");
		addPowers(20, "Twin eidolon");		
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
