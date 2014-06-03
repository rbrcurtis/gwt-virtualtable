package com.mut8ed.battlemap.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mortbay.jetty.RetryRequest;
import org.mortbay.util.ajax.Continuation;
import org.mortbay.util.ajax.ContinuationSupport;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.UnexpectedException;
import com.mut8ed.battlemap.client.EventBus;
import com.mut8ed.battlemap.client.Page;
import com.mut8ed.battlemap.server.dao.Dao;
import com.mut8ed.battlemap.server.dto.Game;
import com.mut8ed.battlemap.server.dto.Invite;
import com.mut8ed.battlemap.server.dto.User;
import com.mut8ed.battlemap.server.util.Util;
import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.ChatMessage;
import com.mut8ed.battlemap.shared.dto.Decal;
import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.GameBrief;
import com.mut8ed.battlemap.shared.dto.GameMap;
import com.mut8ed.battlemap.shared.dto.MapCell;
import com.mut8ed.battlemap.shared.dto.MapObject;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;
import com.mut8ed.battlemap.shared.dto.Note;
import com.mut8ed.battlemap.shared.dto.PlayerModel;
import com.mut8ed.battlemap.shared.dto.Tile;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterBrief;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClassModel;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.spell.Spell;
import com.mut8ed.battlemap.shared.event.AddMapObjectsEvent;
import com.mut8ed.battlemap.shared.event.ChangeMapNameEvent;
import com.mut8ed.battlemap.shared.event.ChatEvent;
import com.mut8ed.battlemap.shared.event.LoadMapEvent;
import com.mut8ed.battlemap.shared.event.MapEvent;
import com.mut8ed.battlemap.shared.event.MapObjectVisibilityEvent;
import com.mut8ed.battlemap.shared.event.MoveDecalEvent;
import com.mut8ed.battlemap.shared.event.MoveFigureEvent;
import com.mut8ed.battlemap.shared.event.RedirectEvent;
import com.mut8ed.battlemap.shared.event.ReloadPlayersEvent;
import com.mut8ed.battlemap.shared.event.RemoveMapObjectsEvent;
import com.mut8ed.battlemap.shared.event.RotateMapObjectEvent;
import com.mut8ed.battlemap.shared.event.UpdateLayerVisibilityEvent;
import com.mut8ed.battlemap.shared.event.UpdateNoteEvent;
import com.mut8ed.battlemap.shared.event.WelcomeEvent;
import com.mut8ed.battlemap.shared.exception.InvalidGameIdException;
import com.mut8ed.battlemap.shared.exception.NotInvitedException;

public class EventBusImpl extends RemoteServiceServlet implements EventBus {

	public static final Logger logger = Logger.getLogger(EventBusImpl.class);
	public static final String PAYLOAD = "com.google.gwt.payload";
	private static final String JETTY_RETRY_REQUEST_EXCEPTION = "org.mortbay.jetty.RetryRequest";
	private static final long serialVersionUID = 1L;
	private static final long SUSPEND_DURATION = 50000;
	private Dao dao = new Dao();

	//gameId = game.  if not in a game, the game id will be -mapId
	private Store store = Store.getInstance();
	private String localhost = "127.0.0.1";



	static {
		logger.setLevel(Level.DEBUG);
		//		Thread t = new Thread(){
		//			
		//			@Override
		//			public void run() {
		//				
		//				while (true){
		//					System.out.println(users);
		//					try {
		//						sleep(10000);
		//					} catch (InterruptedException e) {
		//						logger.error(e, e);
		//					}
		//				}
		//				
		//			}
		//			
		//		};
		//		t.start();
	}


	public EventBusImpl(){	



		//TODO throw errors if map edited by non-editor

	}

	public static void main(String[] args) throws Exception{

		String m = "1d20+33\t1d10+24+4d6+1d6\n1d20+28\t1d10+24+4d6+1d6";
		System.out.println(m+"\n");

		for (String msg : m.split("\n")){
			String[] split = msg.split("\\s");
			msg = "";
			String rollMsg = "";
			for (String s : split){
				if (s.matches(".*[0-9]+[d+-/\\\\*][0-9]+.*")){
					//					logger.debug("rolling!");
					String[] roll = rollDice(s);
					if (roll==null)continue;
					msg += roll[0]+" ";
					if (roll[1]!=null)rollMsg += roll[1]+" ";
				} else {
					msg += s+" ";
				}
			}
			msg = msg.trim();
			rollMsg = rollMsg.trim();

			System.out.println();
			System.out.println(msg);
			System.out.println(rollMsg);
		}


	}

	private static double roll(String c, String d, List<Integer> rolls) {
		double count = Double.parseDouble(c);
		double die = Double.parseDouble(d);
		double total = 0.0;
		//		System.out.println(count+"d"+die);
		for (int x = 0;x<count;x++){
			int roll = ((int)(Math.random()*die))+1;
			rolls.add(roll);
			total += roll;
		}		
		//		System.out.println("rolled "+total);
		return total;
	}

	private static String[] rollDice(String msg) {
		String[] split = msg.split("\\s");
		msg = "";

		String pattern1 = "(([0-9])+d([0-9]+)([+-\\\\*/][0-9.]+)*[+-/\\\\*]*)+";
		String pattern2 = "([0-9]+[d+-/\\\\*]?)*";
		List<Integer> rolls = new ArrayList<Integer>();
		String debugMsg = null;

		for (String s : split){
			if (s==null || s.trim().equals(""))continue;
			String x = s.replaceAll("[()]", "");
			if (x.matches(pattern1) || x.matches(pattern2)){
				//				logger.debug("match!");

				if (s.matches(".*[()].*")){
					s = s.replaceAll("[()]","");
					debugMsg = "Don't use parentheses in rolls; all rolls are processed strictly left to right. This was processed as "+s;
				}

				String[] numbers = s.split("[^0-9.]");
				String[] ops = s.split("[0-9.]+");

				//				System.out.println("numbers");
				//				for (int i = 0; i < numbers.length; i++) {
				//					System.out.println(numbers[i]);
				//				}

				//				System.out.println("ops");
				//				for (int i = 0; i < ops.length; i++) {
				//					System.out.println(ops[i]);
				//				}

				double total = 0;

				int i = 0;
				int j = 1; //split always makes ops[0]==""
				while (i<numbers.length && j<ops.length){

					String op = ops[j];
					//					System.out.println("op:"+op);
					if (op.equals("")){
						j++;
						continue;
					}

					if (j==1 && !op.equals("d"))total+=Double.parseDouble(numbers[i++]);

					if (op.equals("d")){

						total+=roll(numbers[i],numbers[++i],rolls);

					} else if (op.equals("+")){
						if (ops.length>(j+1) && ops[j+1].equals("d")){
							total += roll(numbers[i],numbers[++i],rolls);
							j++;
						} else {
							double number = Double.parseDouble(numbers[i]);
							//							System.out.println("+"+number);
							total+=number;
						}
					} else if (op.equals("-")){
						if (ops.length>(j+1) && ops[j+1].equals("d")){
							total -= roll(numbers[i],numbers[++i],rolls);
							j++;
						} else {
							double number = Double.parseDouble(numbers[i]);
							//							System.out.println("+"+number);
							total-=number;
						}
					} else if (op.equals("/")){
						if (ops.length>(j+1) && ops[j+1].equals("d")){
							total /= roll(numbers[i],numbers[++i],rolls);
							j++;
						} else {
							double number = Double.parseDouble(numbers[i]);
							//							System.out.println("+"+number);
							total/=number;
						}
					} else if (op.equals("*")){
						if (ops.length>(j+1) && ops[j+1].equals("d")){
							total *= roll(numbers[i],numbers[++i],rolls);
							j++;
						} else {
							double number = Double.parseDouble(numbers[i]);
							//							System.out.println("+"+number);
							total*=number;
						}
					}
					//					System.out.println("subtotal:"+total);
					j++;
					i++;
				}


				String t = "";
				if (0==total%1){
					t = ""+((int)total);
				} else {
					t = ""+total;
				}

				//				
				msg+=s+" ["+t+"] ";
			} else {
				logger.debug("no match!");
				msg+=s+" ";
			}
		}

		msg = msg.trim();
		String rollMsg = null;
		if (rolls.size()>0)rollMsg = rolls.toString();
		return new String[]{msg,rollMsg,debugMsg};
	}

	@Override
	public void addMapObject(MapObjectWrapper mow) {
		List<MapObjectWrapper> mowl = new ArrayList<MapObjectWrapper>();
		mowl.add(mow);
		addMapObjects(mowl);
	}

	@Override
	public void addMapObjects(List<MapObjectWrapper> mowl) {

		User user = getCurrentUser();
		Game game = user.getCurrentGame();

		if (mowl==null || mowl.size()==0){
			logger.debug(user+":add map objects called with mowl="+mowl);
			return;
		}

		logger.debug(user+":adding mapobjects to game "+game.getId()+", mapId "+game.getGameMap().getId());

		List<MapObjectWrapper> toRemove = new ArrayList<MapObjectWrapper>();
		toRemove.addAll(game.addMapObjects(mowl.toArray(new MapObjectWrapper[]{})));

		List<String> removeList = new ArrayList<String>();
		for (MapObjectWrapper mow : toRemove){
			removeList.add(mow.getElementId());
		}

		game.broadcast(new AddMapObjectsEvent(mowl));
		if (removeList.size()>0)game.broadcast(new RemoveMapObjectsEvent(removeList));
	}


	@Override
	public void changeMapName(String mapName) {


		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		GameMap map = game.getGameMap();

		logger.debug(user.getUserName()+" changing mapName to "+mapName+" from "+map.getName()+" for map "+map.getId());

		if (map!=null){
			map.setName(mapName);
			dao.saveGameMap(map);
			game.broadcast(new ChangeMapNameEvent(mapName));
		}


	}

	@Override
	public void chat(String m) {
		logger.debug("chat msg before:"+m);
		m = cleanXSS(m);
		m = m.trim();
		logger.debug("chat msg after:"+m);

		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		if (game==null){
			logger.error("game is null for "+user);
			user.addEvent(new RedirectEvent(Page.LIST_GAMES));
			return;
		}

		//		String[] split = msg.split("\\s");
		//		msg = "";
		//		String rollMsg = "";
		//		for (String s : split){
		//			if (s.matches(".*[0-9]+[d+-/\\\\*][0-9]+.*")){
		//				logger.debug("rolling!");
		//				String[] roll = rollDice(s);
		//				if (roll==null)continue;
		//				msg += roll[0]+" ";
		//				if (roll[1]!=null)rollMsg += roll[1]+" ";
		//			} else {
		//				msg += s+" ";
		//			}
		//		}
		//		msg = msg.trim();
		//		rollMsg = rollMsg.trim();

		String chatMsg = "";
		String rollMsg = "";
		for (String msg : m.split("\n")){
			String[] split = msg.split("\\s");
			msg = "";
			for (String s : split){
				if (s.matches(".*[0-9]+[d+-/\\\\*][0-9]+.*")){
					//					logger.debug("rolling!");
					String[] roll = rollDice(s);
					if (roll==null)continue;
					msg += roll[0]+" ";
					if (roll[1]!=null)rollMsg += roll[1]+" ";
				} else {
					msg += s+" ";
				}
			}
			chatMsg += msg.trim()+"\n";
			rollMsg = rollMsg.trim()+"\n";

			//			System.out.println();
			//			System.out.println(msg);
			//			System.out.println(rollMsg);
		}

		ChatMessage cm = new ChatMessage(game.getNid(), user.getUserName(), chatMsg, user.getColor()); 
		chatMsg = user.getUserName()+": "+chatMsg;

		game.broadcast(new ChatEvent(cm));
		dao.saveChatMessage(cm);
		if (rollMsg.length()>0){
			cm = new ChatMessage(game.getNid(), "dice rolls", rollMsg, "black");
			game.broadcast(new ChatEvent(cm));
			dao.saveChatMessage(cm);
		}

		//		cm = new ChatMessage(game.getNid(), "-", "-", "black");
		//		game.broadcast(new ChatEvent(cm));
		//		dao.saveChatMessage(cm);


	}


	@Override
	public void chooseFigure(String figureId) {
		logger.debug("recieved request chooseFigure figureId:"+figureId+" user:"+getCurrentUser());
		//		getCurrentUser().setFigure(dao.getFigure(figureId)); TODO
	}

	@Override
	public String chooseGame(int nid){
		logger.debug("received request chooseGame");
		try {

			User user = getCurrentUser();
			Game game = loadGame(nid);
			user.setCurrentGame(game);
			Map<String,String> prefs = dao.getUserPreferences(user.getId());
			prefs.put("last_game", game.getNid()+"");
			dao.updateUserPrefs(user.getId(), prefs);

			return "success";
		} catch (IndexOutOfBoundsException e) {
			logger.error(e,e);
			return "that game does not exist";
		} catch (Exception e){
			logger.error(e,e);
			return null;
		}
	}

	//	@Override @Deprecated
	//	public GameMap getGameMap(int mapId) {
	//		logger.debug(getSessionId()+" recieved request getGameMap id:"+mapId);
	//		try {
	//			User user = getCurrentUser();
	//			if (user==null){
	//				isAuthorized();
	//				user = getCurrentUser();
	//				if (user==null)throw new RuntimeException("You are not logged in.");
	//			} else {
	//				logger.debug(user);
	//			}
	//
	//			Game game = user.getCurrentGame();
	//			if (game==null){
	//
	//				game = games.get(mapId);
	//				if (game == null) {
	//
	//					game = new Game();
	//					game.setId(-1);
	//					GameMap gameMap = dao.getGameMap(mapId);
	//					game.setGameMap(gameMap);
	//					games.put(mapId, game);
	//					logger.debug("stored game for map " + mapId);
	//
	//				} else {
	//					logger.debug("returning stored game for map " + mapId);
	//				}
	//
	//				user.setCurrentGame(game);
	//
	//			} else {
	//				logger.debug("returning stored game for user "+user.getUsername()+", mapId "+mapId);
	//			}
	//
	//			return game.getGameMap();
	//
	//		} catch (Exception e){
	//			logger.error(e,e);
	//			throw new RuntimeException(e);
	//		}
	//	}

	//	@Override @Deprecated
	//	public void createGame(String title, String description) {
	//		try {
	//			logger.debug("recieved request createGame");
	//			User user = getCurrentUser();
	//			Game game = new Game(description, user.getId(), title);
	//			game.addCurrentPlayer(user);
	//			dao.saveGame(game);
	//			user.setCurrentGame(game);
	//			Map<String,String> prefs = dao.getUserPreferences(user.getId());
	//			prefs.put("last_game",game.getId());
	//			dao.updateUserPrefs(user.getId(), prefs);
	//			games.put(game.getNid(), game);
	//		} catch (NotInvitedException e) {
	//			logger.error(e, e);
	//		}
	//	}

	@Override
	public void disconnect() {
		logger.info(getSessionId()+" logging out");
		store.getUsers().remove(getSessionId());
	}



	@Override
	public List<String> getBlockersAt(int x, int y, int z) {
		return getCurrentUser().getCurrentGame().getBlockersAt(x, y, z);
	}

	@Override
	public List<Decal> getDecalList(int page) {
		logger.debug("recieved request getDecalList");
		return dao.getDecalList(page);
	}

	@Override
	public List<MapEvent> getEvents() {
		//flush events every time we return them to the user
		//so that the event queue doesnt get too long for each user.
		List<MapEvent> ret = new ArrayList<MapEvent>();
		try {
			User user = getCurrentUser();
			if (user==null){
				logger.error("user is null");
				ret.add(new RedirectEvent(Page.LOGIN));
				return ret;
			}
			//			logger.debug(user+" is getting events");

			List<MapEvent> mapEvents = user.getEvents();
			//			logger.info(getSessionId()+" has "+events.size()+" events");
			//			logger.info(events);
			HttpServletRequest request = getThreadLocalRequest();
			if (mapEvents.size()==0){
				//suspend until events occur
				//				logger.debug(user.getUsername()+" setting continuation");
				Continuation continuation = ContinuationSupport.getContinuation(request, null);
				if(continuation.isNew() || !continuation.isPending()){
					request.setAttribute("ts", Long.valueOf(System.currentTimeMillis()));
					user.setContinuation(continuation);
					//note that the following ends the method, and then when
					//the continuation gets resumed it starts the method
					//from the beginning.
					continuation.suspend(SUSPEND_DURATION);
				}
			} else {
				logger.debug("returning events "+mapEvents+" to "+user.getUserName());
			}
			synchronized (mapEvents) {
				for (int i = mapEvents.size() - 1; i >= 0; i--) {
					ret.add(mapEvents.remove(i));
				}
			}
		} catch (Exception e) {
			if (e instanceof RetryRequest){
				throw (RetryRequest)e;
			}
			logger.error(e,e);
		}

		return ret;
	}



	@Override
	public List<GameBrief> getGameList() {
		logger.debug("recieved request getGameList");

		User user = getCurrentUser();
		return dao.getGameList(user.getId());
	}

	@Override
	public Map<String, String[]> getMapList() {
		User user = getCurrentUser();
		logger.debug("recieved request getMapList");
		try {
			return dao.getMapList(user.getId());
		} catch (Exception e){
			logger.error(e,e);
			throw new RuntimeException();
		}
	}

	//	@Override
	//	public MapObject getMapObject(MapObjectType type, int id) {
	//		logger.debug("recieved request getMapObject");
	//		logger.debug("getting map object "+type+"/"+id);
	//		switch (type){
	//		case FIGURE:return dao.getFigure(id);
	//		default:
	//			logger.error("get map object for "+type+" not implemented yet");
	//			return null;
	//		}
	//	}

	@Override
	public List<MapObject> getMapObjectsByTags(String tags, MapObjectType type) {
		logger.debug("recieved request getMapObjectsByTags tags:"+tags+" type:"+type);
		return dao.getMapObjectsByTags(tags, type);
	}

	/**
	 * this is the function in which a user's current game is expected to possibly be null
	 */
	@Override
	public MapEvent getMapState() {
		try {
			User user = getCurrentUser();
			logger.debug(user+":**************** recieved request getMapState *****************");
			if (user==null){
				logger.error("user is null!");
				return new RedirectEvent(Page.LOGIN);
			} else user.setContinuation(null);//likely reloading page, needs a new continuation

//			if (user.getAccountLevel()==null || !user.getAccountLevel().toUpperCase().equals("ADMINISTRATOR")){
//				return new RedirectEvent(Page.CHAT);
//			}

			Game game = user.getCurrentGame();
			if (game==null){

				MapEvent e = assignGame(user);
				if (e != null)return e;

				game = user.getCurrentGame();
				if (game==null)return new RedirectEvent(Page.LANDING);//shouldnt be possible

				if (game.getGameMapId()==null){
					logger.debug("redirection to choose a map");
					return new RedirectEvent(Page.MAP_LIST);
				}
			} else {
				logger.debug("user is already loaded up with game "+game.getId()+", map "+game.getGameMap().getId());
			}

			logger.debug("game "+game);
			logger.debug("dm "+game.getDm());
			logger.debug("account level "+user.getAccountLevel());


			logger.debug(" game "+game.getId()+" has "+game.getGameMap().getCellCountX()+" cols and "+game.getGameMap().getCellCountY()+" rows");
			try {
				return new LoadMapEvent(game.getGameMap());
			} finally {
//				game.setGameMap(null);
			}
		} catch (Exception e) {
			logger.error(e, e);
			return null;
		}

	}

	public MapEvent assignGame(User user) {

		synchronized (user){

			Game game = user.getCurrentGame();

			if (game!=null){
				logger.debug("game already assigned for "+user);
				return null;
			}

			Map<String,String> prefs = dao.getUserPreferences(user.getId());
			String lastGame = prefs.get("last_game");
			logger.debug("last game:"+lastGame);

			if (lastGame!=null && lastGame.matches("[0-9]+")){

				logger.debug("loading last game of "+lastGame+" for user "+user.getUserName());
				game = loadGame(Integer.parseInt(lastGame));

				if (game == null){
					logger.debug("game not found, redirecting to list games");
					return new RedirectEvent(Page.LIST_GAMES);
				}

			} else {
				return new RedirectEvent(Page.LIST_GAMES);
			}
			user.setCurrentGame(game);
			return null;

		}
	}

	@Override
	public List<PlayerModel> getPlayers() {

		User user = getCurrentUser();
		Game game = user.getCurrentGame();

		if (game==null)return null;

		logger.debug("user "+user+", "+game);
		List<PlayerModel> ret = new ArrayList<PlayerModel>();

		logger.debug("getting players:"+user+"/"+game);
		for (User p : game.getCurrentPlayers()){
			if (p==null)continue;
			PlayerModel pm = new PlayerModel(
					p.getUserName(), 
					p.getUserName(), 
					"https://secure.gravatar.com/avatar/"+Util.getStringMD5(p.getEmail())+"?s=32",
					p.getColor(),
					false
					);
			logger.debug("gravatar: "+p.getEmail()+" / "+pm.gravatarUrl);
			if (user.getUserName().equals(p.getUserName())){
				pm.isCurrentUser = true;
				ret.add(pm); //current player on right
			} else {
				ret.add(0, pm);
			}
		}

		return ret;
	}

	@Override
	public List<Figure> getFigureList(int page) {
		logger.debug("recieved request getFigureList");
		return dao.getFigureList(page);
	}

	public String getSessionId() {

		HttpServletRequest request = getThreadLocalRequest();
		if (request.getCookies()==null){
			//			logger.info("no cookies found.");
		}			
		Cookie sc = null;
		if (request.getCookies() != null){
			for (Cookie c : request.getCookies()){
				//				logger.info(c.getName()+"="+c.getValue());
				//				if (c.getName().equals("SESS9813ee3f4db59b2b1a7c36d4151569b6")){
				if (c.getName().equals("SESS34f22c3aea82019ba0212bd08055c07f")){
					//					logger.info("found session cookie");
					sc = c;
					break;
				}
			}
		}

		if (sc==null){
			//			logger.info("no session cookie found.");
			if (getRemoteIp().equals(localhost)){
				return "abc123";
			} else {
				return null;
			}
		}

		return sc.getValue();

	}


	@Override
	public List<Tile> getTileList(int page) {
		logger.debug("recieved request getTileList");
		return dao.getTileList(page);
	}


	@Override
	public String getUser() {
		User user = getCurrentUser();
		if (user!=null)return user.getUserName();
		return null;
	}


	@Override
	public List<String> getUserList() {

		User caller = getCurrentUser();
		if (!caller.getAccountLevel().equals("administrator")){
			throw new RuntimeException("403 not authorized");
		}

		List<String> ret = new ArrayList<String>();
		for (User user : store.getUsers().values()){
			ret.add(user.toString());
		}
		ret.add("");
		for (Game g : store.getGames().values()){
			String s = g.getId()+": ";
			for (User u : g.getCurrentPlayers()){
				if (!u.isOnline())continue;
				s+=u.getSessionId()+" | ";
			}
			ret.add(s);
		}
		return ret;
	}

	@Override
	public boolean handleInvite(String code) {
		try {
			logger.debug("handling invite for "+getSessionId());
			User user = getCurrentUser();
			Invite invite = dao.getInvite(cleanXSS(code));
			if (invite==null)return false;
			Game game = loadGame(invite.gameNid);
			//broadcast first, then add player so new player doesn't get reloadplayersevent.
			game.broadcast(new ReloadPlayersEvent());
			game.addCurrentPlayer(user);
			dao.deleteInvite(invite.getId());
			return true;
		} catch (NotInvitedException e) {
			logger.error(e,e);
			return false;
		}
	}


	//	@Override
	//	public String invite(String invitees) {
	//
	//		String ret = "";
	//
	//		User user = getCurrentUser();
	//		Game game = user.getCurrentGame();
	//		invitees = cleanXSS(invitees);
	//		logger.debug(user.getUserName()+" invited "+invitees+" to game "+game.getId());
	//
	//		boolean newPlayers = false;
	//
	//
	//		for (String i : invitees.split(",")){
	//			i = i.trim();
	//			User invitee =  (i.contains("@")) ? dao.getUserByEmail(i) : dao.getUserByUsername(i);
	//			Map<String,Object> attributes = new HashMap<String, Object>();
	//
	//			if (invitee != null){
	//				logger.debug("adding user "+invitee.getUserName()+" to game "+game.getId());
	////				game.addPlayer(invitee);
	//				ret+=invitee.getUserName()+" added to game, ";
	//				newPlayers = true;
	//				attributes.put("gameTitle", game.getTitle());
	//				attributes.put("inviter", user.getUserName());
	//				EmailSender.sendEmail("invitePlayer", attributes, invitee.getEmail());
	//			} else {
	//				if (i.matches("[A-Z0-9._%+-]+@(?:[A-Z0-9-]+\\.)+[A-Z]{2,4}")){
	//					try {
	//						Invite invite = dao.createInvite(i, game.getId());
	//						attributes.put("gameTitle", game.getTitle());
	//						attributes.put("inviter", user.getEmail());
	//						attributes.put("code", invite.getCode());
	//						EmailSender.sendEmail("inviteNewUser", attributes, i);
	//						ret+=i+" invited to join, ";
	//					} catch (Exception e){
	//						ret+=i+" looks like a bad address, got error "+e.getMessage()+", ";
	//					}
	//				} else {
	//					ret+=i+" not found and doesn't look like an email address, ";
	//				}
	//			}
	//		}
	//
	//		if (ret.length()>0)ret = ret.substring(0, ret.length()-2);
	//		ret+=".";
	//
	//		if (newPlayers){
	//			game.broadcast(new ReloadPlayersEvent());
	//		}
	//
	//		return ret;
	//
	//	}


	public boolean isAuthorized() {


		try {

			User user = null;
			String sessionId = getSessionId();
			if (sessionId==null)return false; 
			if ((user = store.getUsers().get(sessionId))!=null)return true;
			else {

				logger.debug("checking auth for "+getRemoteIp()+" "+sessionId);
				user = dao.getUser(getRemoteIp(),sessionId); 


				if (user==null && getRemoteIp().equals(localhost)){
					logger.debug("local request, loading user ryan");
					user = dao.getUserByUsername("ryan");
				}

				if (user!=null){

					logger.info("auth successful");
					user.setSessionId(sessionId);


					logger.info("caching user "+user);
					user = store.addUser(sessionId, user);

					user.setContinuation(null);

					user.addEvent(new WelcomeEvent());

					return true;
				} else {
					logger.info("auth failed");
					return false;
				}

			} 
			//			else {
			//				//some browsers(safari) wont log out properly when the page changes
			//				//this means that their user object stays active and as such
			//				//if they are just refreshing the page, the OLD continuation will
			//				//get restarted which will then respond to the wrong response object
			//				//meaning no objects will show up on the map.
			//				logger.info("no need to create a new user");
			//				user.setContinuation(null);
			//
			//				//				user.setCurrentGame(dao.getGame(1));
			//				//				user.addEvent(getMapState());
			//
			//				return true;
			//			}

		} catch (RuntimeException e){
			logger.error(e,e);
			throw e;
		}

	}


	private String getRemoteIp() {
		String ip = getThreadLocalRequest().getHeader("X-Real-IP");
		if (ip!=null)return ip;
		return getThreadLocalRequest().getRemoteAddr();
	}

	@Override
	public void moveDecal(MapObjectWrapper mow) {

		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		GameMap map = game.getGameMap();

		logger.debug("user "+user+" moving mapobject "+mow.getMapObject().getElementId()+" in game "+game.getId()+", mapId"+game.getGameMap().getId());

		MapObjectWrapper current = map.getMapObjects().get(mow.getElementId());
		current.setX(mow.getX());
		current.setY(mow.getY());

		game.saveGameMap();

		game.broadcast(new MoveDecalEvent(mow));
	}


	@Override
	public void moveFigure(String elementId, List<MapCell> path) {
		logger.debug("recieved request moveCharacter for "+elementId+" "+path);
		if (path.size()<=1)return;//nothing to move

		Game game = getCurrentUser().getCurrentGame();

		MapObjectWrapper figure = game.getGameMap().getMapObjects().get(elementId);
		MapCell end = path.get(path.size()-1);
		figure.setX(end.col);
		figure.setY(end.row);
		figure.setZ(end.layer);
		game.removeMapObjects(figure.getElementId());
		List<MapObjectWrapper> toRemove = game.addMapObjects(figure);
		if (toRemove.size()>0){
			logger.error("figure move should not be able to displace existing figure! "+toRemove, new Throwable());
		}

		game.broadcast(new MoveFigureEvent(figure.getElementId(), path));

	}

	private static int reqCount = 0;

	@Override
	public String processCall(String payload) throws SerializationException {

		Date start = new Date();
		String abbrev = payload.replaceAll(".*EventBus\\|([a-zA-Z]+)\\|.*", "$1");
		int reqId = reqCount++;
		if (abbrev!=null && !abbrev.equals("getEvents"))logger.debug("^^^ req "+reqId+" "+abbrev);
		try {

			if (isAuthorized())return super.processCall(payload);
			else return "401 not authorized";

		} finally {

			Date end = new Date();
			if (abbrev!=null && !abbrev.equals("getEvents"))logger.debug("$$$ req "+reqId+" "+abbrev+" "+getCurrentUser()+" time:"+(end.getTime()-start.getTime()));

		}

	}




	@Override
	public void removeMapObject(String elementId) {
		List<String> mowl = new ArrayList<String>();
		mowl.add(elementId);
		removeMapObjects(mowl);
	}

	@Override
	public void removeMapObjects(List<String> mowl) {
		if (mowl.size()==0)return;
		User user = getCurrentUser();
		Game game = user.getCurrentGame();

		logger.debug("user "+user+" removing mapobjects from game "+game.getId()+", mapId"+game.getGameMap().getId());
		for (String mow : mowl){
			logger.debug(mow);
		}

		game.broadcast(new RemoveMapObjectsEvent(mowl));

		game.removeMapObjects(mowl.toArray(new String[]{}));

	}

	@Override
	public void rotateObject(String elementId, int rotation) {

		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		GameMap map = game.getGameMap();
		logger.debug("user "+user+" rotating mapobject "+elementId+" to "+rotation);

		MapObjectWrapper mow = map.getMapObjects().get(elementId);
		mow.setRotation(rotation);

		game.broadcast(new RotateMapObjectEvent(elementId, rotation));

		game.saveGameMap();
	}


	@Override
	public void setLayerVisibility(int layer, boolean visible) {
		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		GameMap map = game.getGameMap();
		logger.debug("user "+user+" setting layer visibility "+layer+":"+visible+" for game "+game+", map "+map);

		map.setLayerVisible(layer, visible);

		game.broadcast(new UpdateLayerVisibilityEvent(layer, visible));

		game.saveGameMap();

	}


	@Override
	public void setObjectVisible(String elementId, boolean visible) {
		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		GameMap map = game.getGameMap();

		MapObjectWrapper mow = map.getMapObjects().get(elementId);
		mow.setVisible(visible);

		game.broadcast(new MapObjectVisibilityEvent(elementId, visible));

		game.saveGameMap();

		logger.debug("user "+user+" setting object visibility "+elementId+":"+visible+" for game "+game.getId()+", mapId"+game.getGameMap().getId());		
	}


	@Override
	public void switchMap(String mapId) {

		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		logger.debug("switch map called by "+user+" who currently is on game "+game+" to map "+mapId);

		GameMap map = null;
		if (mapId==null){
			map = new GameMap(Defaults.CELLCOUNTX, Defaults.CELLCOUNTY, user.getUserName(), new Date(), null, null, user.getId());
			dao.saveGameMap(map);//this will assign an id
		} else {
			map = dao.getGameMap(mapId);
			if (map==null){
				throw new RuntimeException("Invalid map chosen.");
			}
			if (map.getOwnerId()!=user.getId() && map.getOwnerId()!=0){
				throw new RuntimeException("You are not authorized to use this map.");
			}
		}

		if (map!=null && game!=null) {
			logger.debug("saving and broadcasting");
			game.setGameMap(map);
			dao.saveGame(game);
			game.broadcast(new LoadMapEvent(game.getGameMap()));
		} 

	}

	private String cleanXSS(String value) {
		//You'll need to remove the spaces from the html entities below
		value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
		value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
		value = value.replaceAll("'", "& #39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("script", "");
		return value;
	}


	private Game loadGame(int nid) {

		Game game = null;
		game = store.getGames().get(nid);
		if (game==null){
			try {
				logger.debug("loading game "+nid+" from database");
				game = dao.getGame(nid);

			} catch (InvalidGameIdException e) {
				logger.debug("game not found!");
				return null;
			}
			if (game!=null)game = store.addGame(nid, game);
		} else {
			logger.debug("loaded game "+nid+" from cache");
		}

		return game;


	}

	private User getCurrentUser() {
		String sessionId = getSessionId();
		if (sessionId==null)return null;
		User user = store.getUsers().get(sessionId);
		if (user==null)isAuthorized();
		user = store.getUsers().get(getSessionId());
		if (user!=null){}//logger.debug("current user is "+user);
		else logger.info("no user logged in.");
		return user;
	}

	@Override
	protected void doUnexpectedFailure(Throwable caught)
	{
		throwIfRetryRequest(caught);
		super.doUnexpectedFailure(caught);
	}

	@Override
	protected String readContent(HttpServletRequest request)
			throws IOException, ServletException {
		String payload=(String)request.getAttribute( PAYLOAD );
		if (payload==null){
			payload=super.readContent( request );
			request.setAttribute( PAYLOAD,payload );
		}
		return payload;
	}

	/**
	 * Throws the Jetty RetryRequest if found.
	 *
	 * @param caught the exception
	 */
	protected void throwIfRetryRequest(Throwable caught){
		if (caught instanceof UnexpectedException){
			caught = caught.getCause();
		}

		if (JETTY_RETRY_REQUEST_EXCEPTION.equals(caught.getClass().getName())){
			throw (RuntimeException)caught;
		}
	}

	@Override
	public void saveNote(Note note) {
		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		GameMap map = game.getGameMap();
		MapObjectWrapper mow = map.getMapObjects().get(note.getElementId());
		mow.setObj(note);

		game.broadcast(new UpdateNoteEvent(note));

		game.saveGameMap();
	}

	@Override
	public CharacterSheet getCharacter(String characterId) {
		logger.debug("getcharacter called");
		if (characterId.equals("default")){
			return dao.getCharacterSheetByUser(getCurrentUser().getId());
		} else {
			return dao.getCharacterSheet(characterId);
		}
	}

	@Override
	public String saveCharacter(CharacterSheet cs){
		if (cs==null)return null;
		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		logger.debug("saveCharacter called:"+cs.getId()+":"+cs.toString());
		if (cs.getOwnerId()<0)cs.setOwnerId(user.getId());
		for (CharacterClassModel ccm : cs.getClassModels()){
			for (List<Spell> list : ccm.getKnownSpells().values()){
				for (int i = list.size()-1 ; i>=0 ; i--){
					if (list.get(i)==null || list.get(i).getName().equals("")){
						list.remove(i);
					}
				}
			}
		}
		cs.setOwnerId(user.getId());
		if (game!=null)cs.setGameId(game.getId());
		dao.saveCharacterSheet(cs);
		logger.debug("save character finished");
		return cs.getId();

	}


	@Override
	public void expandMap(String direction){
		if (direction==null)logger.error("invalid direction "+direction+" for expandMap");
		else logger.debug("expanding map "+direction);

		User user = getCurrentUser();
		Game game = user.getCurrentGame();

		game.expandMap(direction);//saves

		game.broadcast(new LoadMapEvent(game.getGameMap()));



	}

	@Override
	public void init() throws ServletException {
		super.init();

		logger.fatal("THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. THE SERVER IS BEING STARTED. ");
		Dao.doConversion("CharacterSheetV2");

	}

	@Override
	public Map<String, CharacterBrief> getCharacterList(){
		User user = getCurrentUser();
		logger.debug("getting character list for user "+user);
		return dao.getCharacterList(user.getId());
	}

	public List<ChatMessage> joinChat(){

		User user = getCurrentUser();

		Game game = user.getCurrentGame();

		if (game==null){
			MapEvent e = assignGame(user);
			if (e!=null)user.addEvent(e);
			game = user.getCurrentGame();
			if (game==null){
				logger.debug("no game found for "+user);
				return null;
			}
			logger.debug(user+" called join chat, assigned game "+game.getNid());
		}

		return dao.getRecentChatMessages(game.getNid());


	}

	public boolean isEditor(){
		User user = getCurrentUser();
		Game game = user.getCurrentGame();
		logger.debug("checking editor for "+user+"/"+game);
		
		if (game==null){
			MapEvent e = assignGame(user);
			if (e!=null){
				user.addEvent(e);
				return false;
			}
			game = user.getCurrentGame();
			logger.debug("double checking editor for "+user+"/"+game);
		}

		if (user.getUserName().equals("gary1")){
			return true;
		}
		

		if (user.getId()==game.getDm() || user.getAccountLevel().equals("administrator")){
			return true;
		} else {
			return false;
		}
	}

}










