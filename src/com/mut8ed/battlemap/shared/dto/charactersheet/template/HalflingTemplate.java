package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.SavingThrow;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;

public class HalflingTemplate extends RaceTemplate {

	@Override
	public void apply(CharacterSheet cs) {
		
		cs.getScore(CombatStatType.MOVE).setBase(20);		
		cs.addModifier(new Modifier("halfling-ac", 1, "racial", CombatStatType.AC));
		cs.addModifier(new Modifier("halfling-atk", 1, "racial", CombatStatType.BASEATTACK));
		cs.addModifier(new Modifier("halfling-cmb", -1, "racial", CombatStatType.CMB));
		cs.addModifier(new Modifier("halfling-cmd", -1, "racial", CombatStatType.CMD));
		cs.addModifier(new Modifier("halfling-perception", 2, "racial", Skill.Type.PERCEPTION));
		cs.addModifier(new Modifier("halfling-acrobat", 2, "racial", Skill.Type.ACROBATICS));
		cs.addModifier(new Modifier("halfling-climber", 2, "racial", Skill.Type.CLIMB));

		cs.addModifier(new Modifier("halfling-fort", 1, "racial", SavingThrow.Type.FORT));
		cs.addModifier(new Modifier("halfling-reflex", 1, "racial", SavingThrow.Type.REFLEX));
		cs.addModifier(new Modifier("halfling-will", 1, "racial", SavingThrow.Type.WILL));
	
		cs.addRacePowers(powers);
	}
	
	@Override
	public void remove(CharacterSheet cs){
		cs.getScore(CombatStatType.MOVE).setBase(30);
		cs.removeModifier("halfling-ac");
		cs.removeModifier("halfling-atk");
		cs.removeModifier("halfling-cmb");
		cs.removeModifier("halfling-cmd");
		cs.removeModifier("halfling-perception");
		cs.removeModifier("halfling-acrobat");
		cs.removeModifier("halfling-climber");
		
		cs.removeModifier("halfling-fort");
		cs.removeModifier("halfling-reflex");
		cs.removeModifier("halfling-will");
	}

	@Override
	void setupPowers() {
		addPowers(1, 
				"low light vision," +
						"+2 save vs fear," +
						"halfling weapon familiarity"
				);
	}

	@Override
	public void updateSpells(CharacterSheet cs) {
//		cs.clearSpells();
	}

}
