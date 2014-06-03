package com.mut8ed.battlemap.server.dao.document;

import org.apache.log4j.Logger;

import com.mut8ed.battlemap.server.util.Serializer;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Race;

public class CharacterDocument {

	public static Logger logger = Logger.getLogger(CharacterDocument.class);
//	public CharacterClass characterClass;
	public String classSummary;
	public String characterSheetSerialized;
	public String id;
	public int level;
	public String name;
	public int ownerId;
	public String gameId;
	public Race race;
	public boolean isTemplate = false;
	public boolean isTempCharacter = false;
	public String timestamp;
	public String version;

	public CharacterDocument(){}
	
	public CharacterDocument(CharacterSheet cs) {

		try {
			this.id = cs.getId();
			this.ownerId = cs.getOwnerId();
			this.race = cs.getRace();
//			this.characterClass = cs.getCharacterClass();
			this.classSummary = cs.getClassSummary();
			this.name = cs.getCharacterName();
			this.level = cs.getLevel();
			this.isTemplate = cs.isTemplate();
			this.isTempCharacter = cs.isTempCharacter();
			this.characterSheetSerialized = Serializer.toString((CharacterSheet)cs);
			this.version = cs.getClass().getSimpleName();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
	}

}
