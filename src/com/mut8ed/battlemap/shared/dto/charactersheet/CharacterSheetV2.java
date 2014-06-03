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
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability.Type;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.ArmorClass;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.AttackScore;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.CombatStatType;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.ExtendedScore;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.MaxSkillPoints;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Misc;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.SavingThrow;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Score;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.SpellSlot;

@SuppressWarnings("rawtypes")
public class CharacterSheetV2 extends CharacterSheet implements Serializable, Watchable {
	private static final long serialVersionUID = 1L;

	private String characterName;


	private List<CharacterClassModel> classModels = new LinkedList<CharacterClassModel>();
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
	private int hp;
	private String id;
	private boolean isTempCharacter;
	private boolean isTemplate = false;
	protected Map<String, String> misc = new HashMap<String, String>();
	protected Map<String, Modifier> modifiers = new HashMap<String, Modifier>();
	private String notes;
	private int ownerId = -1;
	private Race race;
	private Map<String, Power> racePowers = new LinkedHashMap<String, Power>();
	public Map<Enum, Score> scores = new HashMap<Enum, Score>();
	private int tempHp;
	transient private List<Watcher> watchers = new ArrayList<Watcher>();
	private List<Weapon> weapons = new ArrayList<Weapon>();

	protected CharacterSheetV2(){
		this(
				"",
				Race.values()[(int)(Math.random()*Race.values().length)],
				CharacterClass.values()[(int)(Math.random()*CharacterClass.values().length)], 
				1
				);


	}

	protected CharacterSheetV2(String characterName, Race race, CharacterClass characterClass, int level){
		this.characterName = characterName;
		this.race = race;
		init();
		CharacterClassModel ccm = new CharacterClassModel(characterClass, level, this);
		this.classModels.add(ccm);
		race.applyTemplate(this);
	}

	public static void main(String[] args){

		CharacterSheet cs = CharacterSheet.create("bernardine", Race.HUMAN, CharacterClass.WIZARD, 1);
		cs.addCharacterClass(CharacterClass.ALCHEMIST, 2);
		cs.setStats(18, 14, 16, 12, 12, 10);

		System.out.println(cs);
		System.out.println("fort="+cs.getScore(SavingThrow.Type.FORT).getAdjusted());
		System.out.println("reflex="+cs.getScore(SavingThrow.Type.REFLEX).getAdjusted());
		System.out.println("will="+cs.getScore(SavingThrow.Type.WILL).getAdjusted());

		System.out.println("atk="+cs.getScore(CombatStatType.BASEATTACK).getAdjusted());
		System.out.println("melee="+cs.getScore(CombatStatType.MELEE).getAdjusted());
		System.out.println("ranged="+cs.getScore(CombatStatType.RANGED).getAdjusted());
		System.out.println("cmb="+cs.getScore(CombatStatType.CMB).getAdjusted());
		System.out.println("cmd="+cs.getScore(CombatStatType.CMD).getAdjusted());
		System.out.println("ac="+cs.getScore(CombatStatType.AC).getAdjusted());
		System.out.println("Max Skills="+cs.getScore(Misc.MAX_SKILLS).getAdjusted());
	}

	@Override @Deprecated
	public void addClassPower(String key, String value) {
		throw new RuntimeException("deprecated");
	}

	@Override @Deprecated
	public void addClassPowers(Map<Integer, Map<String, String>> powers) {
		throw new RuntimeException("deprecated");
	}

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
	public void addFeat(Feat feat) {
		feats.add(feat);
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
	/**
	 * watch to be informed of level and class changes
	 * which will effect bab, saves, max skills, etc
	 */
	public void addWatcher(Watcher watcher){
		getWatchers().add(watcher);
	}

	@Override
	public void addWeapon(Weapon weapon) {
		weapons.add(weapon);
	}

	@Override @Deprecated
	public void clearClassPowers(){
		throw new RuntimeException("deprecated");
	}

	@Override
	public void clearRacePowers(){
		racePowers.clear();
		clearRaceFeats();
		informWatchers();
	}

	@Override @Deprecated
	public void clearSpells() {
		throw new RuntimeException("deprecated");
	}

	@Override
	public Ability getAbility(Ability.Type type){
		return (Ability)getScore(type);
	}

	@Override
	public Map<Ability.Type, Ability> getAbilityScores() {

		Map<Ability.Type, Ability> ret = new LinkedHashMap<Ability.Type, Ability>();
		for (Ability.Type type : Ability.Type.values()){
			Ability a = getAbility(type);
			ret.put(type, a);
		}
		return ret;
	}

	@Override @Deprecated
	public CharacterClass getCharacterClass() {
		throw new RuntimeException("deprecated");
	}

	//	public CharacterClassModel getCharacterClassModel(String characterClass) {
	//		CharacterClassModel struct = classModels.get(characterClass);
	//		if (struct==null)throw new CharacterClassNotFoundException(characterClass);
	//		else return struct;
	//	}

	@Override
	public String getCharacterName() {
		return characterName;
	}

	public CharacterClassModel[] getClassModels(){
		return classModels.toArray(new CharacterClassModel[0]);
	}

	@Override @Deprecated
	public Map<String, Power> getClassPowers() {
		throw new RuntimeException("deprecated");
	}

	@Override
	public String getClassSummary() {
		if (classModels.size()==0)return "";
		String s = "";
		for (CharacterClassModel m : classModels){
			s+=m.getCharacterClass()+"/"+m.getLevel()+", ";
		}
		return s.substring(0,s.length()-2);
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
	public Figure getFigure(){
		return figure;
	}

	@Override
	public String getGameId() {
		return gameId;
	}

	@Override @Deprecated
	public int getHitDie() {
		throw new RuntimeException("deprecated");
	}

	@Override
	public int getHp() {
		return hp;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override @Deprecated
	public Map<Integer, List<Spell>> getKnownSpells() {
		throw new RuntimeException("deprecated");
	}

	@Override
	public int getLevel() {
		int i = 0;
		for (CharacterClassModel ccm : classModels){
			i+=ccm.getLevel();
		}
		return i;
	}

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

	@Override @Deprecated
	public Map<Integer, List<SpellSlot>> getSpellSlots() {
		throw new RuntimeException("deprecated");
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
		for (Watcher watcher : getWatchers()){
			watcher.onChange();
		}
	}

	private List<Watcher> getWatchers() {
		if (watchers==null)watchers = new ArrayList<Watcher>(); 
		return watchers;
	}

	public boolean isTempCharacter() {
		return isTempCharacter;
	}

	@Override
	public void isTempCharacter(boolean isTemp) {
		this.isTempCharacter = isTemp;
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
	public void reapplyModifiers(){
		for (Modifier mod : modifiers.values()){
			for (Enum target : mod.getTargetTypes()){
				scores.get(target).addTransientModifier(mod);
			}
		}
	}

	@Override @Deprecated
	public void removeClassPower(String key) {
		throw new RuntimeException("deprecated");
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

	@Override @Deprecated
	public void setAtkProgression(double multiplier) {
		throw new RuntimeException("deprecated");
	}

	@Override @Deprecated
	public void setCharacterClass(CharacterClass characterClass) {
		throw new RuntimeException("deprecated");
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
	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	@Override
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	@Override @Deprecated
	public void setHitDie(int hitDie) {
		throw new RuntimeException("deprecated");
	}

	@Override
	public void setHp(int hp) {
		this.hp = hp;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override @Deprecated
	public void setLevel(int level) {
		throw new RuntimeException("deprecated");
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

	@Override @Deprecated
	public void setSaves(boolean fort, boolean reflex, boolean will){
		throw new RuntimeException("deprecated");
	}

	@Override @Deprecated
	public void setSkillsPerLevel(int skillsPerLevel) {
		throw new RuntimeException("deprecated");
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

	@Override @Deprecated
	public void setupSpellSlots(String spellSlotsCode, Type ability,
			CharacterSheet cs) {
		throw new RuntimeException("deprecated");
	}

	@Override
	public String toString() {
		return characterName+ ", level "+getLevel()+", "+race+" "+getClassSummary();
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

		addScore(new SavingThrow(SavingThrow.Type.FORT, this, Ability.Type.CON));
		addScore(new SavingThrow(SavingThrow.Type.REFLEX, this, Ability.Type.DEX));
		addScore(new SavingThrow(SavingThrow.Type.WILL, this, Ability.Type.WIS));

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

		//		setSaves(false, false, false);

		setupFeats();

		hp = 1;

	}

	protected void setupFeats() {


		int level = getLevel();
		Map<Integer, Boolean> levelTracker = new HashMap<Integer, Boolean>();
		for (int i = feats.size()-1 ; i >= 0 ; i--){
			Feat f = feats.get(i);
			if (f.getLevel()>level)feats.remove(i);
			else if (f.getType().equals("level"))levelTracker.put(f.getLevel(), true);
		}
		for (int i = 1 ; i <= getLevel() ; i++){
			if (i%2==1 && levelTracker.get(i)==null)feats.add(new Feat(i, "level", ""+i));
		}
	}

	protected void updateLevel(String characterClassName, int from, int level) {
		int f = 0;
		for (CharacterClassModel ccm : classModels){
			if (ccm.getCharacterClass().name().equals(characterClassName)){
				f+=from;
			} else {
				f+=ccm.getLevel();
			}
		}
		int t = f;
		t+= (level-from);
		System.out.println("updating level from "+f+" to "+t+" in CharacterSheetV2");
		race.updateLevel(this, from, level);
	}

	@Override
	public void addCharacterClass(CharacterClass classType, int level) {
		classModels.add(new CharacterClassModel(classType, level, this));
		informWatchers();
	}

	@Override
	public void addCharacterClass() {
		addCharacterClass(CharacterClass.values()[(int)(Math.random()*CharacterClass.values().length)], 1);
	}

	@Override
	public void removeClassModel(int index) {
		CharacterClassModel ccm = classModels.remove(index);
		ccm.setLevel(0);
		for (int i = feats.size()-1 ; i>=0 ; i--){
			if (feats.get(i).getType().contains(ccm.getCharacterClass().name()))feats.remove(i);
		}
		if (ccm!=null)informWatchers();
	}


}