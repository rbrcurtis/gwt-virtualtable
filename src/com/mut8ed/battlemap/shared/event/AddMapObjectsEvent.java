package com.mut8ed.battlemap.shared.event;

import java.util.ArrayList;
import java.util.List;

import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;

/**
 * event to add a set object to the map.
 * @author ryan
 *
 */
public class AddMapObjectsEvent extends MapEvent {
	private List<MapObjectWrapper> mapObjectWrappers = new ArrayList<MapObjectWrapper>();
	
	public AddMapObjectsEvent(){};
	
	public AddMapObjectsEvent(List<MapObjectWrapper> mapObjectWrappers){
		this.mapObjectWrappers = mapObjectWrappers;
	}
	
	public AddMapObjectsEvent(MapObjectWrapper[] mapObjectWrappers){
		for (MapObjectWrapper mow : mapObjectWrappers){
			if (mow==null)continue;
			this.mapObjectWrappers.add(mow);
		}
	}

	public List<MapObjectWrapper> getMapObjects() {
		return mapObjectWrappers;
	}

	public void setMapObjects(List<MapObjectWrapper> mapObjectWrappers) {
		this.mapObjectWrappers = mapObjectWrappers;
	}

	public void addMapObject(MapObjectWrapper mapObjectWrapper) {
		mapObjectWrappers.add(mapObjectWrapper);
	}
	
	public String toString(){
		return "AddMapObjectsEvent:"+mapObjectWrappers;
	}

}


