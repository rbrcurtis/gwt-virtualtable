package com.mut8ed.battlemap.client.view;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.Jq;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterBrief;

public class CharacterChooser extends FlowPanel {

	private static CharacterChooser instance = null;
	private Label closeButton;

	private CharacterChooser(){

		setStylePrimaryName("characterChooser");
		getElement().setId("characterChooser");

		resetHeight();
		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				resetHeight();
			}
		});

		build();

	}

	private void resetHeight() {
		setHeight((Window.getClientHeight()-DOM.getElementById("alerts").getClientHeight())+"px");		
	}

	public void build() {


		BattleMap.eventBus.getCharacterList(new MTAsyncCallback<Map<String,CharacterBrief>>(){

			@Override
			public void onSuccess(Map<String,CharacterBrief> result) {
				//BattleMap.debug("received "+result);

				clear();
				if (closeButton!=null)showCloseButton(true);

				Anchor addNew = new Anchor("Create New Character");
				addNew.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						CharacterView.getInstance().showCharacter(null);
						if (closeButton!=null){
							hide();
							closeButton.removeFromParent();
						}

					}
				});
				add(addNew);

				add(new Label(""));//empty space

				for (final Entry<String, CharacterBrief> cb : result.entrySet()){


					Anchor ca = new Anchor(cb.getValue().toString().toLowerCase());
					ca.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {

							CharacterView.getInstance().showCharacter(cb.getValue().id);
							if (closeButton!=null){
								hide();
								closeButton.removeFromParent();
							}

						}
					});
					add(ca);

				}

			}

		});
	}

	public static CharacterChooser getInstance() {
		if (instance==null)instance = new CharacterChooser();
		return instance;
	}

	public void show() {
		Jq.show(this.getElement().getId(), "fast");
	}

	public void hide(){
		Jq.hide(this.getElement().getId(), "fast");
	}

	public void showCloseButton(boolean show) {
		if (show){
			closeButton = new Label("X");
			closeButton.setStylePrimaryName("sliderCloseButton");
			closeButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					hide();
					closeButton.removeFromParent();
					closeButton = null;
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
