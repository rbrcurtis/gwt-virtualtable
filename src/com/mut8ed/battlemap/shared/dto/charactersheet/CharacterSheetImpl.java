package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Feat;
import com.mut8ed.battlemap.shared.dto.charactersheet.feat.Power;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.ArmorClass;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.AttackScore;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.ExtendedScore;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.MaxSkillPoints;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Misc;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Score;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.SpellSlot;

@SuppressWarnings({"deprecation","rawtypes"})
public class CharacterSheetImpl extends CharacterSheet implements Serializable {
	private static final long serialVersionUID = 1L;

	private CharacterClass characterClass;
	private String characterName;
	private Map<String, Power> classPowers = new LinkedHashMap<String, Power>();
	private int dmg;
	private List<Enhancer> enhancers = new ArrayList<Enhancer>();
	//this is an array so that we can treat it as a reference object
	private List<String[]> equipment = new ArrayList<String[]>();
	//<type, <key,power>>
	private List<Power> extraPowers = new LinkedList<Power>();
	private List<Feat> feats = new LinkedList<Feat>();
	//	private List<Power> feats = new ArrayList<Power>();
	private Figure figure;
	private String gameId;
	private int hitDie;
	private int hp;
	private String id;
	private Map<Integer, List<Spell>> knownSpells = new HashMap<Integer, List<Spell>>();
	protected Map<String, String> misc = new HashMap<String, String>();
	protected Map<String, Modifier> modifiers = new HashMap<String, Modifier>();
	private String notes;
	private int ownerId = -1;
	private Race race;
	private Map<String, Power> racePowers = new LinkedHashMap<String, Power>();
	public Map<Enum, Score> scores = new HashMap<Enum, Score>();
	private Map<Integer, List<SpellSlot>> spellSlots = new HashMap<Integer, List<SpellSlot>>();
	private int tempHp;
	transient private List<Watcher> watchers = new ArrayList<Watcher>();
	private List<Weapon> weapons = new ArrayList<Weapon>();
	private boolean isTemplate = false;
	private boolean isTempCharacter;

	protected CharacterSheetImpl(){
//		this(
//				"",
//				Race.values()[(int)(Math.random()*Race.values().length)],
//				CharacterClass.values()[(int)(Math.random()*CharacterClass.values().length)], 
//				1
//			);


	}

	protected CharacterSheetImpl(String characterName, Race race, CharacterClass characterClass, int level){
		for (int i=0;i<=9;i++){
			knownSpells.put(i, new ArrayList<Spell>());
			spellSlots.put(i, new ArrayList<SpellSlot>());
		}
		this.characterName = characterName;
		this.race = race;
		this.characterClass = characterClass;
//		scores.put(Misc.LEVEL, new Score(Misc.LEVEL, level, this)) V2;
		init();
		race.applyTemplate(this);
//		characterClass.applyTemplate(this); //V2
	}

	public static void main(String[] args){

		//		CharacterSheet cs = new CharacterSheet("bernardine", Race.HUMAN, CharacterClass.FIGHTER, 8);
		//		cs.setStats(28, 15, 20, 16, 20, 10);

		for (int level = 1 ; level <= 9 ; level++){
			int mod = 5;
			mod-=(level-1);
			int bonus = (int)Math.ceil(mod/4.0);
			if (bonus<0)bonus = 0;
			System.out.println(level+":"+bonus);
		}

	}

	@Override
	public void addClassPower(String key, String value) {
		classPowers.put(key, new Power(value));
	}

	@Override
	public void addClassPowers(Map<Integer, Map<String, String>> powers) {
		for (Integer level : powers.keySet()){
			if (level>getLevel())break;
			for (Entry<String, String> power : powers.get(level).entrySet()){
				if (power.getKey().contains("feat")){
					feats.add(new Feat(level, "class", power.getValue()));
				} else {
					classPowers.put(power.getKey(), new Power(power.getValue()));
				}
			}
		}
		informWatchers();
	}

	//	public void addPower(String collection, String type, String val) {
	//		if (val.equals("-"))
	//			return;
	//		if (val.trim().equals(""))
	//			return;
	//		addNamedPower(collection, new Power(type, val));
	//		
	//	}

	//	public LinkedHashMap<String,Power> getPowers(String string) {
	//		if (powers.get(string)==null)powers.put(string, new LinkedHashMap<String,Power>());
	//		return powers.get(string);
	//	}

	@Override
	public void addEnhancer(Enhancer e){
		enhancers.add(e);
		for (Modifier m : e.modifiers){
			addModifier(m);
		}
	}

	@Override
	public void addExtraPower(Power power) {
		extraPowers.add(power);
	}

	@Override
	public void addModifier(Modifier mod){
		System.out.println("cs.addModifier:"+mod.getModId()+":"+mod.getTargetTypes().length);
		if (modifiers==null)modifiers = new HashMap<String, Modifier>();
		for (Enum target : mod.getTargetTypes()){
			scores.get(target).addTransientModifier(mod);
		}
		modifiers.put(mod.getModId(), mod);
	}

	@Override
	public void addRacePowers(Map<Integer, Map<String, String>> powers) {
		for (Integer level : powers.keySet()){
			if (level>getLevel())break;
			for (Entry<String, String> power : powers.get(level).entrySet()){
				racePowers.put(power.getKey(), new Power(power.getValue()));
			}
		}
		informWatchers();
	}

	@Override
	public void addScore(Score score){
		if (scores.get(score.getType())!=null){
			throw new RuntimeException(score.getType()+" has already been added to "+characterName);
		}
		scores.put(score.getType(), score);
	}

	@Override
	public void addWatcher(Watcher watcher){
		watchers.add(watcher);
	}

	@Override
	public void addWeapon(Weapon weapon) {
		weapons.add(weapon);
	}

	@Override
	public void clearClassPowers(){
		classPowers.clear();
		clearClassFeats();
		informWatchers();
	}

	@Override
	public void clearRacePowers(){
		racePowers.clear();
		clearRaceFeats();
		informWatchers();
	}

	@Override
	public void clearSpells() {
		for (int i=0;i<=9;i++){
			spellSlots.get(i).clear();	
			knownSpells.get(i).clear();
		}
		informWatchers();
	}

	@Override
	public Ability getAbility(Ability.Type type){
		return (Ability)getScore(type);
	}

	@Override
	public Map<Ability.Type, Ability> getAbilityScores() {

		Map<Ability.Type, Ability> ret = new LinkedHashMap<Ability.Type, Ability>();
		for (Ability.Type type : Ability.Type.values()){
			ret.put(type, (Ability)getScore(type));
		}
		return ret;
	}

	//	public Map<String, Power> getPowers(){
	//		return powers;
	//	}

	@Override
	public CharacterClass getCharacterClass() {
		return characterClass;
	}

	@Override
	public String getCharacterName() {
		return characterName;
	}

	//	public List<Power> getFeats() {
	//		return feats;
	//	}

	@Override
	public Map<String, Power> getClassPowers() {
		return classPowers;
	}

	@Override
	public int getDmg() {
		return dmg;
	}

	@Override
	public List<String[]> getEquipment() {
		return equipment;
	}

	@Override
	public List<Power> getExtraPowers() {
		return extraPowers;
	}

	@Override
	public List<Feat> getFeats() {
		return feats;
	}

	@Override
	public String getGameId() {
		return gameId;
	}

	@Override
	public int getHitDie() {
		return hitDie;
	}

	@Override
	public int getHp() {
		return hp;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Map<Integer, List<Spell>> getKnownSpells() {
		return knownSpells;
	}

	
	@Override
	public int getLevel() {
		return scores.get(Misc.LEVEL).getBase();
	}

	//	public List<Power> getPowers() {
	//		if (powers==null)powers = new ArrayList<Power>();
	//		return powers;
	//	}

	@Override
	public String getMisc(String key) {
		return misc.get(key);
	}

	@Override
	public Modifier getModifier(String modId) {
		return modifiers.get(modId);
	}

	@Override
	public Map<String, Modifier> getModifiers() {
		return modifiers;
	}

	@Override
	public String getNotes() {
		return notes;
	}

	@Override
	public int getOwnerId() {
		return ownerId;
	}

	@Override
	public Race getRace() {
		return race;
	}

	@Override
	public Map<String, Power> getRacePowers() {
		return racePowers;
	}

	@Override
	public Score getScore(Enum type){
		return scores.get(type);
	}

	@Override
	public Skill getSkill(Skill.Type type){
		return (Skill)getScore(type);
	}

	@Override
	public Map<Skill.Type, Skill> getSkills() {

		Map<Skill.Type, Skill> ret = new LinkedHashMap<Skill.Type, Skill>();
		for (Skill.Type type : Skill.Type.values()){
			ret.put(type, (Skill)getScore(type));
		}
		return ret;
	}

	@Override
	public Map<Integer, List<SpellSlot>> getSpellSlots() {
		return spellSlots;
	}

	@Override
	public int getTempHp() {
		return tempHp;
	}

	@Override
	public List<Weapon> getWeapons() {
		return weapons;
	}

	@Override
	public void informWatchers() {
		for (Watcher watcher : watchers){
			watcher.onChange();
		}
	}

	@Override
	public void reapplyModifiers(){
		for (Modifier mod : modifiers.values()){
			for (Enum target : mod.getTargetTypes()){
				scores.get(target).addTransientModifier(mod);
			}
		}
	}

	@Override
	public void removeClassPower(String key) {
		if (key.contains("feat")){
			feats.remove(key);
		} else {
			classPowers.remove(key);
		}
	}

	@Override
	public void removeEnhancer(Enhancer e){
		if (enhancers.remove(e)){
			for (Modifier m : e.modifiers){
				removeModifier(m.getModId());
			}
		}
	}

	@Override
	public void removeExtraPower(Power power){
		extraPowers.remove(power);
	}

	@Override
	public String removeMisc(String key){
		return misc.remove(key);
	}

	@Override
	public Modifier removeModifier(String modId){
		if (modifiers==null){
			modifiers = new HashMap<String, Modifier>();
			return null;
		}

		Modifier mod = modifiers.remove(modId);
		if (mod==null)return null;
		for (Enum target : mod.getTargetTypes()){
			Score score = scores.get(target);
			if (score!=null)score.removeModifier(mod);
		}

		return mod;
	}

	@Override
	public Score removeScore(Enum type) {
		return scores.remove(type);
	}

	@Override
	public void removeWeapon(Weapon weapon) {
		weapons.remove(weapon);
	}

	@Override
	public void setAtkProgression(double multiplier) {
//		((AttackScore)getScore(CombatStatType.BASEATTACK)).setMultiplier(multiplier);
	}


	@Override
	public void setCharacterClass(CharacterClass characterClass) {
//		this.characterClass.removeTemplate(this); V2
		this.characterClass = characterClass;
//		characterClass.applyTemplate(this); V2
	}

	@Override
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	@Override
	public void setDmg(int dmg) {
		this.dmg = dmg;
	}

	@Override
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	@Override
	public void setHitDie(int hitDie) {
		this.hitDie = hitDie;
	}

	@Override
	public void setHp(int hp) {
		this.hp = hp;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}


	@Override
	public void setLevel(int level) {
		int from = (scores.get(Misc.LEVEL)).getBase();
		(scores.get(Misc.LEVEL)).setBase(level);
//		characterClass.updateLevel(this, from, level); //commented out for V2
		race.updateLevel(this, from, level);

		setupFeats(from, level);

		informWatchers();
	}

	@Override
	public void setMisc(String key, String val){
		misc.put(key, val);
	}

	@Override
	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}


	@Override
	public void setRace(Race race) {
		this.race.removeTemplate(this);
		this.race = race;
		race.applyTemplate(this);
	}

	@Override
	public void setSaves(boolean fort, boolean reflex, boolean will){
//		SavingThrow st = (SavingThrow)getScore(SavingThrow.Type.FORT);
//		if (st!=null){
//			st.setGoodSave(fort);
//			st.informWatchers();
//		} else {
////			addScore(new SavingThrow(SavingThrow.Type.FORT, fort, this, Ability.Type.CON));
//		}
//
//		st = (SavingThrow)getScore(SavingThrow.Type.REFLEX);
//		if (st!=null){
//			st.setGoodSave(reflex);
//			st.informWatchers();
//		} else {
////			addScore(new SavingThrow(SavingThrow.Type.REFLEX, reflex, this, Ability.Type.DEX));
//		}
//
//		st = (SavingThrow)getScore(SavingThrow.Type.WILL);
//		if (st!=null){
//			st.setGoodSave(will);
//			st.informWatchers();
//		} else {
////			addScore(new SavingThrow(SavingThrow.Type.WILL, will, this, Ability.Type.WIS));
//		}
	}

	@Override
	public void setSkillsPerLevel(int skillsPerLevel) {
		getScore(Misc.MAX_SKILLS).setBase(skillsPerLevel);
	}

	@Override
	public void setStats(int str, int dex, int con, int smarts, int wis, int cha){
		getScore(Ability.Type.STR).setBase(str);
		getScore(Ability.Type.DEX).setBase(dex);
		getScore(Ability.Type.CON).setBase(con);
		getScore(Ability.Type.INT).setBase(smarts);
		getScore(Ability.Type.WIS).setBase(wis);
		getScore(Ability.Type.CHA).setBase(cha);
	}

	@Override
	public void setTempHp(int tempHp) {
		this.tempHp = tempHp;
	}

	@Override
	public void setupSpellSlots(String spellSlotsCode, Ability.Type ability, CharacterSheet cs) {
		String[] spellsPerLevel = spellSlotsCode.split("\t");

		for (int level = 0 ; level < spellsPerLevel.length ; level++){

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

			for (int row = Math.max(spellSlots.get(level).size(),slotCount) ; row >= 1 ; row--){
				if (row<=slotCount){
					if (row<=spellSlots.get(level).size())continue;
					else {
						spellSlots.get(level).add(new SpellSlot());
						//						//BattleMap.debug("adding spellslot for level "+level+" now has "+spellSlots.get(level).size());
					}
				} else if (row>slotCount && row<=spellSlots.get(level).size()){
					//					//BattleMap.debug("removing slot "+row+" for level "+level);
					spellSlots.get(level).remove(row-1);
					//					//BattleMap.debug("level "+level+" now has "+spellSlots.get(level).size());
				}
			}
		}

		for (int level = 1 ; level <= 9 ; level++){

			if (spellSlots.get(level).size()==0 && spellsPerLevel.length>level && !spellsPerLevel[level].equals("0"))return;

			int mod = getAbility(ability).getAbilityMod();
			mod-=(level-1);

			int bonus = (int)Math.ceil(mod/4.0);

			if (bonus<0)bonus = 0;

			for (; bonus>0 ; bonus--){
				spellSlots.get(level).add(new SpellSlot());
			}
		}

		informWatchers();
	}

	@Override
	public String toString() {
		return characterName+ ", level "+getLevel()+" "+race+" "+characterClass;
	}

	private void clearClassFeats() {
		for (int i = feats.size()-1;i>=0;i--){
			if (feats.get(i).getType().equals("class"))feats.remove(i);
		}
	}

	private void clearRaceFeats() {
		for (int i = feats.size()-1;i>=0;i--){
			if (feats.get(i).getType().equals("race"))feats.remove(i);
		}
	}

	private void init() {

		misc.put("modPanel-type-1", "Gear");
		misc.put("modPanel-type-2", "Feats");
		misc.put("modPanel-type-3", "Spells");

		for (Ability.Type stat : Ability.Type.values()){
			addScore(new Ability(stat, 10, this));
		}

		addScore(new Score(CombatStatType.MOVE, 30, this));
		AttackScore atk = new AttackScore(CombatStatType.BASEATTACK, this);
		addScore(atk);
		addScore(new ExtendedScore(CombatStatType.MELEE, atk, this, Ability.Type.STR));
		addScore(new ExtendedScore(CombatStatType.RANGED, atk, this, Ability.Type.DEX));
		addScore(new ExtendedScore(CombatStatType.CMB, atk, this, Ability.Type.STR));
		ExtendedScore cmd = new ExtendedScore(CombatStatType.CMD, atk, this, Ability.Type.STR, Ability.Type.DEX);
		cmd.setBase(10);
		addScore(cmd);
		addScore(new Score(CombatStatType.DMG, 0, this));
		addScore(new ArmorClass(this, Ability.Type.DEX));

		addScore(new MaxSkillPoints(this, 0));

		addScore(new Skill(Skill.Type.ACROBATICS, 0, this, true, Ability.Type.DEX));
		addScore(new Skill(Skill.Type.APPRAISE, 0, this, true,Ability.Type.INT));
		addScore(new Skill(Skill.Type.BLUFF, 0, this, true,Ability.Type.CHA));
		addScore(new Skill(Skill.Type.CLIMB, 0, this, true,Ability.Type.STR));
		addScore(new Skill(Skill.Type.CRAFT, 0, this, true,Ability.Type.INT));
		addScore(new Skill(Skill.Type.DIPLOMACY, 0, this, true,Ability.Type.CHA));
		addScore(new Skill(Skill.Type.DISABLE, 0, this, false,Ability.Type.DEX));
		addScore(new Skill(Skill.Type.DISGUISE, 0, this, true,Ability.Type.CHA));
		addScore(new Skill(Skill.Type.ESCAPE, 0, this, true,Ability.Type.DEX));
		addScore(new Skill(Skill.Type.FLY, 0, this, true,Ability.Type.DEX));
		addScore(new Skill(Skill.Type.HANDLE, 0, this, false,Ability.Type.CHA));
		addScore(new Skill(Skill.Type.HEAL, 0, this, true,Ability.Type.WIS));
		addScore(new Skill(Skill.Type.INTIMIDATE, 0, this, true,Ability.Type.CHA));
		addScore(new Skill(Skill.Type.K_ARCANA, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_DUNGEONEERING, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_ENGINEERING, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_GEOGRAPHY, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_HISTORY, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_LOCAL, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_NATURE, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_NOBILITY, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_PLANES, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.K_RELIGION, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.LINGUISTICS, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.PERCEPTION, 0, this, true,Ability.Type.WIS));
		addScore(new Skill(Skill.Type.PERFORM, 0, this, true,Ability.Type.CHA));
		addScore(new Skill(Skill.Type.PROFESSION, 0, this, false,Ability.Type.WIS));
		addScore(new Skill(Skill.Type.RIDE, 0, this, true,Ability.Type.DEX));
		addScore(new Skill(Skill.Type.SENSE, 0, this, true,Ability.Type.WIS));
		addScore(new Skill(Skill.Type.SLEIGHT, 0, this, false,Ability.Type.DEX));
		addScore(new Skill(Skill.Type.SPELLCRAFT, 0, this, false,Ability.Type.INT));
		addScore(new Skill(Skill.Type.STEALTH, 0, this, true,Ability.Type.DEX));
		addScore(new Skill(Skill.Type.SURVIVAL, 0, this, true,Ability.Type.WIS));
		addScore(new Skill(Skill.Type.SWIM, 0, this, true,Ability.Type.STR));
		addScore(new Skill(Skill.Type.USE_MAGIC_DEVICE, 0, this, false, Ability.Type.CHA));

		setSaves(false, false, false);

		setupFeats();

		hitDie = 10;
		hp = (int)((hitDie/2.0+.5)*getLevel());

	}

	private void setupFeats() {
		setupFeats(0, getLevel());
	}

	private void setupFeats(int from, int to) {


		if (from<to){
			for (int i = from+1 ; i <= to ; i++){
				if (i%2==1)feats.add(new Feat(i, "level", ""+i));
			}
		} else {
			for (int i = feats.size()-1;i>=0;i--){
				Feat feat = feats.get(i);
				int level = feat.getLevel();
				if (level > to){
					System.out.println("removing level feat "+i);
					feats.remove(i);
				}
			}
		}
	}

	@Override
	public void addFeat(Feat feat) {
		feats.add(feat);
	}

	@Override
	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	@Override
	public Figure getFigure(){
		return figure;
	}

	@Override
	public boolean isTemplate() {
		return isTemplate;
	}

	@Override
	public void isTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	@Override
	public void isTempCharacter(boolean isTemp) {
		this.isTempCharacter = isTemp;
	}

	public boolean isTempCharacter() {
		return isTempCharacter;
	}

	@Override
	public String getClassSummary() {
		return characterClass.name();
	}

	@Override
	public CharacterClassModel[] getClassModels() {
		throw new RuntimeException("old version, not implemented");
	}

	@Override
	public void addCharacterClass(CharacterClass alchemist, int level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCharacterClass() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeClassModel(int index) {
		// TODO Auto-generated method stub
		
	}


}