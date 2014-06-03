package com.mut8ed.battlemap.shared.dto.charactersheet;

import com.mut8ed.battlemap.shared.dto.charactersheet.template.AlchemistTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.BarbarianTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.BardTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.CavalierTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.CharacterTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.ClericTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.DruidTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.FighterTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.GunslingerTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.InquisitorTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.MagusTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.MonkTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.OracleTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.PaladinTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.RangerTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.RogueTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.SorcerorTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.SummonerTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.WitchTemplate;
import com.mut8ed.battlemap.shared.dto.charactersheet.template.WizardTemplate;

public enum CharacterClass {
	BARBARIAN,
	BARD,
	CLERIC,
	DRUID,
	FIGHTER,
	MONK,
	PALADIN,
	RANGER,
	ROGUE,
	SORCERER,
	WIZARD,
	ALCHEMIST,
	CAVALIER,
	GUNSLINGER,
	INQUISITOR,
	MAGUS,
	ORACLE,
	SUMMONER,
	WITCH;

	public boolean equalsAny(CharacterClass... cc) {
		for (CharacterClass c : cc){
			if (this.equals(c))return true;
		}
		return false;
	}

	public void applyTemplate(CharacterClassModel ccm){
		getTemplate().apply(ccm);
	}

	public CharacterTemplate getTemplate(){
		switch (this){

		case BARBARIAN:
			return new BarbarianTemplate();
		case BARD:
			return new BardTemplate();
		case CLERIC:
			return new ClericTemplate();
		case DRUID:
			return new DruidTemplate();
		case FIGHTER:
			return new FighterTemplate();
		case MONK:
			return new MonkTemplate();
		case PALADIN:
			return new PaladinTemplate();
		case RANGER:
			return new RangerTemplate();
		case ROGUE:
			return new RogueTemplate();
		case SORCERER:
			return new SorcerorTemplate();
		case WIZARD:
			return new WizardTemplate();
		case ALCHEMIST:
			return new AlchemistTemplate();
		case CAVALIER:
			return new CavalierTemplate();
		case GUNSLINGER:
			return new GunslingerTemplate();
		case INQUISITOR:
			return new InquisitorTemplate();
		case MAGUS:
			return new MagusTemplate();
		case ORACLE:
			return new OracleTemplate();
		case SUMMONER:
			return new SummonerTemplate();
		case WITCH:
			return new WitchTemplate();
			
		default: return null;
		}
	}

	public void updateLevel(CharacterClassModel ccm, int from, int to) {
		getTemplate().updateLevel(ccm, from, to);
	}
	
}
