package com.mut8ed.battlemap.shared.event;

public class UpdateLayerVisibilityEvent extends MapEvent {

	private int layer;
	private boolean visibility;
	
	public UpdateLayerVisibilityEvent(){}

	public UpdateLayerVisibilityEvent(int layer, boolean visibility) {
		super();
		this.layer = layer;
		this.visibility = visibility;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public boolean isVisibile() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}
	
	
}
