package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class RangerTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(10);
		cs.setSaves(true, true, false);
		cs.setSkillsPerLevel(6);
		cs.setAtkProgression(1);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "favored enemy, track, wild empathy");
		addPowers(2, "Combat style feat");
		addPowers(3, "Endurance, favored terrain");
		addPowers(4, "Hunter's bond");
		addPowers(5, "favored enemy");
		addPowers(6, "Combat style feat");
		addPowers(7, "Woodland stride");
		addPowers(8, "Swift tracker, favored terrain");
		addPowers(9, "Evasion");
		addPowers(10, "favored enemy, combat style feat");
		addPowers(11, "Quarry");
		addPowers(12, "Camouflage");
		addPowers(13, "favored terrain");
		addPowers(14, "Combat style feat");
		addPowers(15, "favored enemy");
		addPowers(16, "Improved evasion");
		addPowers(17, "Hide in plain sight");
		addPowers(18, "favored terrain, combat style feat");
		addPowers(19, "Improved quarry");
		addPowers(20, "favored enemy, master hunter");
	}


	@Override
	public void updateSpells(CharacterClassModel cs) {

		Ability.Type castingStat = Ability.Type.WIS;
		
		switch (cs.getLevel()){
		case 1: cs.setupSpellSlots("Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 2: cs.setupSpellSlots("Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 3: cs.setupSpellSlots("Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 4: cs.setupSpellSlots("Ñ	0	Ñ	Ñ	Ñ", castingStat); break;
		case 5: cs.setupSpellSlots("Ñ	1	Ñ	Ñ	Ñ", castingStat); break;
		case 6: cs.setupSpellSlots("Ñ	1	Ñ	Ñ	Ñ", castingStat); break;
		case 7: cs.setupSpellSlots("Ñ	1	0	Ñ	Ñ", castingStat); break;
		case 8: cs.setupSpellSlots("Ñ	1	1	Ñ	Ñ", castingStat); break;
		case 9: cs.setupSpellSlots("Ñ	2	1	Ñ	Ñ", castingStat); break;
		case 10: cs.setupSpellSlots("Ñ	2	1	0	Ñ", castingStat); break;
		case 11: cs.setupSpellSlots("Ñ	2	1	1	Ñ", castingStat); break;
		case 12: cs.setupSpellSlots("Ñ	2	2	1	Ñ", castingStat); break;
		case 13: cs.setupSpellSlots("Ñ	3	2	1	0", castingStat); break;
		case 14: cs.setupSpellSlots("Ñ	3	2	1	1", castingStat); break;
		case 15: cs.setupSpellSlots("Ñ	3	2	2	1", castingStat); break;
		case 16: cs.setupSpellSlots("Ñ	3	3	2	1", castingStat); break;
		case 17: cs.setupSpellSlots("Ñ	4	3	2	1", castingStat); break;
		case 18: cs.setupSpellSlots("Ñ	4	3	2	2", castingStat); break;
		case 19: cs.setupSpellSlots("Ñ	4	3	3	2", castingStat); break;
		case 20: cs.setupSpellSlots("Ñ	4	4	3	3", castingStat); break;		
		}

	}		



}
