package com.mut8ed.battlemap.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContextFocusPanel extends FocusPanel {

	private ContextMenu contextMenu = null;
	private VerticalPanel menu;

	public ContextFocusPanel(Widget child){
		super(child);
		sinkEvents(Event.ONCONTEXTMENU);
		contextMenu = ContextMenu.getInstance();
		menu = new VerticalPanel();
		menu.add(new Anchor("item 1"));
		menu.add(new Anchor("item 2"));
		menu.add(new Anchor("item 3"));
	}


	public void onBrowserEvent(Event event) {

		if (DOM.eventGetType(event)==Event.ONCONTEXTMENU) {
			event.stopPropagation();
			event.preventDefault();

			GWT.log("rightclick menu");
			GWT.log("positioning context menu at "+event.getClientX()+"x"+event.getClientY());
			contextMenu.show(event.getClientX(), event.getClientY(), menu);

		} else if (DOM.eventGetType(event)==Event.ONCLICK) {
			ContextMenu.hide();
		}
		
		super.onBrowserEvent(event);
	}
}