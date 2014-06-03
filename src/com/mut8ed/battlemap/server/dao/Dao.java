package com.mut8ed.battlemap.server.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mut8ed.battlemap.server.dao.document.CharacterDocument;
import com.mut8ed.battlemap.server.dao.document.MongoMigration;
import com.mut8ed.battlemap.server.dao.sql.DaoSql;
import com.mut8ed.battlemap.server.dao.util.DaoUtil;
import com.mut8ed.battlemap.server.dto.Game;
import com.mut8ed.battlemap.server.dto.Invite;
import com.mut8ed.battlemap.server.dto.MapImage;
import com.mut8ed.battlemap.server.dto.User;
import com.mut8ed.battlemap.server.servlet.MapObjectUploadServlet;
import com.mut8ed.battlemap.server.util.Serializer;
import com.mut8ed.battlemap.shared.Defaults;
import com.mut8ed.battlemap.shared.MapObjectType;
import com.mut8ed.battlemap.shared.dto.ChatMessage;
import com.mut8ed.battlemap.shared.dto.Decal;
import com.mut8ed.battlemap.shared.dto.Figure;
import com.mut8ed.battlemap.shared.dto.GameBrief;
import com.mut8ed.battlemap.shared.dto.GameMap;
import com.mut8ed.battlemap.shared.dto.MapObject;
import com.mut8ed.battlemap.shared.dto.Tile;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterBrief;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterConverter;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheet;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheetImpl;
import com.mut8ed.battlemap.shared.dto.charactersheet.CharacterSheetV2;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Dao {

	private JdbcTemplate db;
	public static final Logger logger = Logger.getLogger(Dao.class);
	private static MongoTemplate mongo;
	private static int pageSize = 40;

	public Dao(){
		//		try {
		//			db = new JdbcTemplate(DaoUtil.getDataSource());
		//			mongo = new MongoTemplate(new Mongo("mt-db", 27017), "mystictriad");
		//			DataSourceTransactionManager txManager = new DataSourceTransactionManager(db().getDataSource());
		//			tt = new TransactionTemplate(txManager);
		//		} catch (Exception e) {
		//			logger.error(e, e);
		//		}
	}

	public JdbcTemplate db(){
		if (db == null)db = new JdbcTemplate(DaoUtil.getDataSource());
		return db;
	}

	public MongoTemplate mongo(){
		try {
			Mongo m = new Mongo("mt-db", 27017);
			m.fsync(false);
			if (mongo==null)mongo = new MongoTemplate(m, "mystictriad");
		} catch (Exception e) {
			logger.error(e, e);
		}
		return mongo;
	}

	public void saveFigure(final Figure figure){ 
		logger.debug("saving Figure " + figure);
		if (figure.getId()==null)figure.setId(getNewId());
		mongo().save(figure);
	} 

	public Figure getFigure(String id) {
		logger.debug("getting figure "+id);
		if (id==null)return null;

		return mongo().findOne(query(where("id").is(id)), Figure.class);
	}

	public User getUser(int key) {

		return (User)db().query(
				DaoSql.getUserById, new Object[]{key}, new ResultSetExtractor(){

					@Override
					public Object extractData(ResultSet set)
							throws SQLException, DataAccessException {

						if (!set.next())return null;
						return new User(
								set.getString("account_level"),
								new Timestamp(set.getLong("create_date")*1000),
								set.getString("email"),
								set.getInt("uid"),
								set.getString("username")
								);
					}

				});
	}

	public List<Figure> getFigureList(int page) {
		return mongo().find(new Query().skip(pageSize*page).limit(pageSize), Figure.class);
	}



	public GameMap getGameMap(String id) {
		Date start = new Date();
		logger.debug("dao getting gamemap "+id);
		try {
			return mongo().findById(id, GameMap.class);
		} finally {
			Date end = new Date();
			logger.debug("TTT getGameMap time "+(end.getTime()-start.getTime()));
		}
	}

	public void saveGameMap(GameMap gameMap){
		Date start = new Date();
		try {
			logger.debug("saving map "+gameMap.getId());
			if (gameMap.getId()==null)gameMap.setId(getNewId());
			mongo().save(gameMap);
		} finally {
			Date end = new Date();
			logger.debug("TTT saveGameMap time "+(end.getTime()-start.getTime()));
		}
	}

	public static String getNewId(){
		return UUID.randomUUID().toString();
	}


	public Game getGame(final int nid) {
		Game game = mongo().findOne(query(where("nid").is(nid)), Game.class);
		if (game==null){

			return db().query(DaoSql.getGame, new Object[]{nid}, new ResultSetExtractor<Game>(){

				@Override
				public Game extractData(ResultSet set) throws SQLException,
				DataAccessException {

					Game game = new Game();
					game.setNid(nid);

					if (!set.next()){
						logger.error("game "+nid+" does not exist");
						return null;
					}
					game.setDescription(set.getString("description"));
					game.setDm(set.getInt("dm"));
					game.setOwnerId(set.getInt("dm"));
					game.setTitle(set.getString("title"));
					game.setId(getNewId());

					return game;
				}

			});

		}
		game.setGameMap(game.getGameMap());
		return game;
	}

	public Map<Integer, User> getPlayers(int nid) {
		return db().query(
				DaoSql.getUsersByGroup, new Object[]{nid}, new ResultSetExtractor<Map<Integer, User>>(){

					@Override
					public Map<Integer, User> extractData(ResultSet set)
							throws SQLException, DataAccessException {
						Map<Integer, User> ret = new HashMap<Integer, User>();
						while (set.next()){
							ret.put(set.getInt("uid"), new User(
									set.getString("account_level"),
									new Timestamp(set.getLong("create_date")*1000),
									set.getString("email"),
									set.getInt("uid"),
									set.getString("username")
									));
						}
						return ret;
					}

				});
	}

	public void saveGame(Game game){
		logger.debug("saving game "+game.getId());
		if (game.getId()==null)game.setId(getNewId());
		mongo().save(game);
	}

	public Tile getTile(String id) {
		if (id==null)return null;
		return mongo().findOne(query(where("id").is(id)), Tile.class);
	}


	public void updateTile(String tileId, final String imageId, final Integer height, final Integer width, List<String> tags){ 

		Tile tile = getTile(tileId);
		MapImage image = getMapImageByUrl(tile.getImageUrl());
		deleteImage(image.getId());
		image = getMapImageById(imageId);
		tile.setImageUrl(image.getFilePath());
		tile.setHeight(height);
		tile.setWidth(width);
		tile.setTags(tags);
		saveTile(tile);

	} 

	public void saveTile(Tile tile) {
		if (tile.getId()==null)tile.setId(getNewId());
		mongo().save(tile);
	}

	public MapImage getMapImageById(String id) {
		return mongo().findOne(query(where("id").is(id)), MapImage.class);
	}

	public MapImage getMapImageByUrl(String imageUrl) {
		return mongo().findOne(query(where("filepath").is(imageUrl)), MapImage.class);
	}

	public void updateDecal(String decalId, final String imageId, final Integer height, final Integer width, List<String> tags){ 

		Decal decal = getDecal(decalId);
		MapImage image = getMapImageById(decal.getImageUrl());
		deleteImage(image.getId());
		image = getMapImageById(imageId);
		decal.setImageUrl(image.getFilePath());
		decal.setHeight(height);
		decal.setWidth(width);
		decal.setTags(tags);
		saveDecal(decal);

	}

	private void deleteImage(String filepath) {
		try {
			new File(MapObjectUploadServlet.FILE_PATH + filepath).delete();
		} catch (Exception e) {
			logger.error(e,e);
		}
	}

	public Decal getDecal(String id) {
		if (id==null)return null;
		return mongo().findOne(query(where("id").is(id)), Decal.class);
	}


	public void saveDecal(Decal obj){
		if (obj.getId()==null)obj.setId(getNewId());
		mongo().save(obj);
	} 

	public boolean isDuplicateImage(String md5) {
		return (mongo().find(query(where("md5").is(md5)), MapImage.class).size()>0);
	}


	public String insertImage(final String path, final String md5){ 

		MapImage image = new MapImage(path, md5);

		mongo().save(image);

		return image.getId();

	}


	public void insertObject(MapObjectType type, Map<String, String> params,
			String imagePath, String md5) {

		String[] t = params.get("tags").split(",");
		List<String> tags = new ArrayList<String>();
		for (int i = 0; i < t.length; i++) {
			if (t[i]==null)continue;
			t[i] = t[i].trim();
			if (t[i].equals(""))continue;
			tags.add(t[i]);
		}
		String cat = params.get("category");
		if (cat!=null && !cat.trim().equals("")){
			tags.add("c:"+cat.trim());
		}

		String imageId = null;
		if (imagePath!=null){
			imageId = insertImage(imagePath, md5);
		}
		switch (type){
		case TILE:
			if (params.get("object-id")!=null){
				String tileId = cleanXSS(params.get("object-id"));

				updateTile(
						tileId,
						imageId,
						Integer.parseInt(params.get("height")),
						Integer.parseInt(params.get("width")),
						tags
						);


			} else if (imageId!=null){
				saveTile(
						new Tile(
								imagePath,
								Integer.parseInt(params.get("height")),
								Integer.parseInt(params.get("width")),
								tags
								)
						);
			}
			break;
		case DECAL:
			if (params.get("object-id")!=null){
				String decalId = cleanXSS(params.get("object-id"));

				updateDecal(
						decalId,
						imageId,
						Integer.parseInt(params.get("height")),
						Integer.parseInt(params.get("width")),
						tags
						);


			} else if (imageId!=null){
				saveDecal(
						new Decal(
								imagePath,
								Integer.parseInt(params.get("height")),
								Integer.parseInt(params.get("width")),
								tags
								)
						);

			}
			break;
		default:logger.fatal("I should not be able to get here");
		}
	}


	private String cleanXSS(String string) {
		return string.replaceAll("[^0-9a-zA-Z]", "");
	}




	public List<Tile> getTileList(int page) {
		return mongo().find(new Query().skip(pageSize*page).limit(pageSize), Tile.class);
	}


	public List<Decal> getDecalList(int page) {
		return mongo().find(new Query().skip(pageSize*page).limit(pageSize), Decal.class);	
	}

	public List<MapObject> getMapObjectsByTags(String t, final MapObjectType type) {
		Object[] tags = t.split(",");
		for (int i = 0; i < tags.length; i++) {
			tags[i] = tags[i].toString().trim().toLowerCase();
		}
		switch (type){
		case FIGURE:return mongo().find(query(where("tags").all(tags)), MapObject.class, "figure");
		case DECAL:return mongo().find(query(where("tags").all(tags)), MapObject.class, "decal");
		case TILE:return mongo().find(query(where("tags").all(tags)), MapObject.class, "tile");
		default: 
			return null;
		}
	}

	public Map<String, String[]> getMapList(Integer userId) {

		Map<String, String[]> ret = new LinkedHashMap<String, String[]>();
		Query query = query(where("ownerId").in(new Object[]{userId,0}));
		query.fields().include("name").include("owner").include("createdBy");
		List<GameMap> list = mongo().find(query, GameMap.class);
		for (GameMap map : list){
			System.out.println(map);
			ret.put(
					map.getId(), 
					new String[]{
						map.getName(), 
						map.getOwnerId()+"", 
						map.getCreatedBy()
					}
					);
		}
		return ret;

	}

	public boolean passwordValid(String username, String hash) {

		int c = db().queryForInt(DaoSql.checkPassword,new Object[]{username,hash});

		return (c>0)?true:false;

	}

	public User getUser(final String ip, final String drupalSessionId) {
		return (User)db().query(DaoSql.getuserByDrupalSession, new Object[]{drupalSessionId},new ResultSetExtractor() {

			@Override
			public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {

				if (rs.next()){
					if (ip!=null && !ip.equals("127.0.0.1")){
						String hostname = rs.getString("hostname");
						if (!ip.equals(hostname)){
//							throw new RuntimeException(
//									"HAX! received login attempt from "+
//											ip+" but session is for "+hostname
//									);
						}
					}
					return new User(
							rs.getString("account_level"),
							new Timestamp(rs.getLong("create_date")*1000),
							rs.getString("email"),
							rs.getInt("uid"),
							rs.getString("username")
							);
				}

				return null;
			}
		});
	}

	public Map<String,String> getUserPreferences(int userId){

		return (Map<String,String>)db().query(DaoSql.getUserPrefs, new Object[]{userId}, new ResultSetExtractor() {

			@Override
			public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {
				Map<String,String> ret = new HashMap<String, String>();

				while (rs.next()){
					ret.put(rs.getString("key"), rs.getString("value"));
				}

				return ret;
			}
		});
	}

	public void updateUserPrefs(int userId, Map<String,String> prefs){

		db().update(DaoSql.deleteUserPrefs, new Object[]{userId});

		String sql = DaoSql.insertUserPrefs;
		List<String> params = new ArrayList<String>();

		for (Map.Entry<String, String> e : prefs.entrySet()){
			sql+="(?,?,?),";
			params.add(""+userId);
			params.add(e.getKey());
			params.add(e.getValue());
		}
		sql = sql.substring(0,sql.length()-1);
		System.out.println(sql);
		System.out.println(params);

		db().update(sql,params.toArray());


	}

	public List<GameBrief> getGameList(int userId) {

		return db().query(DaoSql.getGameList, new Object[]{userId}, new RowMapper<GameBrief>(){

			@Override
			public GameBrief mapRow(ResultSet set, int row)
					throws SQLException {

				return new GameBrief(set.getString("description"), set.getInt("nid"), set.getString("title"));

			}

		});

	}

	public User getUserByEmail(String i){

		return (User)db().query(DaoSql.getUserByEmail, new Object[]{i}, new ResultSetExtractor() {

			@Override
			public Object extractData(ResultSet set) throws SQLException,
			DataAccessException {

				if (!set.next())return null;
				return new User(
						set.getString("account_level"),
						new Timestamp(set.getLong("create_date")*1000),
						set.getString("email"),
						set.getInt("uid"),
						set.getString("username")
						);

			}
		});

	}


	public User getUserByUsername(String i){

		return (User)db().query(DaoSql.getUserByUsername, new Object[]{i}, new ResultSetExtractor() {

			@Override
			public Object extractData(ResultSet set) throws SQLException,
			DataAccessException {

				if (!set.next())return null;
				return new User(
						set.getString("account_level"),
						new Timestamp(set.getLong("create_date")*1000),
						set.getString("email"),
						set.getInt("uid"),
						set.getString("username")
						);

			}
		});

	}

	public static void main(String[] args) throws Exception {


//		Figure figure = new Figure();
//		figure.setName("Proto-Man");
//		figure.setHeight(1);
//		figure.setWidth(1);
//		
//		List<String> tags = new ArrayList<String>();
//		tags.add("custom");
//		tags.add("proto");
//		figure.setTags(tags);
//		
//		Map<String,String> stances = new HashMap<String,String>();
		String path = "/sprites/characters/Male/custom/";
//		for (String pole : new String[]{"N", "NW", "W", "SW", "S", "SE", "E", "NE" }){
//		stances.put(pole,path+"Male_01-"+pole+".gif");
//		}
//		figure.setStances(stances);
//		
//		System.out.println(figure);
		
		Dao dao = new Dao();

		User user = dao.getUser(1);
		
		System.out.println(user);
		
//		for (String key : figure.getStances().keySet()){
//			figure.getStances().put(key,path+"Male_01-"+key+".gif");
//		}
//		dao.saveFigure(figure);
		
		
		

//		doConversion("CharacterSheetV2");
		//
//		CharacterSheet cs = new Dao().getCharacterSheet("57de9acf-84be-4d96-b315-f8d65e4852db");
//		//		CharacterSheet cs = new Dao().getCharacterSheetByUser(1);
//
//		System.out.println(cs);
//
//		for (Entry<Type, Ability> s : cs.getAbilityScores().entrySet()){
//			System.out.println(s.getKey()+"="+s.getValue());
//		}
//		System.out.println("fort="+cs.getScore(SavingThrow.Type.FORT).getAdjusted());
//		System.out.println("reflex="+cs.getScore(SavingThrow.Type.REFLEX).getAdjusted());
//		System.out.println("will="+cs.getScore(SavingThrow.Type.WILL).getAdjusted());
//
//		System.out.println("atk="+cs.getScore(CombatStatType.BASEATTACK).getAdjusted());
//		System.out.println("melee="+cs.getScore(CombatStatType.MELEE).getAdjusted());
//		System.out.println("ranged="+cs.getScore(CombatStatType.RANGED).getAdjusted());
//		System.out.println("cmb="+cs.getScore(CombatStatType.CMB).getAdjusted());
//		System.out.println("cmd="+cs.getScore(CombatStatType.CMD).getAdjusted());
//		System.out.println("ac="+cs.getScore(CombatStatType.AC).getAdjusted());
//		System.out.println("Max Skills="+cs.getScore(Misc.MAX_SKILLS).getAdjusted());
//
//		System.out.println();
//
//		cs.getClassModels()[0].setLevel(5);
//
//		for (Entry<Integer, List<SpellSlot>> s : cs.getClassModels()[0].getSpellSlots().entrySet()){
//			System.out.println(s.getKey()+"="+s.getValue().size());
//		}


//		final Dao dao = new Dao();
//		dao.mongo().executeQuery(new Query(), "characterDocument.bak", new DocumentCallbackHandler() {
//
//			@Override
//			public void processDocument(DBObject dbObject) throws MongoException,
//			DataAccessException {
//
//
//				try {
//					CharacterDocument cd = dao.mongo().getConverter().read(CharacterDocument.class, dbObject);
//					System.out.println("\n****************** "+cd.id+"\n");
//
//					CharacterSheetImpl cs = (CharacterSheetImpl)Serializer.fromString(cd.characterSheetSerialized);
//					cs.setId(cd.id);
//
//					if (!cd.id.contains("-")){
//						dao.mongo().remove(query(where("id").is(cd.id)), "characterDocument.bak");
//						cd.id = getNewId();
//						cs.setId(cd.id);
//						dao.mongo().save(cd, "characterDocument.bak");
//					}
//
//					CharacterSheetV2 ncs = CharacterConverter.convert(cs);
//
//					cd.characterSheetSerialized = Serializer.toString(ncs);
//					cd = new CharacterDocument(ncs);
//
//					System.out.println(cd.name);
//					dao.mongo().save(cd);
//
//				} catch (Exception e) {
//					logger.error(e, e);
//				}
//
//			}
//		});

		
		

	}

	public static void transferGameMap() throws UnknownHostException,
	IOException, ClassNotFoundException {
		MongoTemplate from = new MongoTemplate(new Mongo("mt-db"), "mystictriad");
		MongoTemplate to = new MongoTemplate(new Mongo("mystictriad.com"), "mystictriad");
		GameMap gameMap = from.findById("26b59f43-87bb-4591-96fc-c56d8d4d575f", GameMap.class);
		System.out.println(gameMap);
		to.save(gameMap);
		System.out.println("saved");
	}

	public static void doConversion(String conversion) {
		final Dao dao = new Dao();
		MongoMigration mm = dao.mongo().findOne(query(where("conversion").is(conversion)), MongoMigration.class);
		if (mm!=null){
			logger.fatal("skipping conversion "+conversion+" already completed on "+mm.timestamp);
			return;
		} else logger.fatal("RUNNING CONVERSION "+conversion);

		if (conversion.equals("CharacterSheetV2")){
			dao.mongo().executeQuery(new Query(), "characterDocument", new DocumentCallbackHandler() {

				@Override
				public void processDocument(DBObject dbObject) throws MongoException,
				DataAccessException {
					CharacterDocument cd = dao.mongo().getConverter().read(CharacterDocument.class, dbObject);
					System.out.println("\n****************** "+cd.id+"\n");
					dao.mongo().save(cd, "characterDocument.bak");
				}
			});

			dao.mongo().executeQuery(new Query(), "characterDocument.bak", new DocumentCallbackHandler() {

				@Override
				public void processDocument(DBObject dbObject) throws MongoException,
				DataAccessException {


					try {
						CharacterDocument cd = dao.mongo().getConverter().read(CharacterDocument.class, dbObject);
						System.out.println("\n****************** "+cd.id+"\n");

						CharacterSheetImpl cs = (CharacterSheetImpl)Serializer.fromString(cd.characterSheetSerialized);
						cs.setId(cd.id);

						if (!cd.id.contains("-")){
							dao.mongo().remove(query(where("id").is(cd.id)), "characterDocument.bak");
							cd.id = getNewId();
							cs.setId(cd.id);
							dao.mongo().save(cd, "characterDocument.bak");
						}

						CharacterSheetV2 ncs = CharacterConverter.convert(cs);

						cd.characterSheetSerialized = Serializer.toString(ncs);
						cd = new CharacterDocument(ncs);

						System.out.println(cd.name);
						dao.mongo().save(cd);

					} catch (Exception e) {
						logger.error(e, e);
					}

				}
			});
		}
		
		dao.mongo().save(new MongoMigration(conversion));
		logger.fatal("completed conversion "+conversion);



	}

	public Invite getInvite(String code) {
		return mongo().findOne(query(where("code").is(code)), Invite.class);
	}

	public void deleteInvite(String id){
		mongo().remove(query(where("id").is(id)));
	}

	public Invite createInvite(String email, int gameNid) {

		final Invite invite = new Invite(email, gameNid);

		mongo().save(invite);

		return invite;

	}

	//	public void changeGamesMap(String gameId, String mapId) {
	//		mongo.updateFirst(query(where("id").is(gameId)), update("mapId", mapId), GameDocument.class);
	//	}

	public void updateMapName(String mapId, String mapName) {
		mongo.updateFirst(query(where("id").is(mapId)), update("mapName", mapName), GameMap.class);
	}

	public CharacterSheet getCharacterSheet(String id) {
		try {
			CharacterDocument cd = mongo().findOne(query(where("id").is(id)), CharacterDocument.class);
			if (cd==null)return null;
			CharacterSheet cs = (CharacterSheet) Serializer.fromString(cd.characterSheetSerialized);
			cs.setId(cd.id);
			return cs;
		} catch (IOException e) {
			logger.error(e, e);
		} catch (ClassNotFoundException e){
			logger.error(e, e);
		}
		return null;
	}

	public CharacterSheet getCharacterSheetByUser(int userId){
		try {
			Query query = query(where("ownerId").is(userId));
			query.sort().on("timestamp", Order.DESCENDING);
			query.limit(1);
			List<CharacterDocument> cds = mongo().find(query, CharacterDocument.class);
			if (cds.size()==0)return null;
			CharacterDocument cd = cds.get(0);
			if (cd==null){
				CharacterSheet cs = CharacterSheet.create();
				saveCharacterSheet(cs);
				return cs;
			}
			CharacterSheet cs = (CharacterSheet) Serializer.fromString(cd.characterSheetSerialized);
			logger.debug("got CS "+cd.id+" from mongo, version is "+cs.getClass().getSimpleName());
			cs.setId(cd.id);
			return cs;
		} catch (IOException e) {
			logger.error(e, e);
		} catch (ClassNotFoundException e){
			logger.error(e, e);
		}
		return null;
	}


	public void saveCharacterSheet(CharacterSheet cs){
		if (cs.getId()==null)cs.setId(getNewId());
		CharacterDocument cd = new CharacterDocument(cs);
		cd.timestamp = getTimeString();
		mongo().save(cd);
		cs.setId(cd.id);
	} 


	public static String getTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat(Defaults.DATE_FORMAT);
		return sdf.format(new Date());
	}

	public Map<String, CharacterBrief> getCharacterList(Integer userId) {

		Map<String, CharacterBrief> ret = new TreeMap<String, CharacterBrief>();		
		for (CharacterDocument cd : mongo().find(query(where("ownerId").is(userId).and("isTempCharacter").ne(true)), CharacterDocument.class)){
			try {
				CharacterSheet cs = (CharacterSheet)Serializer.fromString(cd.characterSheetSerialized);
				cs.setId(cd.id);
				String name = cs.getCharacterName().toLowerCase();
				String newName = name;
				int inc = 0;
				while (ret.containsKey(newName)){
					inc++;
					newName = name+"-"+inc;
				}
				ret.put(newName, new CharacterBrief(cs));
				logger.debug("adding "+cs.getId()+":"+cs.toString());
			} catch (Exception e) {
				logger.error(e, e);
			}
		}

		return ret;
	}

	public void deleteCharacter(String id) {

		mongo().remove(query(where("id").is(id)), CharacterDocument.class);

	}

	public void saveChatMessage(ChatMessage cm) {
		SimpleDateFormat sdf = new SimpleDateFormat(Defaults.DATE_FORMAT);
		cm.timestamp = sdf.format(new Date());
		mongo().save(cm);
	}

	public List<ChatMessage> getRecentChatMessages(int nid){

		SimpleDateFormat sdf = new SimpleDateFormat(Defaults.DATE_FORMAT);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String timestamp = sdf.format(c.getTime());
		System.out.println(timestamp);
		return mongo().find(query(where("gameNid").is(nid).and("timestamp").gte(timestamp)), ChatMessage.class);

	}

}