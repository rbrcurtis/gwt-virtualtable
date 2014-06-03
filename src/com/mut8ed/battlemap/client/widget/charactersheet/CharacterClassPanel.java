package com.mut8ed.battlemap.client.widget.charactersheet;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClass;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;

public class CharacterClassPanel extends DialogBox {

	public FlexTable body = new FlexTable();
	private CharacterSheet cs;
	
	public CharacterClassPanel(CharacterSheet cs){
		super(true);
		
		this.cs = cs;
		
		this.add(body);

		build();


	}

	public void build() {
		
		body.clear();
		
		int row = 0;
		for (final CharacterClassModel ccm : cs.getClassModels()){
			final int index = row;

			body.setWidget(row,4,new Label("Class:"));
			final ListBox cClass = new ListBox();
			cClass.setStylePrimaryName("cClassBox");

			int i = 0;
			for (CharacterClass c : CharacterClass.values()){
				cClass.addItem(c.name().toLowerCase());
				if (c==ccm.getCharacterClass())cClass.setSelectedIndex(i);
				i++;
			}

			cClass.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					try {
						CharacterClass cc = CharacterClass.valueOf(cClass.getItemText(cClass.getSelectedIndex()).toUpperCase());
						ccm.setCharacterClass(cc);
					} catch (Exception e) {
						//BattleMap.debug(e.getMessage(),e);
					}
					CharacterView.getInstance().save();
				}
			});
			body.setWidget(row,5,cClass);

			body.setWidget(row,6,new Label("Level:"));
			final TextBox level = new TextBox();
			level.setStylePrimaryName("levelBox");
			level.setValue(ccm.getLevel()+"");
			level.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					try {
						ccm.setLevel(Integer.parseInt(level.getValue()));
					} catch (NumberFormatException e) {
						//BattleMap.debug(e.getMessage(), e);
						level.setValue(ccm.getLevel()+"");
					}
					CharacterView.getInstance().save();
				}
			});
			body.setWidget(row,7,level);
			
			HTML remove = new HTML("&#10007;");
			remove.setWidth("20px");
			remove.setHeight("15px");
			DOM.setStyleAttribute(remove.getElement(), "cursor", "pointer");
			
			remove.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if (!Window.confirm("Are you sure you want to delete this class?  This cannot be undone."))return;
					cs.removeClassModel(index);
					CharacterView.getInstance().save();
					build();
				}
			});
			
			body.setWidget(row, 8, remove);

			row++;
		}


		Button adder = new Button("+");

		adder.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CharacterClassPanel.this.cs.addCharacterClass();
				CharacterView.getInstance().save();
				build();
			}
		});
		body.setWidget(row, 7, adder);
	}
	
	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		if (key == KeyCodes.KEY_ESCAPE){
			hide();
			return false;
		}
		
		return true;
	}
}
