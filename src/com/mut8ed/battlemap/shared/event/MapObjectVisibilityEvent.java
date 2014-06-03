package com.mut8ed.battlemap.shared.event;

public class MapObjectVisibilityEvent extends MapEvent {

	private String elementId = null;
	private boolean visable = true;
	
	public MapObjectVisibilityEvent(){}

	public MapObjectVisibilityEvent(String elementId, Boolean visable) {
		super();
		this.elementId = elementId;
		this.visable = visable;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public boolean isVisable() {
		return visable;
	}

	public void setVisable(Boolean visable) {
		this.visable = visable;
	}
	
	
}
