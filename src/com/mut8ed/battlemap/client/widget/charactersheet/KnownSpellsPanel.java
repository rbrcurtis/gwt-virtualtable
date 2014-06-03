package com.mut8ed.battlemap.client.widget.charactersheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.DragEndEvent;
import com.google.gwt.event.dom.client.DragEndHandler;
import com.google.gwt.event.dom.client.DragEvent;
import com.google.gwt.event.dom.client.DragHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;

public class KnownSpellsPanel extends FlexTable {

	private Map<Integer, List<Label>> spellDivs = new HashMap<Integer, List<Label>>();
	private CharacterClassModel ccm;
	private Spell dragSpell = null;

	public KnownSpellsPanel(CharacterClassModel ccm) {

		this.ccm = ccm;

		build();
		
	}

	private void build() {
		clear(true);
		spellDivs.clear();
		for (int level = 0 ; level <= 9 ; level++){
//			if (ccm.getSpellSlots().get(level).size()==0)continue;
			this.setHTML(0, level, "<b>"+level+"</b>");
			int row = 0;
			if (ccm.getKnownSpells().get(level)==null)ccm.getKnownSpells().put(level,new ArrayList<Spell>());
			for (; row < ccm.getKnownSpells().get(level).size() ; row++){
				addKnownSpell(level, row);
			}
			addKnownSpell(level, ccm.getKnownSpells().get(level).size());
		}
	}

	private Label addKnownSpell(final int level, final int row) {
		final Spell spell = (row<ccm.getKnownSpells().get(level).size()) ? ccm.getKnownSpells().get(level).get(row) : new Spell(level, "");
		final Label spellDiv = new Label();
		DOM.setStyleAttribute(spellDiv.getElement(), "color", "#555");
		spellDiv.setStylePrimaryName("knownSpell");
		spellDiv.getElement().setDraggable(Element.DRAGGABLE_TRUE);

		if (spell!=null){
			spellDiv.setText(spell.getName());
			spellDiv.setTitle(spell.getName()+": double click to edit, or drag onto a spell slot above");
		} else {
			spellDiv.setTitle("Empty Spell Slot: double click to edit");
		}
		this.setWidget(row+1, level, spellDiv);

		spellDiv.addDragHandler(new DragHandler() {

			@Override
			public void onDrag(DragEvent event) {
				//				//BattleMap.debug("KSP:dragging "+spellBox.getText());
				//				dragSpell = spell;
			}
		});

		spellDiv.addDragEndHandler(new DragEndHandler() {

			@Override
			public void onDragEnd(DragEndEvent event) {
				//BattleMap.debug("KSP:drag ending for "+spellDiv.getText());
				dragSpell = null;
			}
		});


		spellDiv.addDragStartHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {
				//BattleMap.debug("KSP:drag start "+spell.getName());
				event.getDataTransfer().setData("text", spellDiv.getText());
				dragSpell = ccm.getKnownSpells().get(level).get(row);
			}
		});

		spellDiv.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {

				//BattleMap.debug("dbl");
				swapInTextBox(level, row, spellDiv);

			}

		});

		if (spellDivs.get(level)==null)spellDivs.put(level, new ArrayList<Label>());
		spellDivs.get(level).add(row, spellDiv);

		return spellDiv;
	}

	private void swapInTextBox(final int level, final int row, final Label spellDiv) {
		final TextBox spellBox = new TextBox();
		spellBox.setStylePrimaryName("knownSpell");
		spellBox.setText(spellDiv.getText());
		KnownSpellsPanel.this.setWidget(row+1, level, spellBox);
		spellBox.setFocus(true);
		spellBox.selectAll();

		spellBox.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE){
					spellBox.setFocus(false);
				}
			}
		});

		spellBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				spellDiv.setText(spellBox.getText());
				Spell spell = new Spell(level);
				spell.setName(spellBox.getText());
				spellBox.setFocus(false);
				if (ccm.getKnownSpells().get(level).size()>row){
					ccm.getKnownSpells().get(level).remove(row);
				}
				ccm.getKnownSpells().get(level).add(row, spell);
				CharacterView.getInstance().save();
				if (row==ccm.getKnownSpells().get(level).size()-1){
					swapInTextBox(level, row+1, addKnownSpell(level, row+1));
				} else {
					swapInTextBox(level, row+1, spellDivs.get(level).get(row+1));
				}
			}
		});

		spellBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				KnownSpellsPanel.this.setWidget(row+1, level, spellDiv);		
			}
		});
	}

	public Spell getDragSpell() {
		return dragSpell;
	}

}
