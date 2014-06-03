package com.mut8ed.battlemap.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.view.CharacterChooser;
import com.mut8ed.battlemap.client.view.ListGamesView;

public class ChatMenu extends HorizontalPanel {
	
	public ChatMenu(){
		
		setStyleName("chatMenu");
		
		Label label = new Label("Characters");
		label.setStyleName("menuLink");
		
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				CharacterChooser characterChooser = CharacterChooser.getInstance();
				if (!characterChooser.isAttached())BattleMap.add(characterChooser);
				characterChooser.showCloseButton(true);
				characterChooser.show();
			}
		});
		add(label);
		this.setCellHorizontalAlignment(label, HasAlignment.ALIGN_RIGHT);
		
		
		label = new Label("Switch Game");
		label.setStyleName("menuLink");
		
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGamesView lgv = new ListGamesView();
				BattleMap.add(lgv);
				DOM.setStyleAttribute(lgv.getElement(), "height", ChatPanel.getInstance().getOffsetHeight()+"px");
				lgv.showCloseButton(true);
			}
		});
		add(label);
		this.setCellHorizontalAlignment(label, HasAlignment.ALIGN_LEFT);
	}

}
