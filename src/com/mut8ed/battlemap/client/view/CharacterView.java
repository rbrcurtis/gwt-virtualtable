package com.mut8ed.battlemap.client.view;


import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.widget.charactersheet.CharacterEquipmentPanel;
import com.mut8ed.battlemap.client.widget.charactersheet.CharacterMainPanel;
import com.mut8ed.battlemap.client.widget.charactersheet.CharacterSpellsPanel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.exception.WtfException;

public class CharacterView extends FocusPanel {

	private static CharacterView instance = null;
	private CharacterSheet cs;
	TabLayoutPanel content = new TabLayoutPanel(1.5, Unit.EM);
	private CharacterMainPanel characterMain;

	public CharacterView() {
		super();

		setStyleName("characterView");
		getElement().setId("characterView");

		resetHeight();
		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				resetHeight();
			}
		});
		
		this.setWidget(content);

	}
	
	public void showCharacter(String characterId, boolean showTemplateFlag) {
		showCharacter(characterId);
		showTemplateFlag(showTemplateFlag);
	}

	public void showCharacter(String characterId) {
		
		content.clear();

		if (characterId!=null){
			//BattleMap.debug("making call to get the character sheet");

			BattleMap.eventBus.getCharacter(characterId, new MTAsyncCallback<CharacterSheet>(){

				@Override
				public void onSuccess(CharacterSheet result) {
					if (result==null)throw new WtfException();

					cs = result;
					System.out.println("client got cs "+cs.getId()+" which is a "+cs.getClass());
					build();
				}
			});
			
		} else {
			cs = CharacterSheet.create();
			build();
		}
	}
	
	public void showTemplateFlag(boolean show){
		if (characterMain==null)return;
		characterMain.showTemplateFlag(show);
	}

	private void build() {
		//BattleMap.debug("retrieved character");

		content.add(characterMain = new CharacterMainPanel(cs), "Character");
		content.add(new CharacterEquipmentPanel(cs, CharacterView.this), "Equipment");
		content.add(new CharacterSpellsPanel(cs), "Spells");

		final TextArea notes = new TextArea();
		notes.setStylePrimaryName("characterNotes");
		notes.setValue(cs.getNotes());
		content.add(notes, "Notes");
		notes.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				cs.setNotes(notes.getValue());
				save();
			}
		});


		//				selectTab(2);
	}

	private void resetHeight() {
		content.setHeight((Window.getClientHeight()-DOM.getElementById("alerts").getClientHeight())+"px");		
	}

	public void save(){
		BattleMap.eventBus.saveCharacter(cs, new MTAsyncCallback<String>(){

			@Override
			public void onSuccess(String result) {

				cs.setId(result);
				CharacterChooser.getInstance().build();

			}

		});
	}

	public static CharacterView getInstance() {
		if (instance==null)instance  = new CharacterView();
		return instance;
	}

}
