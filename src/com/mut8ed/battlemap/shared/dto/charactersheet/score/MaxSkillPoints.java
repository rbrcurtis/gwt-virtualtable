package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;

public class MaxSkillPoints extends Score {
	private static final long serialVersionUID = 1L;

	public MaxSkillPoints(){}
	
	public MaxSkillPoints(CharacterSheet characterSheet, int skillsPerLevel) {
		super(Misc.MAX_SKILLS, skillsPerLevel, characterSheet);
	}

	@Override
	public int getAdjusted() {
		
		int intMod = (int)Math.floor(((cs.getAbility(Ability.Type.INT).getBase()*1.0)-10)/2);
		int maxSkills = 0;
		for (CharacterClassModel ccm : cs.getClassModels()){
			maxSkills+=((ccm.getSkillsPerLevel()+intMod)*ccm.getLevel());
		}
		
		return maxSkills;
	}

	@Override
	public void addWatcher(Watcher watcher) {
		cs.getAbility(Ability.Type.INT).addWatcher(watcher);
		cs.addWatcher(watcher);
		super.addWatcher(watcher);
	}

}
