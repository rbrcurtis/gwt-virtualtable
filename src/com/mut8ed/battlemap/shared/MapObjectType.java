/**
 * 
 */
package com.mut8ed.battlemap.shared;

public enum MapObjectType {
	FIGURE, TILE, NOTE, DECAL;

	public boolean equalsAny(MapObjectType... types) {
		for (MapObjectType right : types){
			if (right.equals(this))return true;
		}
		return false;
	}
}