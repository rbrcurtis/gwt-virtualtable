package com.mut8ed.battlemap.shared.dto.charactersheet;

import java.util.List;
import java.util.Map.Entry;

import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Skill;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;

public class CharacterConverter {

	public static CharacterSheetV2 convert(CharacterSheetImpl ocs) {
		
		CharacterSheetV2 cs = new CharacterSheetV2(ocs.getCharacterName(), ocs.getRace(), ocs.getCharacterClass(), ocs.getLevel());
		cs.setId(ocs.getId());
		cs.setOwnerId(ocs.getOwnerId());
		cs.setFigure(ocs.getFigure());
		cs.setDmg(ocs.getDmg());
		cs.setTempHp(ocs.getTempHp());
		cs.setGameId(ocs.getGameId());
		cs.setHp(ocs.getHp());
		cs.setNotes(ocs.getNotes());
		
		for (Ability.Type at : Ability.Type.values()){
			cs.getAbility(at).setBase(ocs.getAbility(at).getBase());
		}
		for (Skill.Type st : Skill.Type.values()){
			cs.getScore(st).setBase(ocs.getScore(st).getBase());
		}
		for (Entry<Integer, List<Spell>> l : ocs.getKnownSpells().entrySet()){
			cs.getClassModels()[0].getKnownSpells().put(l.getKey(), l.getValue());
		}
		for (Entry<String, String> m : ocs.misc.entrySet()){
			cs.misc.put(m.getKey(), m.getValue());
		}
		for (Entry<String, Modifier> m : ocs.modifiers.entrySet()){
			Modifier mod = m.getValue();
			mod.setScore(cs.getScore(mod.getScore().getType()));
			cs.modifiers.put(m.getKey(), mod);
		}
//		for (Weapon weapon : ocs.getWeapons()){
//			List<WeaponDamageBonus> dmg = new ArrayList<WeaponDamageBonus>();
//			List<WeaponAttackBonus> atk = new ArrayList<WeaponAttackBonus>();
//			for (WeaponDamageBonus w : weapon.getDamageBonuses()){
//				dmg.add(w.get)
//			}
//			cs.addWeapon(weapon);
//		}
				
		
		return cs;
		
	}

}
