package com.mut8ed.battlemap.shared.dto;

import com.mut8ed.battlemap.shared.MapObjectType;

public class Note extends MapObject {
	private static final long serialVersionUID = 1L;

	private String note;
	
	
	public Note() {
		super();
	}

	public Note(String note){
		this.note = note;
	}
	
	public Note(String id, String note){
		super();
		this.id = id;
		this.note = note;
	}

	@Override
	public MapObject clone() {
		return new Note(id, note);
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public MapObjectType getMapObjectType() {
		return MapObjectType.NOTE;
	}
}
