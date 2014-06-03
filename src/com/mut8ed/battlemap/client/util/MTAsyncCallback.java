package com.mut8ed.battlemap.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.Page;

public class MTAsyncCallback<T> implements AsyncCallback<T> {

	private String failMsg = "The request failed. ";
	
	public MTAsyncCallback(){}
	
	public MTAsyncCallback(String failMsg) {
		this.failMsg = failMsg;
	}

	@Override
	public void onFailure(Throwable caught) {
		if (caught instanceof StatusCodeException){
			StatusCodeException e = (StatusCodeException)caught;
			if (e.getStatusCode()==0)return;
		}
		if (caught.getMessage()!=null && caught.getMessage().contains("401")){
			Page.LOGIN.redirect();
		} else {
			//BattleMap.debug(failMsg+caught.toString(), caught);
		}
	}

	@Override
	public void onSuccess(T result) {
		
	}

}
