package com.mut8ed.battlemap.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.util.MapKeyboardHandler;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.shared.dto.Note;

public class NoteDialog extends DialogBox {

	private VerticalPanel panel = new VerticalPanel();
	private Note note;
	private TextArea textArea;
	private boolean kbEnabled;

	public NoteDialog(Note n){
		this.note = n;

		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		this.addStyleName("noteDialog");

		this.textArea = new TextArea();
		textArea.setValue(note.getNote());

		HorizontalPanel buttons = new HorizontalPanel();

		Button button = null;

		button = new Button("Cancel");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
				textArea.setValue(note.getNote());
			}
		});
		buttons.add(button);

		button = new Button("Delete");
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
				BattleMap.eventBus.removeMapObject(
						note.getElementId(), 
						new MTAsyncCallback<Void>()
						);
			}
		});
		buttons.add(button);

		button = new Button("Save");
		buttons.add(button);
		button.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
				saveNote();
			}
		});

		panel.add(buttons);
		panel.add(textArea);

		this.setWidget(panel);

		this.setAutoHideEnabled(true);

		textArea.setEnabled(MapView.getInstance().isEditable());

	}

	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		if (key == KeyCodes.KEY_ESCAPE){
			saveNote();
			hide();
			BattleMap.setMessage("");
			return false;
		}

		return true;
	}

	@Override
	public void hide() {
		MapKeyboardHandler.getInstance().setEnabled(kbEnabled);
		super.hide();
	}

	@Override
	public void show() {
		MapKeyboardHandler kbh = MapKeyboardHandler.getInstance();
		this.kbEnabled = kbh.isEnabled();
		kbh.setEnabled(false);
		super.show();
	}

	public void saveNote(){
		if (note.getNote().equals(textArea.getValue()))return;
		//BattleMap.debug("saving note");
		note.setNote(textArea.getValue());
		BattleMap.eventBus.saveNote(note, new MTAsyncCallback<Void>());
	}

	public void focus(){
		textArea.setFocus(true);
		textArea.setCursorPos(textArea.getValue().length());
	}

}
