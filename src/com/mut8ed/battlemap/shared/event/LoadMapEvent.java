package com.mut8ed.battlemap.shared.event;

import com.mut8ed.battlemap.shared.dto.GameMap;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;

/**
 * an event telling the client to redraw the entire map, and passes all map objects for the given map.
 * @author ryan
 *
 */
public class LoadMapEvent extends MapEvent {
	private GameMap gameMap;
	
	public LoadMapEvent(){};
	
	public LoadMapEvent(GameMap gameMap){
		this.gameMap = gameMap;
	}

	public void addMapObject(MapObjectWrapper mapObjectWrapper) {
		gameMap.addMapObject(mapObjectWrapper);
	}
	
	@Override
	public String toString(){
		return "LoadMapEvent:"+gameMap;
	}

	public GameMap getGameMap() {
		return gameMap;
	}

	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
	}

	
}
