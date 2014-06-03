package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class PaladinTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(10);
		cs.setSaves(true, false, true);
		cs.setSkillsPerLevel(2);
		cs.setAtkProgression(1);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Aura of Good, Detect Evil, Smite Evil 1/day");
		addPowers(2, "Divine Grace, Lay On Hands");
		addPowers(3, "Aura of Courage, Divine Health, Mercy");
		addPowers(4, "Channel Positive Energy, Smite Evil 2/day");
		addPowers(5, "Divine Bond");
		addPowers(6, "Mercy");
		addPowers(7, "Smite Evil 3/day");
		addPowers(8, "Aura of Resolve");
		addPowers(9, "Mercy");
		addPowers(10, "Smite Evil 4/day");
		addPowers(11, "Aura of Justice");
		addPowers(12, "Mercy");
		addPowers(13, "Smite Evil 5/day");
		addPowers(14, "Aura of Faith");
		addPowers(15, "Mercy");
		addPowers(16, "Smite Evil 6/day");
		addPowers(17, "Aura of Righteousness");
		addPowers(18, "Mercy");
		addPowers(19, "Smite Evil 7/day");
		addPowers(20, "Holy Champion");
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
		case 20: cs.setupSpellSlots("Ñ	4	4	3	3", castingStat); break;		}

	}		



}
