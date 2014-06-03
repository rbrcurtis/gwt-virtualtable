package com.mut8ed.battlemap.client.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.user.client.ui.Widget;

public class HtmlList extends Widget {
	private final List<Element> listItems = new ArrayList<Element>();

	public static enum ListType {
		UNORDERED {
			public Element createElement() {
				return Document.get().createULElement();
			}
		},
		ORDERED {
			public Element createElement() {
				return Document.get().createULElement();
			}
		};
		
		public abstract Element createElement();
	}
	
	public HtmlList(ListType listType) {
		setElement(listType.createElement());
		setStylePrimaryName("html-list");
	}
	
	public void add(Widget w){
		this.add(w.getElement());
	}
	
	public void add(Element elem) {
		LIElement liElement = Document.get().createLIElement();
		liElement.appendChild(elem);
		getElement().appendChild(liElement);

		listItems.add(liElement);
		
		// All the events we're interested in
//		sinkEvents(MapEvent.ONMOUSEOVER | MapEvent.ONMOUSEOUT | MapEvent.ONCLICK);	
	}
	
//	@SuppressWarnings("deprecation")
//	@Override
//	public void onBrowserEvent(MapEvent event) {
//		switch(event.getTypeInt()) {
//			case MapEvent.ONCLICK:
//				Element target = event.getTarget();
//				if (listItems.containsKey(target)) 
//					DeferredCommand.addCommand(listItems.get(target));
//
//				break;
//			case MapEvent.ONMOUSEOUT:
//				event.getTarget().setClassName(null);
//				break;
//				
//			case MapEvent.ONMOUSEOVER:
//				event.getTarget().setClassName("highlightOn");
//				break;
//		}
//	}
}
