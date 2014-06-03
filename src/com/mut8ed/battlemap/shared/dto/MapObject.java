package com.mut8ed.battlemap.shared.dto;

import java.io.Serializable;

import com.mut8ed.battlemap.shared.MapObjectType;


/**
 * abstract class for figures, notes and tiles to extend
 * @author ryan
 *
 */
public abstract class MapObject implements Serializable {
	private static final long serialVersionUID = 1L;

	protected String id;
	private String elementId;

	public String getElementId(){
		if (elementId==null){
			this.elementId = getMapObjectType()+"-"+Integer.toHexString((int)(1000000*Math.random()));
		}
		return elementId;
	}

	public MapObject(){
		getElementId();
	}
	
	public void setElementId(String eid){
		this.elementId = eid;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public abstract MapObjectType getMapObjectType();

	public abstract MapObject clone();

}
