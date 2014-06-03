package com.mut8ed.battlemap.client.widget;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.shared.dto.PlayerModel;

public class PlayerPanel extends HorizontalPanel {

	private static PlayerPanel instance;
	private Map<String, Object[]> players = new LinkedHashMap<String, Object[]>();
	
	private PlayerPanel(){	
		getElement().setId("playerPanel");
		this.setHorizontalAlignment(ALIGN_CENTER);
	}
	
	public void setupAvatars() {
		BattleMap.eventBus.getPlayers(new MTAsyncCallback<List<PlayerModel>>(){

			@Override
			public void onSuccess(List<PlayerModel> result) {
				
				clear();
				
				if (result==null)return;
				
				PlayerPanel.this.clear();
				for (PlayerModel player : result){
					//BattleMap.debug("adding player "+player.userName);
					Image image = new Image(player.gravatarUrl);
					image.setHeight("32px");
					image.setWidth("32px");
					image.setStylePrimaryName("playerAvatar");
					image.setTitle(player.userName);
					image.addStyleName(player.color);
					add(image);
					players.put(player.userName, new Object[]{player, image});
				}
				ChatPanel.getInstance().resetHeight();
			}
		});
	}

	public static PlayerPanel getInstance(){
		if (instance==null)instance = new PlayerPanel();
		return instance;
	}
	
	
}
