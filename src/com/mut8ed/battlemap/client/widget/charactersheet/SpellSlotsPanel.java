package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.SpellSlot;

public class SpellSlotsPanel extends FlexTable {

	private KnownSpellsPanel knownSpellsPanel;
	private CharacterClassModel ccm;

	public SpellSlotsPanel(CharacterClassModel ccm) {
		this.ccm = ccm;

		build();
		
	}

	private void build() {
		clear(true);
		for (int i = 0 ; i < ccm.getSpellSlots().size() ; i++){
			final int level = i;
			if (ccm.getSpellSlots().get(level).size()==0)continue;
			this.setHTML(0, level, "<b>"+level+"</b>");
			for (int row = 0 ; row < ccm.getSpellSlots().get(level).size() ; row++){
				final SpellSlot slot = ccm.getSpellSlots().get(level).get(row);
				Spell spell = slot.getSpell();
				final Label spellBox = new Label();
				spellBox.setStylePrimaryName("spellSlot");
				//				spellBox.setReadOnly(true);
				if (spell!=null){
					spellBox.setText(spell.getName());
					spellBox.setTitle(spell.getName()+": single click to cast");
				} else {
					spellBox.setTitle("Empty Spell Slot: drag a spell from below to set");
				}
				this.setWidget(row+1, level, spellBox);

				final Timer singleClickTimer = new Timer() {

					@Override
					public void run() {
						//TODO cast the stupid spell yo
					}
				};

				spellBox.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						singleClickTimer.schedule(250);
					}
				});


				spellBox.addDragOverHandler(new DragOverHandler() {

					@Override
					public void onDragOver(DragOverEvent event) {
						//						//BattleMap.debug("drag over spellBox");
						event.preventDefault();
					}
				});

				spellBox.addDragEnterHandler(new DragEnterHandler() {

					@Override
					public void onDragEnter(DragEnterEvent event) {
						if (knownSpellsPanel!=null && knownSpellsPanel.getDragSpell()!=null){
							if (knownSpellsPanel.getDragSpell().getLevel()<=level){
								spellBox.addStyleName("dragOver");
							}
						}
					}
				});

				spellBox.addDragLeaveHandler(new DragLeaveHandler() {

					@Override
					public void onDragLeave(DragLeaveEvent event) {
						spellBox.removeStyleName("dragOver");
					}
				});

				spellBox.addDropHandler(new DropHandler() {

					@Override
					public void onDrop(DropEvent event) {
						event.preventDefault();

						if (knownSpellsPanel!=null && knownSpellsPanel.getDragSpell()!=null){
							if (knownSpellsPanel.getDragSpell().getLevel()<=level){
								Spell spell = knownSpellsPanel.getDragSpell();
								slot.setSpell(spell);
								spellBox.setText(spell.getName());
								CharacterView.getInstance().save();
							}
						}

						//BattleMap.debug("SSP:dropped");
						try {
							//BattleMap.debug("SSP:dropped "+event.getData("text"));
						} catch (Exception e) {
							//BattleMap.debug(e.toString(),e);
						}
						spellBox.removeStyleName("dragOver");
					}
				});
			}
		}
	}

	public KnownSpellsPanel getKnownSpellsPanel() {
		return knownSpellsPanel;
	}

	public void setKnownSpellsPanel(KnownSpellsPanel knownSpellsPanel) {
		this.knownSpellsPanel = knownSpellsPanel;
	}




}
