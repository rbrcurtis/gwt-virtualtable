package com.mut8ed.battlemap.shared.event;

public class ChangeMapNameEvent extends MapEvent {

	private String mapName;

	public ChangeMapNameEvent(String mapName) {
		this.mapName = mapName;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	

}
