package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;


public class Ability extends Score {
	private static final long serialVersionUID = 1L;

	public Ability() {
		super();
	}

	public Ability(Type type, int score, CharacterSheet cs) {
		super(type, score, cs);
	}

	public int getAbilityMod(){
		int adjusted = getAdjusted();
		return (int)Math.floor(((adjusted*1.0)-10)/2);
	}

	@Override
	public Type getType() {
		return (Type)super.getType();
	}

	public enum Type {
		STR,
		DEX,
		CON,
		INT,
		WIS,
		CHA
	}

	@Override
	public String getDamageBonus() {
		return getAbilityMod()+"";
	}

	@Override
	public String getAttackBonus() {
		return getAbilityMod()+"";
	}

	@Override
	public String toString() {
		return "Ability [type=" + type + ", base=" + base
				+ ", getAdjusted()=" + getAdjusted() + ", getAbilityMod()=" + getAbilityMod() + "]";
	}

	@Override
	public void informWatchers() {
		if (cs!=null && cs.getClassModels()!=null){
			System.out.println("updating spellSlots");
			for (CharacterClassModel ccm : cs.getClassModels()){
				ccm.getCharacterClass().getTemplate().updateSpells(ccm);
			}
			
		} else System.out.println("NOT updating spellSlots, cs="+cs);
		super.informWatchers();
	}

}
