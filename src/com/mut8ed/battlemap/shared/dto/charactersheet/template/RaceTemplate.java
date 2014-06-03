package com.mut8ed.battlemap.shared.dto.charactersheet.template;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;

public abstract class RaceTemplate {

	protected Map<Integer, Map<String,String>> powers = new LinkedHashMap<Integer, Map<String,String>>();
	String nonIncrementingPowers = 
			"(bonus feat|rage power|weapon training|favored enemy" +
			"|favored terrain|rogue talent|bloodline power" +
			"|bloodline spell|bloodline feat|order ability" +
			"|discovery|grand discovery|deeds|gun training [0-9]+" +
			"|magus arcana)";

	public RaceTemplate(){
		setupPowers();
		
		for (int i = 1 ; i<=20 ; i++){
			if (powers.get(i)==null)powers.put(i, new HashMap<String, String>());
		}
	}

	abstract void setupPowers();

	public abstract void apply(CharacterSheet cs);

	protected void addPowers(int level, String p){
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (String power : p.split(",")){
			power = power.trim().toLowerCase();
			String key = power;
			key = key.replaceAll("[^a-z]", "");
			if (power.matches(nonIncrementingPowers)){
				key = level + key;
				power = "**"+power+"**";
				if (map.get(key)!=null)key+="b";
			}
			map.put(key, power);
		}
		
		powers.put(level, map);
		System.out.println(level+"="+map+" added to powers list");
	}

	abstract void updateSpells(CharacterSheet cs);

	public void updateLevel(CharacterSheet ccm, int from, int to){

//		updateSpells(ccm);
//
//		//holy fuck this shit is convoluted
//		if (from<to){//up
//			for (int level = from+1 ; level<=to ; level++){
//				for (Entry<String, String> e : powers.get(level).entrySet()){
//					if (e.getKey().contains("feat")){
//						ccm.addClassFeat(new Feat(level, "class", e.getValue()));
//					
//					} else {
//						ccm.addClassPower(e.getKey(), e.getValue());
//					}
//				}
//			}
//		} else {//down
//			for (int level = from ; level>to ; level--){
//				for (Entry<String, String> e : powers.get(level).entrySet()){
//					ccm.removeClassPower(e.getKey());
//					//feats are handled by the CS
//					if (!e.getValue().matches(nonIncrementingPowers)){
//						//n^2 !
//						for (int l = to; l>=0 ; l--){
//							if (powers.get(l)==null)continue;
//							if (powers.get(l).get(e.getKey())!=null){
//								ccm.addClassPower(e.getKey(), powers.get(l).get(e.getKey()));
//								break;
//							}
//						}
//					}
//				}
//			}
//		}
//
//		ccm.informWatchers();

	}

	public void remove(CharacterSheet cs) {
		
	}

}
