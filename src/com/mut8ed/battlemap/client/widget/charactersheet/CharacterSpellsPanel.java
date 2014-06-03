package com.mut8ed.battlemap.client.widget.charactersheet;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Ability;
import com.mut8ed.battlemap.shared.dto.charactersheet.score.Watcher;

public class CharacterSpellsPanel extends FlowPanel {


	public CharacterSpellsPanel(final CharacterSheet cs) {
		super();

		setStylePrimaryName("spellSheet");
		
		build(cs);
		Watcher watcher = new Watcher() {
			
			@Override
			public void onChange() {
				build(cs);
			}
		};
		cs.addWatcher(watcher);
		for (Ability a : cs.getAbilityScores().values())a.addWatcher(watcher);

	}

	public void build(CharacterSheet cs) {
		clear();
		for (CharacterClassModel ccm : cs.getClassModels()){
			if (ccm.getSpellSlots().size()==0)continue;
			
			Label className = new Label(ccm.getCharacterClass().name());
			DOM.setStyleAttribute(className.getElement(), "fontWeight","700");
			add(className);
			add(new HTML("<br/>"));
			
			KnownSpellsPanel knownSpellsPanel;
			SpellSlotsPanel spellSlotsPanel;
			add(new Label("Spell Slots"));
			add(spellSlotsPanel = new SpellSlotsPanel(ccm));

			add(new HTML("<br/>"));
			add(new Label("Known Spells"));

			add(knownSpellsPanel = new KnownSpellsPanel(ccm));

			spellSlotsPanel.setKnownSpellsPanel(knownSpellsPanel);
			
			add(new HTML("<br/>"));
		}
	}

}
