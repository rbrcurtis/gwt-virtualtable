//package com.mut8ed.battlemap.client.view;
//
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.Composite;
//import com.google.gwt.user.client.ui.FormPanel;
//import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
//import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.TextArea;
//import com.google.gwt.user.client.ui.TextBox;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.mut8ed.battlemap.client.BattleMap;
//import com.mut8ed.battlemap.client.Page;
//import com.mut8ed.battlemap.client.util.MTAsyncCallback;
//
//public class CreateGameView extends Composite implements SubmitHandler {
//
//
//	private TextBox title;
//	private TextArea description;
//
//	public CreateGameView(){
//
//		final FormPanel form = new FormPanel();
//		VerticalPanel body = new VerticalPanel();
//		body.setStylePrimaryName("createGamePanel");
//
//		body.add(new Label("Game Title"));
//		title = new TextBox();
//		title.setStylePrimaryName("gameTitle");
//		body.add(title);
//
//		body.add(new Label("Game Description"));
//		description = new TextArea();
//		description.setStylePrimaryName("gameDesc");
//		body.add(description);
//
//		//TODO add players
//
//		Button submit = new Button("Create Game",new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				form.submit();
//			}
//		});
//
//		body.add(submit);
//
//		form.add(body);
//
//		form.addSubmitHandler(this);
//
//		this.initWidget(form);
//
//
//	}
//
//	@Override
//	public void onSubmit(SubmitEvent event) {
//		
//		BattleMap.setMessage(title.getText()+":"+description.getText());
//		
//		BattleMap.eventBus.createGame(
//				title.getText(), 
//				description.getText(), 
//				new MTAsyncCallback<Void>("create game failed") {
//
//					@Override
//					public void onSuccess(Void result) {
//						Page.MAP_LIST.redirect();
//					}
//				}
//		);
//	}
//}
