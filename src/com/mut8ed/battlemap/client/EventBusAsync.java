package com.mut8ed.battlemap.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
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

public interface EventBusAsync {

//	void checkAuth(AsyncCallback<Page> asyncCallback);
	
	void handleInvite(String code, AsyncCallback<Boolean> callback);

	void chooseFigure(String figureId, AsyncCallback<Void> callback);

	void chooseGame(int gameNid, AsyncCallback<String> callback);

//	void createGame(String title, String description, AsyncCallback<Void> callback);

	void getDecalList(int page, AsyncCallback<List<Decal>> asyncCallback);

	//	void clickCell(MapCell cell, AsyncCallback<Void> callback);

	void getEvents(AsyncCallback<List<MapEvent>> callback);

	void getGameList(AsyncCallback<List<GameBrief>> callback);

//	void getGameMap(int mapId, AsyncCallback<GameMap> asyncCallback);

	void getMapList(AsyncCallback<Map<String, String[]>> asyncCallback);

//	void getMapObject(MapObjectType type, int objId, AsyncCallback<MapObject> callback);

	void getMapObjectsByTags(String tags, MapObjectType type,AsyncCallback<List<MapObject>> asyncCallback);

	void getMapState(AsyncCallback<MapEvent> callback);

	void getFigureList(int page, AsyncCallback<List<Figure>> callback);

	void getTileList(int page, AsyncCallback<List<Tile>> asyncCallback);

	void getUser(AsyncCallback<String> asyncCallback);

	void disconnect(AsyncCallback<Void> callback);

	void moveFigure(String elementId, List<MapCell> path, AsyncCallback<Void> callback);

	void getUserList(AsyncCallback<List<String>> callback);

	void addMapObjects(List<MapObjectWrapper> mow, AsyncCallback<Void> callback);

	void removeMapObjects(List<String> tr, AsyncCallback<Void> callback);

	void moveDecal(MapObjectWrapper mow, AsyncCallback<Void> callback);

	void rotateObject(String elementId, int rotation,
			AsyncCallback<Void> callback);

	void setLayerVisibility(int layer, boolean visible,
			AsyncCallback<Void> callback);

	void setObjectVisible(String elementId, boolean visible,
			AsyncCallback<Void> callback);

	void chat(String msg, AsyncCallback<Void> callback);

//	void invite(String invitees, AsyncCallback<String> asyncCallback);

	void addMapObject(MapObjectWrapper mow,	AsyncCallback<Void> asyncCallback);
	
	void removeMapObject(String elementId, AsyncCallback<Void> asyncCallback);

	void getBlockersAt(int x, int y, int z, AsyncCallback<List<String>> callback);

	void switchMap(String mapId, AsyncCallback<Void> callback);

	void changeMapName(String mapName, AsyncCallback<Void> callback);
	
	void getPlayers(AsyncCallback<List<PlayerModel>> callback);

	void saveNote(Note note, AsyncCallback<Void> cb);

	void getCharacter(String characterId, AsyncCallback<CharacterSheet> callback);

	void saveCharacter(CharacterSheet cs, AsyncCallback<String> callback);

	void expandMap(String direction, AsyncCallback<Void> callback);

	void getCharacterList(AsyncCallback<Map<String, CharacterBrief>> callback);

	void joinChat(AsyncCallback<List<ChatMessage>> callback);

	void isEditor(AsyncCallback<Boolean> callback);

}
