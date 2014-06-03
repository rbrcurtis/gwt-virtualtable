package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Modifier;
import com.mut8ed.battlemap.shared.dto.charactersheet.Watchable;
import com.mut8ed.battlemap.shared.dto.charactersheet.WeaponAttackBonus;
import com.mut8ed.battlemap.shared.dto.charactersheet.WeaponDamageBonus;

@SuppressWarnings("rawtypes")
public class Score implements Watchable, WeaponAttackBonus, WeaponDamageBonus, Serializable {
	private static final long serialVersionUID = 1L;

	Integer base = null;
	CharacterSheet cs;
	transient Map<String,List<Modifier>> modifiers;
	protected Enum type;
	transient private List<Watcher> watchers = new ArrayList<Watcher>();

	public Score(){}

	public Score(Enum type, int score) {
		this.type = type;
		this.base = score;
	}

	public Score(Enum type, int score, CharacterSheet cs) {
		this(type, score);
		this.cs = cs;
	}


	public int getBase() {
		return base;
	}

	public CharacterSheet getCs() {
		return cs;
	}

	public Map<String, List<Modifier>> getModifiers() {
		if (modifiers==null)modifiers = new HashMap<String,List<Modifier>>();
		return modifiers;
	}

	public Enum getType() {
		return type;
	}


	public void setBase(int base) {
		this.base = base;
		informWatchers();
	}

	@Override
	public void informWatchers() {
		for (Watcher watcher : watchers)watcher.onChange();
	}

	public void setCs(CharacterSheet cs) {
		this.cs = cs;
	}

	public void setModifiers(Map<String, List<Modifier>> modifiers) {
		this.modifiers = modifiers;
	}

	public void setType(Enum type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Score [base=" + base + ", cs=" + cs + ", type=" + type + "]";
	}

	/**
	 * NOTE that the collection of modifiers on this class
	 * is TRANSIENT which means it wont serialize or save.
	 * you should add all Modifiers to the CharacterSheet
	 * instead which will serialize/save them
	 * @param mod
	 */
	public void addTransientModifier(Modifier mod){
		System.out.println("adding "+mod.getType()+":"+mod.getAmount()+" to "+getType());
		System.out.println(getType()+" has "+watchers.size()+" watchers");
		mod.setScore(this);
		mod.setType(mod.getType().toLowerCase());
		List<Modifier> mods = getModifiers().get(mod.getType());
		if (mods==null){
			mods = new ArrayList<Modifier>();
			if (!mods.contains(mod))mods.add(mod);
			getModifiers().put(mod.getType(), mods);
		} else {
			Modifier current = mods.get(0);
			if (current.getAmount()<mod.getAmount()){
				if (!mods.contains(mod))mods.add(0, mod);
			} else {
				if (!mods.contains(mod))mods.add(mod);
			}
		}
		informWatchers();
	}

	public int getAdjusted(){
		int adjusted = base;
		for (Entry<String, List<Modifier>> mod : getModifiers().entrySet()){
			if (mod.getValue()==null)mod.setValue(new ArrayList<Modifier>());
			if (mod.getValue().size()==0)continue;
			adjusted+=mod.getValue().get(0).getAmount();
			if (mod.getKey().matches("unnamed|dodge")){
				for (int i = 1; i < mod.getValue().size(); i++) {
					Modifier m = mod.getValue().get(i);
					adjusted+=m.getAmount();

				}
			}
		}
		return adjusted;
	}

	public void removeModifier(Modifier mod){
		mod.setType(mod.getType().toLowerCase());
		List<Modifier> mods = getModifiers().get(mod.getType());
		if (mods==null){
			return;
		} else {
			if (mods.remove(mod)){
				informWatchers();
			}
		}
	}

	@Override
	public void addWatcher(Watcher watcher){
		this.watchers.add(watcher);
	}

	@Override
	public String getDamageBonus() {
		return getAdjusted()+"";
	}

	@Override
	public String getAttackBonus() {
		return getAdjusted()+"";
	}

}