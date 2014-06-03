package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;

public class AlchemistTemplate extends CharacterTemplate {

	@Override	
	public void apply(CharacterClassModel cs) {

		cs.setHitDie(8);
		cs.setSaves(true, true, false);
		cs.setSkillsPerLevel(4);
		cs.setAtkProgression(.75);

		updateSpells(cs);

		cs.addClassPowers(powers);
	}


	@Override
	void setupPowers() {
		addPowers(1, "Alchemy, bomb 1d6, brew potion, mutagen, throw anything");
		addPowers(2, "Discovery, poison resistance +2, poison use");
		addPowers(3, "Bomb 2d6, swift alchemy");
		addPowers(4, "Discovery");
		addPowers(5, "Bomb 3d6, poison resistance +4");
		addPowers(6, "Discovery, swift poisoning");
		addPowers(7, "Bomb 4d6");
		addPowers(8, "Discovery, poison resistance +6");
		addPowers(9, "Bomb 5d6");
		addPowers(10, "Discovery, poison immunity");
		addPowers(11, "Bomb 6d6");
		addPowers(12, "Discovery");
		addPowers(13, "Bomb 7d6");
		addPowers(14, "Discovery, persistent mutagen");
		addPowers(15, "Bomb 8d6");
		addPowers(16, "Discovery");
		addPowers(17, "Bomb 9d6");
		addPowers(18, "Discovery, instant alchemy");
		addPowers(19, "Bomb 10d6");
		addPowers(20, "Discovery, Discovery, Grand Discovery");		
	}


	@Override
	public void updateSpells(CharacterClassModel ccm) {

		Ability.Type castingStat = Ability.Type.INT;
		
		switch (ccm.getLevel()){
		case 1: ccm.setupSpellSlots("-	1	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 2: ccm.setupSpellSlots("-	2	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 3: ccm.setupSpellSlots("-	3	Ñ	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 4: ccm.setupSpellSlots("-	3	1	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 5: ccm.setupSpellSlots("-	4	2	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 6: ccm.setupSpellSlots("-	4	3	Ñ	Ñ	Ñ	Ñ", castingStat); break;
		case 7: ccm.setupSpellSlots("-	4	3	1	Ñ	Ñ	Ñ", castingStat); break;
		case 8: ccm.setupSpellSlots("-	4	4	2	Ñ	Ñ	Ñ", castingStat); break;
		case 9: ccm.setupSpellSlots("-	5	4	3	Ñ	Ñ	Ñ", castingStat); break;
		case 10: ccm.setupSpellSlots("-	5	4	3	1	Ñ	Ñ", castingStat); break;
		case 11: ccm.setupSpellSlots("-	5	4	4	2	Ñ	Ñ", castingStat); break;
		case 12: ccm.setupSpellSlots("-	5	5	4	3	Ñ	Ñ", castingStat); break;
		case 13: ccm.setupSpellSlots("-	5	5	4	3	1	Ñ", castingStat); break;
		case 14: ccm.setupSpellSlots("-	5	5	4	4	2	Ñ", castingStat); break;
		case 15: ccm.setupSpellSlots("-	5	5	5	4	3	Ñ", castingStat); break;
		case 16: ccm.setupSpellSlots("-	5	5	5	4	3	1", castingStat); break;
		case 17: ccm.setupSpellSlots("-	5	5	5	4	4	2", castingStat); break;
		case 18: ccm.setupSpellSlots("-	5	5	5	5	4	3", castingStat); break;
		case 19: ccm.setupSpellSlots("-	5	5	5	5	5	4", castingStat); break;
		case 20: ccm.setupSpellSlots("-	5	5	5	5	5	5", castingStat); break;
		}

	}


}
