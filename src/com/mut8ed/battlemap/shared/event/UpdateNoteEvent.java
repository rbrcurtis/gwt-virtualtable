package com.mut8ed.battlemap.shared.event;

import com.mut8ed.battlemap.shared.dto.Note;

public class UpdateNoteEvent extends MapEvent {

	private Note note;

	public UpdateNoteEvent(){}
	
	public UpdateNoteEvent(Note note) {
		this.note = note;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}
	
}
