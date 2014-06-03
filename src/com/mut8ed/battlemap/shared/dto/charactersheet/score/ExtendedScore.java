package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;


/**
 * class to extend attack score with melee, ranged, etc
 * @author rcurtis
 *
 */
@SuppressWarnings("rawtypes")
public class ExtendedScore extends StatModdableScore {
	private static final long serialVersionUID = 1L;
	
	Score baseScore;
	
	public ExtendedScore(){	}
	
	public ExtendedScore(Enum type, AttackScore baseScore, CharacterSheet cs, Ability.Type... abilities){
		super(type, 0, cs, abilities);
		this.baseScore = baseScore;
	}

	@Override
	public int getAdjusted() {
		int adjusted = super.getAdjusted();
		adjusted+=baseScore.getAdjusted();
		
		return adjusted;
	}

	@Override
	public void addWatcher(Watcher watcher) {
		super.addWatcher(watcher);
		baseScore.addWatcher(watcher);
	}
	
	
	
	

}
