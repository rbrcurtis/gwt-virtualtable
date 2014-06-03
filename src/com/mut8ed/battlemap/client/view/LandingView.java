package com.mut8ed.battlemap.client.view;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.Page;

public class LandingView extends VerticalPanel {

	
	@SuppressWarnings("unused")
	public LandingView(){
		
		if (true){
			Page.CHAT.redirect();
			return;
		}
		
		setStyleName("LandingViewList");
		getElement().setAttribute("width", "100%");
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		add(new Hyperlink("Create Game", Page.CREATE_GAME.name()));
		add(new Hyperlink("List Games", Page.LIST_GAMES.name()));
		add(new Hyperlink("Create Map", Page.MAP.name()));
		add(new Hyperlink("Edit Map", Page.MAP_LIST.name()));
		add(new Hyperlink("Add Objects", Page.ADD_MAP_OBJECT.name()));
		add(new Hyperlink("Options", Page.OPTIONS.name()));
		String hostname = Window.Location.getHostName();
		Anchor a = new Anchor("Log out","http://"+hostname+"/user/logout");
		
		add(a);
		
		
		
		
	}
	
}
