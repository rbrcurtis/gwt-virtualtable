package com.mut8ed.battlemap.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.Page;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.shared.dto.GameBrief;

public class ListGamesView extends FlowPanel {

	private List<GameBrief> gameList;
	private Label closeButton;

	public ListGamesView(){
		
		setStyleName("listGamesView");

		BattleMap.eventBus.getGameList(new MTAsyncCallback<List<GameBrief>>("there was a problem getting the game list") {


			@Override
			public void onSuccess(List<GameBrief> result) {
				gameList = result;
				populateGameList();
			}

		});



		resetHeight();
		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				resetHeight();
			}
		});


	}

	private void resetHeight() {
		setHeight((Window.getClientHeight()-DOM.getElementById("alerts").getClientHeight())+"px");		
	}

	private void populateGameList() {
		if (gameList==null || gameList.size()==0){
			
			HTML link = new HTML("No games found. Would you like to <a href=\"/node/add/group\">create a new one</a>?");
			add(link);
			return;
		}


		for (final GameBrief game : gameList){
			Anchor link = new Anchor();
			link.setStyleName("gameListLink");

			if (game.getDescription()!=null){
				link.setText(game.getTitle());
				link.setTitle(game.getDescription());
			} else {
				link.setText(game.getTitle());
				link.setTitle(game.getTitle());
			}
			
			link.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					BattleMap.eventBus.chooseGame(game.getNid(), new MTAsyncCallback<String>("there was a problem selecting your game") {

						@Override
						public void onSuccess(String result) {
							//BattleMap.debug("choose game returned "+result);
							
							if (result.equals("success")){
								if (History.getToken().toUpperCase().equals(Page.CHAT.name()))Window.Location.reload();
								Page.CHAT.redirect();
							}
						}

					});
				}

			});


			add(link);

		}
	}

	public void showCloseButton(boolean show) {
		if (show){
			closeButton = new Label("X");
			closeButton.setStylePrimaryName("sliderCloseButton");
			closeButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					ListGamesView.this.removeFromParent();
					closeButton.removeFromParent();
				}
			});
			insert(closeButton, 0);
		} else {
			if (closeButton!=null && closeButton.isAttached()){
				closeButton.removeFromParent();
			}
		}
	}
}
