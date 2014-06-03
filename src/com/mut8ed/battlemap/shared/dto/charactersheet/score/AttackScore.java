package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability.Type;


@SuppressWarnings("rawtypes") 
public class AttackScore extends StatModdableScore {
	private static final long serialVersionUID = 1L;

	private Double tempMultiplier; //such as from a spell like Transformation

	public AttackScore(){
		super();
	}
	
	public AttackScore(Enum type, CharacterSheet cs){
			super(type, 0, cs);
	}

	public AttackScore(Enum type, CharacterSheet characterSheet, Ability.Type... ability) {
		super(type, 0, characterSheet, ability);
	}

	public AttackScore(Enum type, int base, CharacterSheet cs, Type[] abilities) {
		super(type, base, cs, abilities);
	}

	@Override
	public int getAdjusted() {
		this.base = 0;
		for (CharacterClassModel ccm : cs.getClassModels()){
			base+=(int)((ccm.getLevel()*ccm.getAtkProgression()));
		}
		return super.getAdjusted();
	}
	
	@Override
	public int getBase() {
		this.base = 0;
		for (CharacterClassModel ccm : cs.getClassModels()){
			base+=(int)(ccm.getLevel()*ccm.getAtkProgression());
		}
		return base;
	}

	public Double getTempMultiplier() {
		return tempMultiplier;
	}

	public void setTempMultiplier(Double tempMultiplier) {
		this.tempMultiplier = tempMultiplier;
		informWatchers();
	}

	@Override
	public String toString() {
		return "LevelDependentScore ["
				+ "tempMultiplier=" + tempMultiplier + ", getAdjusted()="
				+ getAdjusted() + "]";
	}

	@Override
	public void addWatcher(Watcher watcher) {
		if (cs!=null)cs.addWatcher(watcher);
		super.addWatcher(watcher);
	}
	
	
	
	

}
