package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;



public class Skill extends StatModdableScore {
	private static final long serialVersionUID = 1L;
	
	boolean classSkill = false;
	boolean untrained;

	public Skill(){
		super();
	}
	
	public Skill(Type type, int score, CharacterSheet cs, Ability.Type ability) {
		super(type, score, cs, ability);
	}

	public Skill(Type type, int score, CharacterSheet cs, boolean untrained, Ability.Type ability) {
		super(type, score, cs, ability);

		this.untrained = untrained;
	}
	
	public boolean isClassSkill() {
		return classSkill;
	}

	public boolean isUntrained() {
		return untrained;
	}
	
	public void setClassSkill(boolean classSkill) {
		this.classSkill = classSkill;
	}
	public void setUntrained(boolean classSkill) {
		this.untrained = classSkill;
	}

	public enum Type {
		ACROBATICS,
		APPRAISE,
		BLUFF,
		CLIMB,
		CRAFT,
		DIPLOMACY,
		DISABLE,
		DISGUISE,
		ESCAPE,
		FLY,
		HANDLE,
		HEAL,
		INTIMIDATE,
		K_ARCANA,
		K_DUNGEONEERING,
		K_ENGINEERING,
		K_GEOGRAPHY,
		K_HISTORY,
		K_LOCAL,
		K_NATURE,
		K_NOBILITY,
		K_PLANES,
		K_RELIGION,
		LINGUISTICS,
		PERCEPTION,
		PERFORM,
		PROFESSION,
		RIDE,
		SENSE,
		SLEIGHT,
		SPELLCRAFT,
		STEALTH,
		SURVIVAL,
		SWIM,
		USE_MAGIC_DEVICE
	}

	public Ability getAbilityScore() {
		try {
			return (Ability)moddingAbilities.values().toArray()[0];
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void setBase(int base) {
		int level = cs.getLevel();
		if (base>level){
			base = level;
			informWatchers();
		}
		super.setBase(base);
	}
	
}
