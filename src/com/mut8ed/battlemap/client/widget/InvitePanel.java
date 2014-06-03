//package com.mut8ed.battlemap.client.widget;
//
//import com.google.gwt.event.dom.client.KeyCodes;
//import com.google.gwt.event.dom.client.KeyUpEvent;
//import com.google.gwt.event.dom.client.KeyUpHandler;
//import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.ui.DialogBox;
//import com.google.gwt.user.client.ui.TextBox;
//import com.mut8ed.battlemap.client.BattleMap;
//import com.mut8ed.battlemap.client.util.ClientUtil;
//import com.mut8ed.battlemap.client.util.MTAsyncCallback;
//
//public class InvitePanel extends DialogBox {
//
//	private TextBox input;
//
//	public InvitePanel(){
//		super(true, true);
//
//		this.getElement().setId("inviter");
//
//		this.setText("Email or Usernames, seperated by commas");
//
//		input = new TextBox();
//		ClientUtil.disableKbOnFocus(input);
//		input.setStylePrimaryName("menuTextInput");
//		DOM.setStyleAttribute(input.getElement(), "width", "100%");
//
//		input.addKeyUpHandler(new KeyUpHandler() {
//
//			@Override
//			public void onKeyUp(KeyUpEvent event) {
//				if (event.getNativeKeyCode()==KeyCodes.KEY_ENTER){
//					BattleMap.eventBus.invite(input.getValue(), new MTAsyncCallback<String>("Your invite request failed.  Please try again.") {
//
//						@Override
//						public void onSuccess(String result) {
//							input.setValue("");
//
//							hide();
//
//							Window.alert(result);
//						}
//					});
//				} else if (event.getNativeKeyCode()==KeyCodes.KEY_ESCAPE){
//					event.stopPropagation();
//					event.preventDefault();
//				}
//				
//			}
//		});		
//
//		add(input);
//	}
//
//	public void focus(){
//		input.setFocus(true);
//	}
//
//	@Override
//	public boolean onKeyDownPreview(char key, int modifiers) {
//		if (key == KeyCodes.KEY_ESCAPE){
//			hide();
//			return false;
//		}
//
//		return true;
//	}
//}
