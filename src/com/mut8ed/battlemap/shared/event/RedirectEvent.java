package com.mut8ed.battlemap.shared.event;

import com.mut8ed.battlemap.client.Page;

public class RedirectEvent extends MapEvent {

	private Page page = null;

	public RedirectEvent(Page page) {
		this.page = page;
	}
	
	public RedirectEvent(){}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "RedirectEvent [page=" + page + "]";
	}

}
