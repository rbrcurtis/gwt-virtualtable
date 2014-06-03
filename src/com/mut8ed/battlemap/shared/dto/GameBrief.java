package com.mut8ed.battlemap.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GameBrief implements IsSerializable {

	private String description;
	private int nid;
	private String title;
	
	public GameBrief(){}
	
	public GameBrief(String description, int nid, String title) {
		super();
		this.description = description;
		this.nid = nid;
		this.title = title;
	}

	public String getDescription() {
		return description;
	}
	
	public int getNid() {
		return nid;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setNid(int nid) {
		this.nid = nid;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String toString(){
		return nid+":"+title+":"+description;
	}
	
	
}
