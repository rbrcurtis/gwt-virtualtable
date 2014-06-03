package com.mut8ed.battlemap.shared.event;

public class RotateMapObjectEvent extends MapEvent {

	private String elementId = null;
	private Integer rotation = null;

	public RotateMapObjectEvent(){}
			
	public RotateMapObjectEvent(String elementId, int rotation) {
		this.elementId = elementId;
		this.rotation = rotation;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	@Override
	public String toString() {
		return "RotateMapObjectEvent:"+elementId+"/"+rotation;
	}
	
	

}
