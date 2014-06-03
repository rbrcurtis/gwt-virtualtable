package com.mut8ed.battlemap.client;

import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.mut8ed.battlemap.client.util.Jq;
import com.mut8ed.battlemap.client.util.MTAsyncCallback;
import com.mut8ed.battlemap.client.util.MapKeyboardHandler;
import com.mut8ed.battlemap.client.view.AddMapObjectView;
import com.mut8ed.battlemap.client.view.CharacterChooser;
import com.mut8ed.battlemap.client.view.CharacterView;
import com.mut8ed.battlemap.client.view.InvitedView;
import com.mut8ed.battlemap.client.view.LandingView;
import com.mut8ed.battlemap.client.view.ListGamesView;
import com.mut8ed.battlemap.client.view.MapListView;
import com.mut8ed.battlemap.client.view.MapView;
import com.mut8ed.battlemap.client.view.SelectFigureView;
import com.mut8ed.battlemap.client.view.TestingView;
import com.mut8ed.battlemap.client.widget.ChatMenu;
import com.mut8ed.battlemap.client.widget.ChatPanel;
import com.mut8ed.battlemap.client.widget.PlayerPanel;
import com.mut8ed.battlemap.shared.dto.ChatMessage;
import com.mut8ed.battlemap.shared.event.ChatEvent;
import com.mut8ed.battlemap.shared.event.JoinEvent;
import com.mut8ed.battlemap.shared.event.MapEvent;
import com.mut8ed.battlemap.shared.event.RedirectEvent;
import com.mut8ed.battlemap.shared.event.WelcomeEvent;
import com.mut8ed.battlemap.shared.exception.StopEventLoopException;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BattleMap implements EntryPoint, ValueChangeHandler<String> {

	public static boolean debug = true;
	public static EventBusAsync eventBus;
	private static boolean stopEventsLoop = false;
	public final static TextArea alertsBox;
	private static final FlowPanel body;
	private static final FlowPanel kbhandler;


	static {
		alertsBox = new TextArea();
		alertsBox.setReadOnly(true);
		alertsBox.setText("Mystic Triad Games");
		alertsBox.getElement().setId("alerts");

		RootPanel root = RootPanel.get();

		body = new FlowPanel();
		body.getElement().setId("content");

		/*
		 * the kbhandler is a div that is out of the heirarchy of the standard view
		 * the reason for this is because GWT is fucking annoying in that you can't
		 * attach kb events to the window, and kb events only work on elements
		 * that can receive focus, which ISNT tables, divs, etc.
		 * 
		 * so we are creating this div so that we can attach events to it, then we'll
		 * send all document kb events to this handler.
		 */
		kbhandler = new FlowPanel();
		kbhandler.getElement().setId("kbhandler");

		MapKeyboardHandler mapEditorKeyBoardHandler = MapKeyboardHandler.getInstance();
		kbhandler.addDomHandler(mapEditorKeyBoardHandler, KeyDownEvent.getType());
		kbhandler.addDomHandler(mapEditorKeyBoardHandler, KeyUpEvent.getType());
		kbhandler.addDomHandler(mapEditorKeyBoardHandler, KeyPressEvent.getType());

		root.add(kbhandler);
		root.add(body);
		root.add(alertsBox);

	}
	public static void add(Widget widget){
		body.add(widget);
	}

	public static void debug(String obj){
		debug(obj.toString(), null);
	}

	public static void debug(String string, Throwable throwable) {
		if (!debug)return;
		Jq.log(string);
		if (throwable!=null){
			GWT.log(string, throwable);
			for (Object thing : throwable.getStackTrace()){
				Jq.log(thing.toString());
			}
		}
		//		alertsBox.setText(alertsBox.getText()+"\n"+string);
		//		alertsBox.getElement().setScrollTop(alertsBox.getElement().getScrollHeight());
	}

	public static void remove(Element elem) {
		DOM.removeChild(body.getElement(), elem);
	}

	public static void remove(Widget widget) {
		body.remove(widget);
	}

	public static void setMessage(String msg) {
		alertsBox.setText(msg);
		alertsBox.getElement().setScrollTop(alertsBox.getElement().getScrollHeight());
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable throwable) {
				debug(throwable.getMessage(), throwable);
			}
		});

		// use a deferred command so that the handler catches onModuleLoad2() exceptions
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

			@Override
			public void execute() {
				eventBus = GWT.create(EventBus.class);
				GWT.log("event bus created:"+eventBus);

				History.addValueChangeHandler(BattleMap.this);

				if (History.getToken()!=null && !History.getToken().trim().equals("")){

//					debug("going to "+History.getToken());
					loadPage(History.getToken());


				} else if (Window.Location.getParameter("page")!=null){
//					debug("loading by page param");
					try {
						Page page = Page.valueOf(Window.Location.getParameter("page").toUpperCase());
						String url = "https://mystictriad.com/tools/#"+page.name().toLowerCase();
						//						String url = Window.Location.getHref();
						//						url = url.replaceAll("(\\&|\\?)page=[a-zA-Z_]+","");
						//						url = url.replace("//", "/").replace("http:/", "http://");
						//						url += "#"+page.name().toLowerCase();
//						debug("redirecting to "+url);
						Window.Location.replace(url);
						return;
					} catch (Exception e) {
						debug(e.getMessage(), e);
						add(new Label("the page you are trying to access does not exist."));
					}
				} else {

					loadPage("CHAT");

				}

				//		sign out when window closes so the server stops trying to keep track of us
				Window.addCloseHandler(new CloseHandler<Window>() {

					@Override
					public void onClose(CloseEvent<Window> event) {
						BattleMap.eventBus.disconnect(new MTAsyncCallback<Void>());
					}
				});
				//some browsers (chrome, safari) will continue to show "loading"
				//if you start the getEvents loop right away.  we dont have access to
				//body.onload, so we have to use this timer to allow the page to load 
				//completely, and then start the getEvents loop after that has finished.
				new Timer(){
					@Override
					public void run() {
						getEventsLoop();
					}

				}.schedule(250);

			}
		});
	}

	private void getEventsLoop() {


//		debug("getting events");

		BattleMap.eventBus.getEvents(new AsyncCallback<List<MapEvent>>() {

			@Override
			public void onFailure(Throwable caught) {

				if (caught instanceof StatusCodeException){
					int statusCode = ((StatusCodeException)caught).getStatusCode();

					if (statusCode==0){
						//BattleMap.debug("Communication with server interrupted (statusCode "+statusCode+").");
						BattleMap.eventBus.getEvents(this);
					}
					else {
						BattleMap.setMessage("Communication with server interrupted (statusCode "+statusCode+").  Will retry in 5.");
						new Timer() {
							@Override
							public void run() {
								Window.Location.reload();
							}
						}.schedule(5000);
					}
				}
			}

			@Override
			public void onSuccess(List<MapEvent> mapEvents) {

				try {
					if (mapEvents.size()>0){
						//BattleMap.debug("***** got events:"+mapEvents);
						for (MapEvent mapEvent : mapEvents){

							handleEvent(mapEvent);

							if (stopEventsLoop) {
								//BattleMap.debug("stop events loop called");
								stopEventsLoop = false;
								return;
							}
						}
					}
					BattleMap.eventBus.getEvents(this);

				} catch (StopEventLoopException e) {

				}
			}
		});


	}

	/**
	 * handle the given event.  usually this means update the map.
	 * @param mapEvent
	 */
	private void handleEvent(MapEvent mapEvent) throws StopEventLoopException {

		try {
			if (mapEvent instanceof WelcomeEvent){
				
				BattleMap.setMessage("Connection Established.");
				
			} else if (mapEvent instanceof RedirectEvent){
				
				((RedirectEvent)mapEvent).getPage().redirect();
				return;
				
			} else if (mapEvent instanceof JoinEvent){
				if (DOM.getElementById("chatPanel")!=null){
					PlayerPanel.getInstance().setupAvatars();
				}
			} else if (mapEvent instanceof ChatEvent){
				
				if (DOM.getElementById("chatPanel")!=null){
					ChatEvent event = (ChatEvent)mapEvent;
					ChatMessage cm = event.getChatMessage();
					ChatPanel.getInstance().add(cm);
				}
			} else {
				
				if (DOM.getElementById("mapView")!=null){
					MapView.getInstance().handleEvent(mapEvent);
				}
				
			}

		} catch (Exception e) {
			if (e instanceof StopEventLoopException)throw (StopEventLoopException)e;
			Window.alert(e.toString());
			//BattleMap.debug(e.getMessage(), e);
		}

	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		debug("page changed to "+event.getValue());
		Window.Location.reload();
//		loadPage(event.getValue());
	}


	private void loadPage(String shebang) {
//		debug("loading page "+shebang);
		body.clear();

		String extra = null;
		shebang = shebang.toUpperCase();
		if (shebang.contains("/")){
			extra = shebang.substring(shebang.indexOf('/')+1);
			shebang = shebang.substring(0,shebang.indexOf('/'));
		}

		Window.setTitle("MysticTriad - "+capFirst(shebang));

		try {
			Page page = Page.valueOf(shebang);

			switch (page){

			case TEST:
				body.add(new TestingView());
				break;

			case INVITE:
				body.add(new InvitedView(extra));
				break;

			case LOGIN:
				String hash = History.getToken();
				if (hash.toLowerCase().equals("login"))hash="landing";
				String host = Window.Location.getHostName();
				String path = Window.Location.getPath();
				if (path.startsWith("/"))path = path.substring(1);
				String target = "http://"+host+"/user/login?destination="+path+"?page="+hash;
				Window.Location.assign(target);

				return;

			case MAP:
				body.add(MapView.getInstance());
				break;

			case LANDING:
				body.add(new LandingView());
				break;

//			case CREATE_GAME:
//				body.add(new CreateGameView());
//				break;

			case LIST_GAMES:
				body.add(new ListGamesView());
				break;

			case SELECT_FIGURE:
				body.add(new SelectFigureView());
				break;

			case ADD_MAP_OBJECT:
				body.add(AddMapObjectView.getInstance());
				break;

			case MAP_LIST:
				body.add(new MapListView());
				break;

			case CHARACTER:
				body.add(CharacterView.getInstance());
				body.add(CharacterChooser.getInstance());
				if (!CharacterView.getInstance().isVisible()){
					Jq.show(CharacterView.getInstance().getElement().getId(), "fast");
				}
				break;

			case CHAT:
				body.add(new ChatMenu());
				body.add(ChatPanel.getInstance());
				body.add(CharacterView.getInstance());
				CharacterView.getInstance().showCharacter("default");
				break;
			}
		} catch (Exception e) {
//			debug(e.toString(), e);
		}

	}

	private String capFirst(String str) {
		if (str==null || str.length()==0)return str;
		return str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
	}

	public static void stopEventsLoop() {
		stopEventsLoop  = true;
	}

}
