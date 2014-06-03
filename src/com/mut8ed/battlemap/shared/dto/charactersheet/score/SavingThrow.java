package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;


public class SavingThrow extends StatModdableScore {
	private static final long serialVersionUID = 1L;

	public SavingThrow(){
		super();
	}

	public SavingThrow(Type type, CharacterSheet cs){
		super(type, 0, cs);
	}

	public SavingThrow(Type type, CharacterSheet characterSheet, Ability.Type... ability) {
		super(type, 0, characterSheet, ability);
	}

	@Override
	public int getAdjusted() {
		getBase();
		return super.getAdjusted();
	}


	@Override
	public int getBase() {
		base = 0;
		for (CharacterClassModel ccm : cs.getClassModels()){
			
			boolean goodSave = false;
			
			switch ((Type)type){
			case FORT: goodSave = ccm.getGoodSaveFort(); break;
			case REFLEX: goodSave = ccm.getGoodSaveReflex(); break;
			case WILL: goodSave = ccm.getGoodSaveWill(); break;
			}
			
			if (goodSave){
				base += (int)Math.floor(ccm.getLevel()/2)+2;
			} else {
				base += (int)Math.floor(ccm.getLevel()/3);
			}
		}
		return base;
	}

	public enum Type {
		FORT,
		REFLEX,
		WILL
	}

	@Override
	public void addWatcher(Watcher watcher) {
		super.addWatcher(watcher);
		cs.addWatcher(watcher);
	}

	
}

