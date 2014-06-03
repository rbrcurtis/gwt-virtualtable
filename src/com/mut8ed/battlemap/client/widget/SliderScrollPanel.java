package com.mut8ed.battlemap.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.Jq;
import com.mut8ed.battlemap.client.view.AddMapObjectView;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.client.widget.editor.EditorPanel;
import com.mut8ed.battlemap.shared.dto.MapObject;

public class SliderScrollPanel extends FlowPanel {

	private ContextMenu contextMenu = null;
	private VerticalPanel menu;
	private com.google.gwt.user.client.Element clickedElement;
	private EditorPanel editor;
	

	public SliderScrollPanel(EditorPanel ed){
		
		this.editor = ed;
		
		sinkEvents(Event.ONCONTEXTMENU | Event.ONCLICK);
		contextMenu = ContextMenu.getInstance();
		
		menu = new VerticalPanel();
		Anchor editLabel = new Anchor("Edit");
		editLabel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if (clickedElement!=null){
					ContextMenu.hide();
					MapView mapView = MapView.getInstance();
//					mapView.saveMap();
					Jq.hide(mapView.getElement().getId(), "slow");
					AddMapObjectView view = AddMapObjectView.getInstance();
					if (!view.isAttached()){
						view.setVisible(false);
						BattleMap.add(view);
					}
					Jq.show(view.getElement().getId(), "slow");
					
					MapObject mo = editor.getMapObjectById(clickedElement.getId());
					EditorPanel.setSelected(clickedElement, mo, editor.getDimensions());
					view.setSelectedObject(mo);
				}
			}
			
		});
		menu.add(editLabel);
	}

	@Override
	public void onBrowserEvent(Event event) {
		GWT.log("slider mouse event: "+DOM.eventGetTypeString(event));
		if (DOM.eventGetType(event)==Event.ONCONTEXTMENU) {
			
			event.stopPropagation();
			event.preventDefault();

			int x = event.getClientX();
			int y = event.getClientY();
			NodeList<Node> children = getChildren().get(0).getElement().getChildNodes();
			for (int i = 0; i< children.getLength(); i++){
				Node child = children.getItem(i);
				Element widget = (Element)child;
				String id = widget.getId(); 
				GWT.log("checking "+id+" to see if its the rightclicked element");
				int left = widget.getAbsoluteLeft();
				int width = widget.getOffsetWidth();
				int top = widget.getAbsoluteTop();
				int height = widget.getOffsetHeight();
				
				if (left < x && left+width > x
						&&
						top < y && top+height > y){
					
					this.clickedElement = DOM.getElementById(id);
					
					GWT.log(editor.getElement().getId()+" context menu for "+id);
					GWT.log("positioning context menu at "+event.getClientX()+"x"+event.getClientY());
					contextMenu.show(event.getClientX(), event.getClientY(), menu);
					break;
				}
			}
			
			
			

		} else if (DOM.eventGetType(event)==Event.ONCLICK) {
			ContextMenu.hide();
		}
		super.onBrowserEvent(event);
	}
	
}
