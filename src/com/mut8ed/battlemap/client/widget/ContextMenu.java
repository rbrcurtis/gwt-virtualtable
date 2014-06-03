package com.mut8ed.battlemap.client.widget;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;

public class ContextMenu {

	private static ContextMenu instance;
	private static boolean showing = false;
	private HorizontalPanel panel = new HorizontalPanel();

	public ContextMenu(){
		throw new RuntimeException("invalid instantiation, use getInstance()");
	}

	private ContextMenu(String Null) {
		super();
		panel.getElement().setId("contextMenu");
	}

	public static ContextMenu getInstance(){
		if (instance==null){
			instance = new ContextMenu(null);
		}
		return instance;
	}

	public void show(int clientX, int clientY, VerticalPanel contextPanel) {
		panel.clear();
		if (contextPanel!=null){
			panel.add(contextPanel);
			panel.getElement().getStyle().setProperty("left", clientX+"px");
			panel.getElement().getStyle().setProperty("top", clientY+"px");
			if (!showing)BattleMap.add(panel);
			showing = true;
		} else {
			hide();
		}
	}

	public static void hide(){
		if (showing){
			showing = false;
			BattleMap.remove(getInstance().panel);
		}
	}
}
