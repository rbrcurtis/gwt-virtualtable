package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;

public class DwarfTemplate extends RaceTemplate {

	@Override
	public void apply(CharacterSheet cs) {
		cs.getScore(CombatStatType.MOVE).setBase(20);
		
		cs.addRacePowers(powers);
	}
	
	@Override
	public void remove(CharacterSheet cs){
		cs.getScore(CombatStatType.MOVE).setBase(30);
	}

	@Override
	void setupPowers() {
		addPowers(1, 
				"darkvision 60, " +
				"+4 AC vs Giants, " +
				"+2 appraise [gemstones], " +
				"+1 atk vs orc and goblins, " +
				"+2 saves vs poisons, " +
				"+2 saves vs spells, " +
				"+4 CMD vs bull rush and trip, " +
				"+2 perception [stonework]," +
				"dwarven weapon familiarity"
				);
	}

	@Override
	public void updateSpells(CharacterSheet cs) {
//		cs.clearSpells();
	}

}
