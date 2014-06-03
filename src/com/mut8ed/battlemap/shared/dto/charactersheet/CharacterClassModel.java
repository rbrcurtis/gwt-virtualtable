package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Feat;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Power;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.SpellSlot;

public class CharacterClassModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private double atkProgression;
	private CharacterClass characterClass;
	private Map<String, Power> classPowers = new LinkedHashMap<String, Power>();
	private CharacterSheetV2 cs;
	private boolean goodSaveFort;
	private boolean goodSaveReflex;
	private boolean goodSaveWill;
	private int hitDie;
	private int level = 0;
	private int skillsPerLevel;
	private Map<Integer, List<SpellSlot>> spellSlots = new HashMap<Integer, List<SpellSlot>>();
	private Map<Integer, List<Spell>> knownSpells = new HashMap<Integer, List<Spell>>();
	
	public CharacterClassModel(){}

	public CharacterClassModel(CharacterClass characterClass, int level, CharacterSheetV2 cs) {
		this.characterClass = characterClass;
		this.level = level;
		this.cs = cs;
		characterClass.applyTemplate(this);
	}

	public void addClassFeat(Feat feat) {
		feat.setType("class-"+characterClass.name());
		cs.addFeat(feat);
	}

	public void addClassPower(String key, String value) {
		classPowers.put(key, new Power(value));
	}

	public void addClassPowers(Map<Integer, Map<String, String>> powers) {
		for (Integer level : powers.keySet()){
			if (level>getLevel())break;
			for (Entry<String, String> power : powers.get(level).entrySet()){
				if (power.getKey().contains("feat")){
					cs.addFeat(new Feat(level, characterClass.name(), power.getValue()));
				} else {
					classPowers.put(power.getKey(), new Power(power.getValue()));
				}
			}
		}
		cs.informWatchers();
	}
	
	public Map<Integer, List<Spell>> getKnownSpells() {
		return knownSpells;
	}

	public void clearSpells() {
		spellSlots.clear();
		cs.informWatchers();
	}

	public void clearSpellSlots(){
		spellSlots.clear();
		cs.informWatchers();
	}

	public double getAtkProgression() {
		return atkProgression;
	}

	public CharacterClass getCharacterClass() {
		return characterClass;
	}

	public Map<String, Power> getClassPowers() {
		return classPowers;
	}

	public boolean getGoodSaveFort() {
		return goodSaveFort;
	}

	public boolean getGoodSaveReflex() {
		return goodSaveReflex;
	}

	public boolean getGoodSaveWill() {
		return goodSaveWill;
	}

	public int getHitDie() {
		return hitDie;
	}

	public int getLevel() {
		return level;
	}

	public int getSkillsPerLevel(){
		return skillsPerLevel;
	}

	public Map<Integer, List<SpellSlot>> getSpellSlots() {
		return spellSlots;
	}

	public void removeClassPower(String key) {
		classPowers.remove(key);
		//feats will be removed by the level update in CS
	}

	public void setAtkProgression(double multiplier) {
		this.atkProgression = multiplier;
	}

	public void setCharacterClass(CharacterClass characterClass) {
		classPowers.clear();
		for (int i = cs.getFeats().size()-1 ; i>=0 ; i--){
			if (cs.getFeats().get(i).getType().contains(characterClass.name()))cs.getFeats().remove(i);
		}
		this.characterClass = characterClass;
		characterClass.applyTemplate(this);
		cs.informWatchers();
	}

	public void setClassPowers(Map<String, Power> classPowers) {
		this.classPowers = classPowers;
	}

	//		protected void clearClassFeats(String characterClass) {
	//			for (int i = feats.size()-1;i>=0;i--){
	//				if (feats.get(i).getType().equals(characterClass))feats.remove(i);
	//			}
	//		}


	public void setGoodSaveFort(boolean fort) {
		this.goodSaveFort = fort;
	}

	public void setGoodSaveReflex(boolean reflex) {
		this.goodSaveReflex = reflex;
	}

	public void setGoodSaveWill(boolean will) {
		this.goodSaveWill = will;
	}

	public void setHitDie(int hitDie) {
		this.hitDie = hitDie;
	}

	public void setLevel(int level) {
		int from = this.level;
		this.level = level;
		characterClass.updateLevel(this, from, level);
		cs.updateLevel(characterClass.name(), from, level);
		cs.setupFeats();
		cs.informWatchers();
	}

	public void setSaves(boolean fort, boolean reflex, boolean will){
		this.goodSaveFort = fort;
		this.goodSaveReflex = reflex;
		this.goodSaveWill = will;
	}

	public void setSkillsPerLevel(int skillsPerLevel) {
		this.skillsPerLevel = skillsPerLevel;
	}

	public void setSpellSlots(Map<Integer, List<SpellSlot>> spellSlots) {
		this.spellSlots = spellSlots;
	}

	public void setupSpellSlots(String spellSlotsCode, Ability.Type ability) {
		String[] spellsPerLevel = spellSlotsCode.split("\t");

		for (int level = 0 ; level < spellsPerLevel.length ; level++){
			
			/**handle clerics domain spell**/
			if (spellsPerLevel[level].contains("+")){
				//				//BattleMap.debug("adjusting level "+level+" from "+spellsPerLevel[level]);
				spellsPerLevel[level] = spellsPerLevel[level].substring(0, spellsPerLevel[level].indexOf("+"));
				//				//BattleMap.debug("adjusting level "+level+" interim "+spellsPerLevel[level]);
				int c = Integer.parseInt(spellsPerLevel[level]);
				//				if (c>0){
				spellsPerLevel[level] = ""+(c+1);
				//				}
				//				//BattleMap.debug("adjusting level "+level+" to "+spellsPerLevel[level]);
			}

			int slotCount = ( (spellsPerLevel[level].matches("(-|Ñ|0|\\*)")) ? 0 : Integer.parseInt(spellsPerLevel[level]) );
			if (spellsPerLevel[level].equals("*"))slotCount = 1;

			if (spellSlots.get(level)==null)spellSlots.put(level,new ArrayList<SpellSlot>());
			List<SpellSlot> slotList = spellSlots.get(level); 
			while(slotList.size()<slotCount)slotList.add(new SpellSlot());
			while(slotList.size()>slotCount)slotList.remove(slotList.size()-1);
		}

		for (int level = 1 ; level <= 9 ; level++){

			if (spellSlots.get(level)==null)spellSlots.put(level, new ArrayList<SpellSlot>());
			if (spellSlots.get(level).size()==0 && spellsPerLevel.length>level && !spellsPerLevel[level].equals("0"))return;

			int mod = this.cs.getAbility(ability).getAbilityMod();
			mod-=(level-1);

			int bonus = (int)Math.ceil(mod/4.0);

			if (bonus<0)bonus = 0;

			for (; bonus>0 ; bonus--){
				spellSlots.get(level).add(new SpellSlot());
			}
		}

		cs.informWatchers();
	}

	@Override
	public String toString() {
		return "CharacterClassModel [characterClass=" + characterClass
				+ ", level=" + level + "]";
	}

	public void informWatchers() {
		cs.informWatchers();
	}


}