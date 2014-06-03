package com.mut8ed.battlemap.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.ChatMessage;
import com.mut8ed.battlemap.shared.dto.Decal;
import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.GameBrief;
import com.mut8ed.battlemap.shared.dto.MapCell;
import com.mut8ed.battlemap.shared.dto.MapObject;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;
import com.mut8ed.battlemap.shared.dto.Note;
import com.mut8ed.battlemap.shared.dto.PlayerModel;
import com.mut8ed.battlemap.shared.dto.Tile;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterBrief;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.event.MapEvent;

@RemoteServiceRelativePath("eventbus")
public interface EventBus extends RemoteService {

//	Page checkAuth();
	
	boolean handleInvite(String code);
	
	void chooseFigure(String figureId);
	
	String chooseGame(int gameNid);
	
//	void createGame(String title, String description);
	
	List<Decal> getDecalList(int page);
	
	//	void clickCell(MapCell cell);
	
	List<MapEvent> getEvents();
	
	List<GameBrief> getGameList();
	
//	GameMap getGameMap(int mapId);
	
	Map<String,String[]> getMapList();
	
//	MapObject getMapObject(MapObjectType type, int id);
	
	List<MapObject> getMapObjectsByTags(String tags, MapObjectType type);
	
	MapEvent getMapState();
	
	List<Figure> getFigureList(int page);
	
	List<Tile> getTileList(int page);
	
	String getUser();
	
	void disconnect();
	
	void moveFigure(String elementId, List<MapCell> path);
	
	List<String> getUserList();
	
	void addMapObjects(List<MapObjectWrapper> mow);
	
	void removeMapObjects(List<String> objectIds);
	
	void moveDecal(MapObjectWrapper mow);
	
	void rotateObject(String elementId, int rotation);
	
	void setLayerVisibility(int layer, boolean visible);
	
	void setObjectVisible(String elementId, boolean visible);
	
	void chat(String msg);
	
//	String invite(String invitee);
	
	void addMapObject(MapObjectWrapper mow);
	
	void removeMapObject(String elementId);
	
	List<String> getBlockersAt(int x, int y, int z);

	void switchMap(String mapId);
	
	void changeMapName(String mapName);
	
	List<PlayerModel> getPlayers();
	
	void saveNote(Note note);
	
	CharacterSheet getCharacter(String id);
	
	String saveCharacter(CharacterSheet cs);
	
	void expandMap(String direction);
	
	Map<String, CharacterBrief> getCharacterList();
	
	List<ChatMessage> joinChat();
	
	boolean isEditor();
	
}
