package com.mut8ed.battlemap.client.view;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.widget.PlayerPanel;



public class TestingView extends VerticalPanel {

	
	public TestingView(){
		add(PlayerPanel.getInstance());
	}
}
