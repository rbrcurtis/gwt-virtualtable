package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import java.util.HashMap;
import java.util.Map;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;


@SuppressWarnings("rawtypes")
public class StatModdableScore extends Score {
	private static final long serialVersionUID = 1L;
	
	protected Map<Ability.Type,Ability> moddingAbilities = new HashMap<Ability.Type, Ability>();

	public StatModdableScore(){
		super();
	}
	
	public StatModdableScore(Enum type, int score, CharacterSheet cs, Ability.Type...abilities) {
		super(type, score, cs);
		for (Ability.Type abilityType : abilities){
			Ability ability = (Ability)cs.getScore(abilityType);
			moddingAbilities.put(abilityType, ability);
		}
	}
	
	StatModdableScore(Enum type, int score) {
		super(type, score);
	}
	
	public StatModdableScore(Enum type, int score, CharacterSheet cs) {
		super(type, score, cs);
	}

	@Override
	public int getAdjusted() {
		int i = super.getAdjusted();

		for (Ability ability : moddingAbilities.values()){
			i+=ability.getAbilityMod();
		}
		return i;
	}

	@Override
	public void addWatcher(Watcher watcher) {
		super.addWatcher(watcher);
		for (Ability ability : moddingAbilities.values()){
			ability.addWatcher(watcher);
		}
	}

	public Map<Ability.Type, Ability> getModdingAbilities() {
		return moddingAbilities;
	}

	
}
