package com.mut8ed.battlemap.client.widget.charactersheet;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;

public class CharacterEquipmentPanel extends FlowPanel {

	private VerticalPanel content = new VerticalPanel();
	private CharacterSheet cs;
	private CharacterView cv;

	public CharacterEquipmentPanel(CharacterSheet cs, final CharacterView cv) {
		this.cs = cs;
		this.cv = cv;
		
		content.setStylePrimaryName("characterEquipment");
		add(content);

		build();
		
		
		
	}

	private void build() {

		content.clear();
		
		List<String[]> equipment = cs.getEquipment();
		for (final String item[] : equipment){
			
			final TextBox itemBox = new TextBox();
			itemBox.setStylePrimaryName("itemBox");
			itemBox.setValue(item[0]);
			
			itemBox.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					item[0] = itemBox.getValue();
					cv.save();
					int index = content.getWidgetIndex(itemBox);
					((TextBox)content.getWidget(index+1)).setFocus(true);
				}
			});
			
			content.add(itemBox);
			
		}
		
		addEmptyBox();
		
	}

	private void addEmptyBox() {
		
		final TextBox itemBox = new TextBox();
		itemBox.setStylePrimaryName("itemBox");
		
		itemBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cs.getEquipment().add(new String[]{itemBox.getValue()});
				cv.save();
				int index = content.getWidgetIndex(itemBox);
				build();
				((TextBox)content.getWidget(index+1)).setFocus(true);
			}
		});
		
		content.add(itemBox);
		
	}

}
