package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Feat;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Power;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Score;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.SpellSlot;

@SuppressWarnings("rawtypes")
public abstract class CharacterSheet implements Serializable {
	private static final long serialVersionUID = 1L;

	public static CharacterSheet create(){
		return new CharacterSheetV2();
	}
	
	public static CharacterSheet create(String characterName, Race race, CharacterClass characterClass, int level){
		return new CharacterSheetV2(characterName, race, characterClass, level);
	}

	@Deprecated
	public abstract void addClassPower(String key, String value);

	@Deprecated
	public abstract void addClassPowers(Map<Integer, Map<String, String>> powers);

	public abstract void addEnhancer(Enhancer e);

	public abstract void addExtraPower(Power power);

	public abstract void addModifier(Modifier mod);

	public abstract void addRacePowers(Map<Integer, Map<String, String>> powers);

	public abstract void addScore(Score score);

	public abstract void addWatcher(Watcher watcher);

	public abstract void addWeapon(Weapon weapon);

	@Deprecated
	public abstract void clearClassPowers();

	public abstract void clearRacePowers();

	@Deprecated
	public abstract void clearSpells();

	public abstract Ability getAbility(Ability.Type type);

	public abstract Map<Ability.Type, Ability> getAbilityScores();

	@Deprecated
	public abstract CharacterClass getCharacterClass();

	public abstract String getCharacterName();

	@Deprecated
	public abstract Map<String, Power> getClassPowers();

	public abstract int getDmg();

	public abstract List<String[]> getEquipment();

	public abstract List<Power> getExtraPowers();

	public abstract List<Feat> getFeats();

	public abstract String getGameId();

	@Deprecated
	public abstract int getHitDie();

	public abstract int getHp();

	public abstract String getId();

	@Deprecated
	public abstract Map<Integer, List<Spell>> getKnownSpells();

	public abstract int getLevel();

	public abstract String getMisc(String key);

	public abstract Modifier getModifier(String modId);

	public abstract Map<String, Modifier> getModifiers();

	public abstract String getNotes();

	public abstract int getOwnerId();

	public abstract Race getRace();

	public abstract Map<String, Power> getRacePowers();

	public abstract Score getScore(Enum type);

	public abstract Skill getSkill(Skill.Type type);

	public abstract Map<Skill.Type, Skill> getSkills();

	@Deprecated
	public abstract Map<Integer, List<SpellSlot>> getSpellSlots();

	public abstract int getTempHp();

	public abstract List<Weapon> getWeapons();

	public abstract void informWatchers();

	public abstract void reapplyModifiers();

	public abstract void removeClassPower(String key);

	public abstract void removeEnhancer(Enhancer e);

	public abstract void removeExtraPower(Power power);

	public abstract String removeMisc(String key);

	public abstract Modifier removeModifier(String modId);

	public abstract Score removeScore(Enum type);

	public abstract void removeWeapon(Weapon weapon);

	@Deprecated
	public abstract void setAtkProgression(double multiplier);

	@Deprecated
	public abstract void setCharacterClass(CharacterClass characterClass);

	public abstract void setCharacterName(String characterName);

	public abstract void setDmg(int dmg);

	public abstract void setGameId(String gameId);

	@Deprecated
	public abstract void setHitDie(int hitDie);

	public abstract void setHp(int hp);

	public abstract void setId(String id);

	@Deprecated
	public abstract void setLevel(int level);

	public abstract void setMisc(String key, String val);

	public abstract void setNotes(String notes);

	public abstract void setOwnerId(int ownerId);

	public abstract void setRace(Race race);

	@Deprecated
	public abstract void setSaves(boolean fort, boolean reflex, boolean will);

	@Deprecated
	public abstract void setSkillsPerLevel(int skillsPerLevel);

	public abstract void setStats(int str, int dex, int con, int smarts,
			int wis, int cha);

	public abstract void setTempHp(int tempHp);

	public abstract void setupSpellSlots(String spellSlotsCode,
			Ability.Type ability, CharacterSheet cs);

	public abstract String toString();

	public abstract void addFeat(Feat feat);
	
	public abstract void setFigure(Figure figure);

	public abstract Figure getFigure();
	
	public abstract boolean isTemplate();
	
	public abstract void isTemplate(boolean isTemplate);

	public abstract void isTempCharacter(boolean isTemp);
	
	public abstract boolean isTempCharacter();

	public abstract String getClassSummary();
	
	public abstract CharacterClassModel[] getClassModels();

	public abstract void addCharacterClass(CharacterClass classType, int level);
	
	public abstract void addCharacterClass();

	public abstract void removeClassModel(int index);

}