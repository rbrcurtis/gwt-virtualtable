package com.mut8ed.battlemap.shared.dto.charactersheet.score;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;

public class ArmorClass extends StatModdableScore {
	private static final long serialVersionUID = 1L;
	
	private Integer maxDex = null;
	
	public ArmorClass(CharacterSheet cs, Ability.Type... abilities){
		super(CombatStatType.AC, 10, cs, abilities);
	}

	public ArmorClass(){}

//	@Override
//	public int getAdjusted() {
//		
//		int adjusted = base;
//		for (Entry<String, List<Modifier>> mod : modifiers.entrySet()){
//			adjusted+=mod.getValue().get(0).getAmount();
//			if (mod.getKey().matches("unnamed|dodge")){
//				for (int i = 1; i < mod.getValue().size(); i++) {
//					Modifier m = mod.getValue().get(i);
//					adjusted+=m.getAmount();
//					
//				}
//			}
//		}
//		for (Ability ability : moddingAbilities.values()){
//			if (ability.type == Ability.Type.DEX && maxDex!=null){
//				
//				adjusted += Math.min(ability.getAbilityMod(), maxDex);
//			
//			} else {
//			
//				adjusted += ability.getAbilityMod();
//				
//			}
//		}
//		
//		return adjusted;
//	}
	
	public Integer getMaxDex() {
		return maxDex;
	}

	public void setMaxDex(Integer maxDex) {
		this.maxDex = maxDex;
	}
	
	
	
}
