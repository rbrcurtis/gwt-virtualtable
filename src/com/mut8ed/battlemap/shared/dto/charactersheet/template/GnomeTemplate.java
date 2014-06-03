package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;

public class GnomeTemplate extends RaceTemplate {

	@Override
	public void apply(CharacterSheet cs) {
		
		cs.getScore(CombatStatType.MOVE).setBase(20);		
		cs.addModifier(new Modifier("gnome-ac", 1, "racial", CombatStatType.AC));
		cs.addModifier(new Modifier("gnome-atk", 1, "racial", CombatStatType.BASEATTACK));
		cs.addModifier(new Modifier("gnome-cmb", -1, "racial", CombatStatType.CMB));
		cs.addModifier(new Modifier("gnome-cmd", -1, "racial", CombatStatType.CMD));
		cs.addModifier(new Modifier("gnome-perception", 2, "racial", Skill.Type.PERCEPTION));
		cs.addModifier(new Modifier("gnome-crafty", 2, "racial", Skill.Type.CRAFT));
		cs.addModifier(new Modifier("gnome-professional", 2, "racial", Skill.Type.PROFESSION));
		
		cs.addRacePowers(powers);
	}
	
	@Override
	public void remove(CharacterSheet cs){
		cs.getScore(CombatStatType.MOVE).setBase(30);
		cs.removeModifier("gnome-ac");
		cs.removeModifier("gnome-atk");
		cs.removeModifier("gnome-cmb");
		cs.removeModifier("gnome-cmd");
		cs.removeModifier("gnome-perception");
		cs.removeModifier("gnome-crafty");
		cs.removeModifier("gnome-professional");
		
		cs.removeClassPower("dancinglights");
		cs.removeClassPower("ghostsound");
		cs.removeClassPower("prestidigitation");
		cs.removeClassPower("speakwithanimals");
	}

	@Override
	void setupPowers() {
		addPowers(1, 
				"low light vision," +
						"+4 AC vs giants," +
						"+1 illusion DC," +
						"+1 atk vs reptiles and goblins," +
						"+2 save vs illusions," +
						"gnomish weapon familiarity"
				);
		addPowers(11, "dancing lights 1/day");
		addPowers(11, "ghost sound 1/day");
		addPowers(11, "prestidigitation 1/day");
		addPowers(11, "speak with animals 1/day");
	}

	@Override
	public void updateSpells(CharacterSheet cs) {
//		cs.clearSpells();
	}

}
