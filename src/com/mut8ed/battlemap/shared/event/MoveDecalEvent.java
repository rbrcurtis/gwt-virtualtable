package com.mut8ed.battlemap.shared.event;

import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;

public class MoveDecalEvent extends MapEvent {

	private MapObjectWrapper mapObject;

	public MoveDecalEvent(){}
	
	public MoveDecalEvent(MapObjectWrapper mow) {
		this.mapObject = mow;
	}

	public MapObjectWrapper getMapObject() {
		return mapObject;
	}

	public void setMapObject(MapObjectWrapper mapObject) {
		this.mapObject = mapObject;
	}
	
	

}
