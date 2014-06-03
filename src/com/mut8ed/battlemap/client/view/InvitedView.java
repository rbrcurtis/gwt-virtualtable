package com.mut8ed.battlemap.client.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;

public class InvitedView extends VerticalPanel {
	
	private String code;

	public InvitedView(String code) {
		
		this.code = code;
		
		add(new Label("Your invitation code is "+this.code));
		BattleMap.eventBus.handleInvite(code, new MTAsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if (result){
//					Page.MAP.redirect();
					add(new Label("Dude, your invite code is all good."));
				} else {
					add(new Label("I'm sorry, your invitation code is invalid."));
				}
			}
			
		});
		
	}

}
