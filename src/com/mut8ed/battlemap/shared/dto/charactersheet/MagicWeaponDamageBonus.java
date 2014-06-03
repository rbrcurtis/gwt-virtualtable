package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.io.Serializable;

import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class MagicWeaponDamageBonus implements WeaponDamageBonus, Serializable {
	private static final long serialVersionUID = 1L;

	private String bonus;
	
	public MagicWeaponDamageBonus(){}
	
	public MagicWeaponDamageBonus(String bonus) {
		this.bonus = bonus;
	}

	@Override
	public void addWatcher(Watcher watcher) {}

	@Override
	public void informWatchers() {}

	@Override
	public String getDamageBonus() {
		return bonus;
	}

}
