package com.mut8ed.battlemap.shared.event;

import java.util.ArrayList;
import java.util.List;

import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;

/**
 * event to add a set object to the map.
 * @author ryan
 *
 */
public class RemoveMapObjectsEvent extends MapEvent {
	private List<String> mapObjectIds = new ArrayList<String>();
	
	public RemoveMapObjectsEvent(){};
	
	public RemoveMapObjectsEvent(List<String> mowl){
		mapObjectIds.addAll(mowl);
	}
	
	public List<String> getMapObjectIds() {
		return mapObjectIds;
	}

	public void setMapObjects(List<String> mapObjectIds) {
		this.mapObjectIds = mapObjectIds;
	}

	public void addMapObject(MapObjectWrapper mapObjectWrapper) {
		mapObjectIds.add(mapObjectWrapper.getMapObject().getElementId());
	}
	
	public void addMapObject(String mowId) {
		if (mowId==null)return;
		mapObjectIds.add(mowId);
	}
	
	public String toString(){
		return "RemoveMapObjectsEvent:"+mapObjectIds;
	}

}


