package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability.Type;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class Weapon implements Watchable, Serializable {
	private static final long serialVersionUID = 1L;


	private List<WeaponAttackBonus> attackBonuses = new ArrayList<WeaponAttackBonus>();
	private Map<WeaponDamageBonus, Integer> damageBonuses = new HashMap<WeaponDamageBonus, Integer>();
	private String damageDice;
	private boolean is2H;
	private int magicBonus = 0;
	private String name;
	private int range;
	private Integer tempMagicBonus = null;
	transient private List<Watcher> watchers = new ArrayList<Watcher>();

	public Weapon(){}

	public Weapon(String name, String damageDice, Integer range) {
		super();
		this.name = name;
		this.damageDice = damageDice;
		if (range==null)this.range = 0;
		else this.range = range;
	}

	public void addAttackBonus(WeaponAttackBonus bonus) {
		attackBonuses.add(bonus);
	}

	public void addDamageBonus(Ability ability, Integer maxDamage) {
		damageBonuses.put(ability, maxDamage);
	}

	public void addDamageBonus(WeaponDamageBonus bonus){
		damageBonuses.put(bonus, null);
	}

	@Override
	public void addWatcher(Watcher watcher) {
		this.watchers.add(watcher);
		for (WeaponAttackBonus bonus : attackBonuses){
			bonus.addWatcher(watcher);
		}
		for (WeaponDamageBonus bonus : damageBonuses.keySet()){
			bonus.addWatcher(watcher);
		}
	}

	public String getAttack(){
		String attack = "1d20";
		if (tempMagicBonus!=null){
			attack+="+"+tempMagicBonus;
		} else if (magicBonus>0){
			attack+="+"+magicBonus;
		}

		for (int i = attackBonuses.size()-1 ; i>=0 ; i--){
			WeaponAttackBonus bonus = attackBonuses.get(i);
			if (bonus==null || bonus.getAttackBonus().equals("")){
				attackBonuses.remove(i);
				continue;
			}
			attack+="+"+bonus.getAttackBonus();
		}
		return attack;
	}

	public String getDamage(){
		String damage = damageDice;
		if (tempMagicBonus!=null){
			damage+="+"+tempMagicBonus;
		} else if (magicBonus>0){
			damage+="+"+magicBonus;
		}
		for (Entry<WeaponDamageBonus, Integer> bonus : damageBonuses.entrySet()){
			try {
				String bns = bonus.getKey().getDamageBonus();
				if (bns!=null && bns.equals("0"))continue;
				String d = ""+((bonus.getValue()!=null)?Math.min(Integer.parseInt(bonus.getKey().getDamageBonus()), bonus.getValue()) : bonus.getKey().getDamageBonus());
				if (is2H && bonus.getKey() instanceof Ability){
					Ability ability = (Ability)bonus.getKey();
					if (ability.getType()==Type.STR){
						damage+="+"+(int)(Integer.parseInt(d)*1.5);
					} else {
						damage+="+"+d;
					}
				} else {
					damage+="+"+d;
				}
			} catch (NumberFormatException e) {
				//BattleMap.debug(e.getMessage(), e);
			}
		}
		return damage;
	}

	public String getDamageDice() {
		return damageDice;
	}

	public int getMagicBonus() {
		return magicBonus;
	}

	public String getName() {
		return name;
	}

	public int getRange() {
		return range;
	}

	public Integer getTempMagicBonus() {
		return tempMagicBonus;
	}

	@Override
	public void informWatchers() {
		for (Watcher watcher : watchers)watcher.onChange();
	}

	public boolean is2H() {
		return is2H;
	}

	public void is2H(boolean b) {
		is2H = b;
	}

	public void removeAttackBonus(WeaponAttackBonus bonus){
		attackBonuses.remove(bonus);
	}

	public void removeDamageBonus(WeaponDamageBonus bonus){
		damageBonuses.remove(bonus);
	}

	public void setDamageDice(String damageDice) {
		this.damageDice = damageDice;
		informWatchers();
	}

	public void setMagicBonus(int magicWeaponBonus) {
		this.magicBonus = magicWeaponBonus;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRange(int range) {
		this.range = range;
	}
	public void setTempMagicBonus(Integer tempMagicBonus) {
		this.tempMagicBonus = tempMagicBonus;
	}

	@Override
	public String toString() {
		return "Weapon [name=" + name + ", damageDice=" + damageDice
				+ ", range=" + range + ", totalDamage=" + getDamage() + "]";
	}

	public List<WeaponAttackBonus> getAttackBonuses() {
		return attackBonuses;
	}

	public Collection<WeaponDamageBonus> getDamageBonuses() {
		return damageBonuses.keySet();
	}

}
