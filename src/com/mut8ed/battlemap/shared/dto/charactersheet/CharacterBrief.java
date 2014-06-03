package com.mut8ed.battlemap.shared.dto.charactersheet;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.mut8ed.battlemap.shared.dto.Figure;

public class CharacterBrief implements IsSerializable {

	public String id;
	public String characterName;
	public Race race;
	public String classSummary;
	public int level;
	public Figure figure;
	
	public CharacterBrief(){}
	
	public CharacterBrief(CharacterSheet cs){
		this.id = cs.getId();
		this.characterName = cs.getCharacterName();
		this.classSummary = cs.getClassSummary();
		this.race = cs.getRace();
		this.level = cs.getLevel();
		this.figure = cs.getFigure();
	}
	
	public String toString(){
		return characterName+ ", level "+level+" "+race+" "+classSummary;
	}
	
}
