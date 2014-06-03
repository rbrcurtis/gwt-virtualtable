package com.mut8ed.battlemap.shared.event;

public class SetEditableEvent extends MapEvent {

	private boolean isEditable = false;
	
	public SetEditableEvent(){}
	
	public SetEditableEvent(boolean isEditable){
		this.isEditable = isEditable;
	}
	
	public boolean isEditable() {
		return isEditable;
	}

	@Override
	public String toString(){
		String className = this.getClass().getName();
		className = className.substring(className.lastIndexOf(".")+1);
		return className+":editable="+isEditable;
	}
	
}
