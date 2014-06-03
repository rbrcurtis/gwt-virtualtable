package com.mut8ed.battlemap.client.widget;

import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mut8ed.battlemap.client.BattleMap;
import com.mut8ed.battlemap.client.util.ClientUtil;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.view.ListGamesView;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.shared.dto.ChatMessage;

public class ChatPanel extends VerticalPanel {

	private static ChatPanel instance = null;
	//	private PlayerPanel playerPanel;
	private FlowPanel chatWindow;
	private TextArea input;

	private ChatPanel(){
		//BattleMap.debug("creating chatpanel");
		this.getElement().setId("chatPanel");

		PlayerPanel playerPanel = PlayerPanel.getInstance();
		add(playerPanel);
		this.setCellHorizontalAlignment(playerPanel.getElement(), ALIGN_CENTER);

		chatWindow = new FlowPanel();
		//		chatWindow.setEnabled(false);
		chatWindow.getElement().setId("chatWindow");

		class ChatFocuser implements MouseDownHandler, MouseUpHandler {

			int startX, startY;

			@Override
			public void onMouseUp(MouseUpEvent event) {
//				BattleMap.debug("chatFocus:"+startX+"/"+event.getX()+":"+startY+"-"+event.getY());
				if (
						Math.abs(event.getX()-startX)<=5 &&
						Math.abs(event.getY()-startY)<=5
						){
					input.setFocus(true);
				}
			}

			@Override
			public void onMouseDown(MouseDownEvent event) {
				startX = event.getX();
				startY = event.getY();
			}

		}
		ChatFocuser cf = new ChatFocuser();
		chatWindow.addDomHandler(cf, MouseDownEvent.getType());
		chatWindow.addDomHandler(cf, MouseUpEvent.getType());

		add(chatWindow);

		input = new TextArea();
		input.getElement().setId("chatInput");

		ClientUtil.disableKbOnFocus(input);

		input.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode()==13 && !event.isShiftKeyDown()){
					chat(input.getValue());
					input.setValue("");
				} else if (event.getNativeKeyCode()==27){//ESC
					MapView.getInstance().focusPanel.setFocus(true);
				}
			}
		});


		add(input);

		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				ChatPanel.this.resetHeight();
			}
		});

		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

			@Override
			public void execute() {
				ChatPanel.this.resetHeight();

				BattleMap.eventBus.joinChat(new MTAsyncCallback<List<ChatMessage>>(){

					@Override
					public void onSuccess(List<ChatMessage> result) {
						PlayerPanel.getInstance().setupAvatars();
						if (result==null){
							ListGamesView lgv = new ListGamesView();
							BattleMap.add(lgv);
							DOM.setStyleAttribute(lgv.getElement(), "height", ChatPanel.getInstance().getOffsetHeight()+"px");
							lgv.showCloseButton(true);

							return;
						}
						for (ChatMessage cm : result){
							add(cm);
						}
					}

				});


			}
		});

	}

	public void resetHeight() {
		int height = Window.getClientHeight();
		height -= PlayerPanel.getInstance().getOffsetHeight();
		int menuHeight = 30;
		height -= menuHeight;
		height -= DOM.getElementById("alerts").getOffsetHeight();
		DOM.setStyleAttribute(getElement(), "height", height+"px");
		DOM.setStyleAttribute(
				chatWindow.getElement(), 
				"height", 
				(height-input.getOffsetHeight())+"px"
				);
		//BattleMap.debug("chatwindow:"+(height-input.getOffsetHeight()));
		//BattleMap.debug("chatpanel:"+(height));
	}

	public static ChatPanel getInstance() {
		if (instance == null){
			instance = new ChatPanel();
		}
		return instance ;
	}

	public void add(ChatMessage cm) {
		for (String msg : cm.message.split("\n")){
			if (msg.trim().length()==0)continue;
			Label label = (cm.username!=null)?new Label(cm.username+": "+msg) : new Label(msg);
			label.addStyleName(cm.color);
			chatWindow.add(label);
		}
//		chatWindow.add(new HTML("&nbsp;\n"));
		chatWindow.getElement().setScrollTop(chatWindow.getElement().getScrollHeight());
	}

	private void chat(String msg){
		BattleMap.eventBus.chat(msg, new MTAsyncCallback<Void>());
	}

	public void focus(){
		input.setFocus(true);
	}

}
