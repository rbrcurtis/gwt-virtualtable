package com.mut8ed.battlemap.server.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.annotation.PersistenceConstructor;

import com.mut8ed.battlemap.server.dao.Dao;
import com.mut8ed.battlemap.server.util.CreateDtoCode;
import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.GameMap;
import com.mut8ed.battlemap.shared.dto.MapObjectWrapper;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterClass;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.Race;
import com.mut8ed.battlemap.shared.event.JoinEvent;
import com.mut8ed.battlemap.shared.event.MapEvent;
import com.mut8ed.battlemap.shared.exception.NotInvitedException;

public class Game implements Serializable {
	public final static Logger logger = Logger.getLogger(Game.class);

	private static String[] colors = {"blue", "green", "yellow", "purple", "orange", "teal"};
	private static final long serialVersionUID = 1L;
	private Date createDate;
	private String description;
	private int dm = -1;
	transient private GameMap gameMap = null;
	private String id;
	private String mapId;
	transient private MapMatrix matrix = null;
	transient private int nextColor = 0;
	private int nid; //organic group id from drupal tables
	private int ownerId = -1;
	transient private Map<Integer, User> currentPlayers = new HashMap<Integer, User>(); //loaded by sql from OG data
	private String title;

	@PersistenceConstructor
	public Game(){}

	public Game(Date createDate, String description, Integer ownerId, String id,
			String mapId, String title) {
		super();
		this.createDate = createDate;
		this.ownerId = ownerId;
		this.dm = ownerId;
		this.id = id;
		this.mapId = mapId;
		this.title = title;
	}

	public Game(String description, Integer ownerId, String title){
		this.createDate = new Date();
		this.description = description;
		this.ownerId = ownerId;
		this.title = title;
	}

	public static void main(String[] args){
		CreateDtoCode.echoBeanCode("Game", "select * from game where id = 1");
	}

	public void addCurrentPlayer(User user) throws NotInvitedException {
		if (currentPlayers.containsKey(user.getId())){
			user.setColor(currentPlayers.get(user.getId()).getColor());
		} else {
			if (new Dao().getPlayers(nid).get(user.getId())==null)throw new NotInvitedException();
			user.setColor(getNextColor());
		}
		//		user.setCharacterName(user.getCharacterName());
		user.setOnline(true);
		broadcast(new JoinEvent());
		currentPlayers.put(user.getId(), user);
	}

	public List<MapObjectWrapper> addMapObjects(MapObjectWrapper... mows) {
		GameMap gameMap = getGameMap();
		List<MapObjectWrapper> ret = new ArrayList<MapObjectWrapper>(); 
		for (MapObjectWrapper mow : mows){
			if (matrix==null || gameMap==null){
				throw new RuntimeException("adding objects to a game without a map.");
			}

			Dao dao = new Dao();
			if (mow.getCharacterId()==null && mow.getMapObjectType().equals(MapObjectType.FIGURE)){
				CharacterSheet cs = CharacterSheet.create("John Doe", Race.HUMAN, CharacterClass.FIGHTER, 1);
				cs.isTemplate(true);
				cs.isTempCharacter(true);
				cs.setFigure((Figure)mow.getMapObject());
				dao.saveCharacterSheet(cs);

				logger.debug("created charactersheet "+cs.getId()+" for "+mow.getElementId());
			} else if (mow.getCharacterId()!=null){
				CharacterSheet cs = dao.getCharacterSheet(mow.getCharacterId());
				logger.debug("adding character "+mow.getCharacterId()+", "+cs.getCharacterName());
				if (cs.isTemplate()){
					cs.setId(null);
					cs.isTempCharacter(true);
					dao.saveCharacterSheet(cs);
					logger.debug("cloned to "+cs.getId());
					mow.setCharacterId(cs.getId());
				}
			}

			List<String> toRemove = matrix.add(mow);
			gameMap.addMapObject(mow);
			for (String id : toRemove){
				ret.add(gameMap.remove(id));
			}
		}
		saveGameMap();
		return ret;
	}

	public void broadcast(MapEvent... events) {
		for (MapEvent event : events){
			logger.debug("broadcasting "+event);
			for (User user : currentPlayers.values()){
				if (user.isOnline()){
					logger.debug("sending to "+user.getUserName());
					user.addEvent(event);
				}
			}
		}
	}

	public void expandMap(String direction) {
		int expandSize = 50;
		GameMap gameMap = getGameMap(); 
		if (direction.equals("up")){

			gameMap.setCellCountY(gameMap.getCellCountY()+expandSize);
			for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
				if (mow.getMapObjectType().name().matches("DECAL|NOTE")){
					mow.setY(mow.getY()+Defaults.CELLSIZE*expandSize);
				} else {
					mow.setY(mow.getY()+expandSize);
				}
			}

		} else if (direction.equals("down")){

			gameMap.setCellCountY(gameMap.getCellCountY()+expandSize);

		} else if (direction.equals("left")){

			gameMap.setCellCountX(gameMap.getCellCountX()+expandSize);
			for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
				if (mow.getMapObjectType().name().matches("DECAL|NOTE")){
					mow.setX(mow.getX()+Defaults.CELLSIZE*expandSize);
				} else {
					mow.setX(mow.getX()+expandSize);
				}
			}

		} else if (direction.equals("right")){

			gameMap.setCellCountX(gameMap.getCellCountX()+expandSize);

		} else throw new RuntimeException("invalid map expand direction "+direction);

		setGameMap(gameMap);//this will create a new matrix
		saveGameMap();
	}

	public List<String> getBlockersAt(int x, int y, int z){
		return matrix.getBlockersAt(x, y, z);
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getDescription() {
		return description;
	}

	public int getDm() {
		return dm;
	}

	public GameMap getGameMap() {
		if (gameMap==null)gameMap = new Dao().getGameMap(mapId);

		logger.debug("matrix "+matrix);

		return gameMap;
	}

	public String getGameMapId() {
		return mapId;
	}

	public String getId() {
		return id;
	}

	public MapMatrix getMatrix() {
		return matrix;
	}

	public String getNextColor(){
		return colors[nextColor++ % colors.length];
	}

	public int getNid() {
		return nid;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public Collection<User> getCurrentPlayers() {
		return currentPlayers.values();
	}

	public Collection<User> getAllPlayers() {
		return new Dao().getPlayers(nid).values();
	}

	public String getTitle() {
		return title;
	}

	public void removeMapObjects(String... elementIds) {
		GameMap gameMap = getGameMap();
		for (String elementId : elementIds){
			if (matrix==null || gameMap==null){
				throw new RuntimeException("removing objects from a game without a map.");
			}
			MapObjectWrapper mow = gameMap.getMapObjects().remove(elementId);
			matrix.remove(mow);
			if (mow.getCharacterId()!=null){
				Dao dao = new Dao();
				CharacterSheet cs = dao.getCharacterSheet(mow.getCharacterId());
				if (cs.isTempCharacter()){
					logger.debug("deleting "+cs.getId());
					dao.deleteCharacter(cs.getId());
				}
			}
		}
		saveGameMap();
	}

	public void saveGameMap() {
		if (gameMap==null)throw new RuntimeException("tried to save null map");
		new Dao().saveGameMap(gameMap);
		gameMap = null;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDm(int dm) {
		this.dm = dm;
	}

	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
		if (gameMap==null)return;
		if (gameMap.getId()==null)new Dao().saveGameMap(gameMap);
		mapId = gameMap.getId();
		matrix = new MapMatrix(gameMap.getCellCountX(), gameMap.getCellCountY());
		List<String> toRemove = new ArrayList<String>();
		for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
			List<String> removals = matrix.add(mow);
			if (removals.size()>0)System.out.println(removals.size());
			toRemove.addAll(removals);
		}
		if (toRemove.size()>0){
			logger.error("have objects needing removal on setGameMap call for map "+gameMap.getId(), new Throwable());
			for(String id : toRemove){
				gameMap.getMapObjects().remove(id);
			}
		}
	}

	@Deprecated
	public void setGameMapId(String string) {
		mapId = string;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void showMatrixCounts() {
		matrix.showCounts();
	}

	public String toString(){
		return "game "+this.id+((mapId!=null)?", map "+mapId:"");
	}

}
