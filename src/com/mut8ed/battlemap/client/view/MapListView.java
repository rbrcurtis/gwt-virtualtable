package com.mut8ed.battlemap.client.view;

import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.Page;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;

public class MapListView extends FlexTable {


	public MapListView(){

		getElement().setId("mapList");

		BattleMap.eventBus.getMapList(new MTAsyncCallback<Map<String,String[]>>("call to server to retrieve Map List failed") {

			Label label = null;
			AsyncCallback<Void> callback = new MTAsyncCallback<Void>(){

				@Override
				public void onSuccess(Void result) {
					MapView.getInstance().clear();
					Page.MAP.redirect();
				}

			};

			@Override
			public void onSuccess(Map<String, String[]> result) {

				int i = 0;
				for (final Map.Entry<String, String[]> e : result.entrySet()){

					for (int j=0;j<e.getValue().length;j++){
						label = new Label(e.getValue()[j]);
						label.setStyleName("mapListEntry");
						label.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								BattleMap.eventBus.switchMap(e.getKey(), callback);
							}
						});

						setWidget(i,j, label);
					}

					i++;
				}

				label = new Label("Create New Map");
				label.setStyleName("mapListEntry");
				label.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						BattleMap.eventBus.switchMap(null, callback);
					}
				});

				setWidget(i,0, label);
				getFlexCellFormatter().setColSpan(i, 0, 3);

			}
		});

	}
}
