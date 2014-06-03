package com.mut8ed.battlemap.server.dao;


public class DaoOld {

//	private JdbcTemplate db;
//	public static final Logger logger = Logger.getLogger(Dao.class);
//	private TransactionTemplate tt;
//	private final static String BASE_IMAGE_PATH = Defaults.IMGURL+"";
//
//	public DaoOld(){
//		db = new JdbcTemplate(DaoUtil.getDataSource());
//		DataSourceTransactionManager txManager = new DataSourceTransactionManager(db.getDataSource());
//		tt = new TransactionTemplate(txManager);
//	}
//
//	public void updateFigure(final Figure obj){ 
//		logger.debug("updating Figure"); 
//		if (obj.getId()==null || obj.getId()<1){ 
//			//			TODO insertFigure(obj); 
//			return; 
//		} 
//		class Query extends UpdatableSqlQuery { 
//			public Query(DataSource ds, String sql) { 
//				super(ds, sql); 
//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//				compile(); 
//			} 
//			@Override 
//			protected Object updateRow(ResultSet rs, int rowNum,  Map context) 
//					throws SQLException { 
//				if (obj.getId()!=null)rs.updateInt("id", obj.getId()); 
//				if (obj.getName()!=null)rs.updateString("name", obj.getName()); 
//				return null; 
//			}			 
//		} 
//
//
//		Query query = new Query(db.getDataSource(), DaoSql.getFigureById); 
//		query.compile(); 
//		query.execute(new Object[]{obj.getId()}); 
//
//	} 
//
//	public Figure getFigure(int id) {
//		logger.debug("getting figure "+id);
//
//		return (Figure)db.query(
//				DaoSql.getFigureById, new Object[]{id}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						Figure figure = new Figure(
//								set.getString("figure_id"),
//								set.getString("name")
//								);
//
//						do {
//							figure.addStance(
//									set.getString("stance"),
//									BASE_IMAGE_PATH+set.getString("filepath")
//									);
//							figure.addTag(set.getString("tag"));
//						} while (set.next());
//
//						return figure;
//					}
//
//				});
//	}
//
//	//	public void insertUser(final User obj){ 
//	//		if (obj.getId()!=null){ 
//	//			updateUser(obj); 
//	//			return; 
//	//		} 
//	//		tt.execute(new TransactionCallbackWithoutResult() {
//	//
//	//			@Override
//	//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//	//				db.update( 
//	//						DaoSql.insertUser,  
//	//						new Object[]{ 
//	//								obj.getAccountLevel(),
//	//								obj.getEmail(),
//	//								obj.getPasswordHash(),
//	//								obj.getUsername(),
//	//						}, 
//	//						new int[]{ 
//	//								Types.VARCHAR,
//	//								Types.VARCHAR,
//	//								Types.VARCHAR,
//	//								Types.VARCHAR,
//	//								Types.VARCHAR,
//	//								Types.VARCHAR,
//	//						} 
//	//
//	//				); 
//	//
//	//				obj.setId(getLastInsertId());
//	//			}
//	//		});
//	//
//	//	}	
//
//
//	//	public void insertFigure(final Figure obj){ 
//	//		if (obj.getId()!=null){ 
//	//			updateFigure(obj); 
//	//			return; 
//	//		} 
//	//		tt.execute(new TransactionCallbackWithoutResult() {
//	//
//	//			@Override 
//	//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//	//				db.update( 
//	//						DaoSql.insertFigure,  
//	//						new Object[]{ 
//	//								obj.getName(),
//	//								obj.getSubtype(),
//	//								obj.getType(),
//	//						}, 
//	//						new int[]{ 
//	//								Types.VARCHAR,
//	//								Types.VARCHAR,
//	//								Types.VARCHAR,
//	//
//	//						} 
//	//
//	//				); 
//	//
//	//				obj.setId(getLastInsertId()); 
//	//			} 
//	//		});
//	//
//	//	}
//
//	//	public void updateUser(final User obj){ 
//	//		logger.debug("updating User"); 
//	//		if (obj.getId()==null){ 
//	//			insertUser(obj); 
//	//			return; 
//	//		} 
//	//		class Query extends UpdatableSqlQuery { 
//	//			public Query(DataSource ds, String sql) { 
//	//				super(ds, sql); 
//	//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//	//				compile(); 
//	//			} 
//	//			@Override 
//	//			protected Object updateRow(ResultSet rs, int rowNum, Map context) 
//	//			throws SQLException { 
//	//				if (obj.getAccountLevel()!=null)rs.updateString("account_level", obj.getAccountLevel().toString()); 
//	//				if (obj.getEmail()!=null)rs.updateString("email", obj.getEmail()); 
//	//				if (obj.getPasswordHash()!=null)rs.updateString("password_hash", obj.getPasswordHash()); 
//	//				if (obj.getUsername()!=null)rs.updateString("username", obj.getUsername()); 
//	//				return null; 
//	//
//	//			}			 
//	//		} 
//	//
//	//
//	//		Query query = new Query(db.getDataSource(), DaoSql.getUserById); 
//	//		query.compile(); 
//	//		query.execute(new Object[]{obj.getId()}); 
//	//
//	//	} 
//
//	private Integer getLastInsertId() {
//		return db.queryForInt("select last_insert_id() limit 1");
//	}
//
//	public User getUser(int key) {
//
//		return (User)db.query(
//				DaoSql.getUserById, new Object[]{key}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						return new User(
//								set.getString("account_level"),
//								new Timestamp(set.getLong("create_date")*1000),
//								set.getString("email"),
//								set.getInt("uid"),
//								set.getString("password_hash"),
//								set.getString("username")
//								);
//					}
//
//				});
//	}
//
//	//	public User getUser(String username, String hash){
//	//		try {
//	//			return (User)db.query(
//	//					DaoSql.getUserByUsernameAndHash, new Object[]{username,hash}, new ResultSetExtractor(){
//	//
//	//						@Override
//	//						public Object extractData(ResultSet set)
//	//						throws SQLException, DataAccessException {
//	//
//	//							if (!set.next())return null;
//	//							return new User(
//	//									set.getString("account_level"),
//	//									set.getTimestamp("create_date"),
//	//									set.getString("email"),
//	//									set.getString("first_name"),
//	//									set.getInt("id"),
//	//									set.getString("last_name"),
//	//									set.getString("password_hash"),
//	//									set.getString("username")
//	//							);
//	//						}
//	//
//	//					});
//	//		} catch (DataAccessException e) {
//	//			logger.error(e,e);
//	//			return null;
//	//		}		
//	//	}
//
//
//	public Figure getFigureByName(String figureName) {
//		return getFigure(db.queryForInt("select id from figure where name = ?", new Object[]{figureName}));
//	}
//
//
//	public List<Figure> getRandomFigureList() {
//
//		return (List<Figure>)db.query(
//				DaoSql.getSomeRandomFigures, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//
//						List<Figure> figures = processFiguresResultSet(set);
//						logger.info("retrieved "+figures.size()+" figures for figure list");
//						return figures;
//					}
//
//
//				});
//	}
//
//	private List<Figure> processFiguresResultSet(ResultSet set) throws SQLException {
//
//		List<Figure> figures = new ArrayList<Figure>();
//		Figure figure = null;
//
//		while (set.next()){
//
//			if (figure==null || set.getInt("id")!=figure.getId()){
//				figure = new Figure(
//						set.getInt("id"),
//						set.getString("name")
//						);
//				figures.add(figure);
//			}
//
//			figure.addStance(
//					set.getString("stance"),
//					BASE_IMAGE_PATH+set.getString("filepath")
//					);
//			figure.addTag(set.getString("tag"));
//
//		}
//
//		return figures;
//	}
//
//	//	private void loadFigureStances(final Figure figure) {
//	//		db.query(
//	//				DaoSql.getFigureStancesByFigureId, 
//	//				new Object[]{figure.getId()}, 
//	//				new RowCallbackHandler() {
//	//
//	//					@Override
//	//					public void processRow(ResultSet rs) throws SQLException {
//	//						figure.addStance(rs.getString("stance"), rs.getString("filepath"));
//	//						figure.addTag(rs.getString("tag"));
//	//					}
//	//				});
//	//
//	//	}
//
//	//	private void loadFigureTags(final Figure figure) {
//	//		db.query(
//	//				DaoSql.getFigureTagsByFigureId, 
//	//				new Object[]{figure.getId()}, 
//	//				new RowCallbackHandler() {
//	//
//	//					@Override
//	//					public void processRow(ResultSet rs) throws SQLException {
//	//						figure.addTag(rs.getString("tag"));
//	//					}
//	//				});
//	//	}
//
//	public MapObjectWrapper getMapObject(int id) {
//
//		return (MapObjectWrapper)db.query(
//				DaoSql.getMapObject, new Object[]{id}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						return processMapObjectRow(set);
//					}
//
//				});
//	}
//
//
//	private MapObjectWrapper processMapObjectRow(ResultSet set) throws SQLException {
//		MapObjectWrapper mow = new MapObjectWrapper(
//				set.getInt("id"),
//				set.getDate("create_date"),
//				set.getInt("height"),
//				set.getInt("map_id"),
//				set.getString("name"),
//				set.getBoolean("visible"),
//				set.getInt("width"),
//				set.getInt("x"),
//				set.getInt("y"),
//				set.getInt("z"),
//				set.getInt("rotation")
//				);
//		MapObjectType type = MapObjectType.valueOf(set.getString("type"));
//		int objectId = set.getInt("type_id");
//		switch (type){
//		case NOTE:
//			mow.setObj(getNote(objectId));
//			break;
//		case FIGURE:
//			mow.setObj(getFigure(objectId));
//			break;
//		case TILE:
//			mow.setObj(getTile(objectId));
//			break;
//		case DECAL:
//			mow.setObj(getDecal(objectId));
//			break;
//		default://freak out!
//		}
//		logger.debug("got map object "+genToString(mow));
//		return mow;
//	}
//
//	public void updateMapObject(final MapObjectWrapper mow){ 
//		if (mow.getId()==null || mow.getId()<1){ 
//			insertMapObject(mow); 
//			return; 
//		}
//		if (mow.getMapObjectType().equals(MapObjectType.NOTE)){
//			updateNote((Note)mow.getObj());
//		}
//		logger.debug("updating MapObjectWrapper "+genToString(mow)); 
//		class Query extends UpdatableSqlQuery { 
//			public Query(DataSource ds, String sql) { 
//				super(ds, sql); 
//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//				compile(); 
//			} 
//			@Override 
//			protected Object updateRow(ResultSet rs, int rowNum,  Map context) 
//					throws SQLException { 
//				if (mow.getCreateDate()!=null)rs.updateTimestamp("create_date", new Timestamp(mow.getCreateDate().getTime())); 
//				if (mow.getHeight()!=null)rs.updateInt("height", mow.getHeight()); 
//				if (mow.getId()!=null)rs.updateInt("id", mow.getId()); 
//				if (mow.getMapId()!=null)rs.updateInt("map_id", mow.getMapId()); 
//				if (mow.getName()!=null)rs.updateString("name", mow.getName()); 
//				if (mow.getMapObjectType()!=null)rs.updateString("type", mow.getMapObjectType().name()); 
//				if (mow.getObjectId()!=null)rs.updateInt("type_id", mow.getObjectId()); 
//				if (mow.isVisible()!=null)rs.updateBoolean("visible", mow.isVisible()); 
//				if (mow.getWidth()!=null)rs.updateInt("width", mow.getWidth()); 
//				if (mow.getX()!=null)rs.updateInt("x", mow.getX()); 
//				if (mow.getY()!=null)rs.updateInt("y", mow.getY()); 
//				if (mow.getRotation()!=null)rs.updateInt("rotation", mow.getRotation());
//				return null; 
//
//			}			 
//		} 
//
//		Query query = new Query(db.getDataSource(), DaoSql.getMapObject); 
//		query.compile(); 
//		query.execute(new Object[]{mow.getId()}); 
//
//	} 
//
//	public void insertMapObject(final MapObjectWrapper obj){
//		if (obj.getId()!=null){ 
//			updateMapObject(obj); 
//			return; 
//		} 
//		if (obj.getMapObjectType().equals(MapObjectType.NOTE)){
//			updateNote((Note)obj.getObj());
//		}
//		logger.debug("inserting map object "+genToString(obj));
//		tt.execute(new TransactionCallbackWithoutResult() {
//
//			@Override 
//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//				db.update( 
//						DaoSql.insertMapObject,  
//						new Object[]{ 
//								obj.getCreateDate(),
//								obj.getHeight(),
//								obj.getMapId(),
//								obj.getName(),
//								obj.getMapObjectType().name(),
//								obj.getObjectId(),
//								obj.isVisible(),
//								obj.getWidth(),
//								obj.getX(),
//								obj.getY(),
//								obj.getZ(),
//								obj.getRotation()
//						}, 
//						new int[]{ 
//								Types.TIMESTAMP,
//								Types.INTEGER,
//								Types.INTEGER,
//								Types.VARCHAR,
//								Types.VARCHAR,
//								Types.INTEGER,
//								Types.BOOLEAN,
//								Types.INTEGER,
//								Types.INTEGER,
//								Types.INTEGER,
//								Types.INTEGER,
//								Types.INTEGER,
//						} 
//						); 
//
//				obj.setId(getLastInsertId()); 
//			} 
//		});
//
//	}
//
//	//	private boolean mapObjectExists(Integer mapId, Integer counterId) {
//	//
//	//		int count = db.queryForInt(DaoSql.checkIfMapObjectExists, new Object[]{mapId,counterId});
//	//		return (count>0);
//	//
//	//	}
//
//	public GameMap getGameMap(int id) {
//
//		final GameMap map = (GameMap)db.query(
//				DaoSql.getGameMap, new Object[]{id}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						return new GameMap(
//								set.getInt("cell_count_x"),
//								set.getInt("cell_count_y"),
//								set.getString("created_by"),
//								set.getDate("create_date"),
//								set.getInt("id"),
//								set.getString("name"),
//								set.getInt("owner_id")
//								);
//					}
//
//				}
//				);
//
//		if (map==null)return null;
//
//		db.query(DaoSql.getMapObjectsByMapId, new Object[]{id},new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet set) throws SQLException,
//			DataAccessException {
//				while (set.next()){
//					map.addMapObject(processMapObjectRow(set));
//				}
//				return null;
//			}
//		});
//
//		db.query(DaoSql.getMapLayers, new Object[]{id}, new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet rs) throws SQLException,
//			DataAccessException {
//				while(rs.next()){
//					map.setLayerName(rs.getInt("layer_index"), rs.getString("name"));
//					map.setLayerVisible(rs.getInt("layer_index"), rs.getBoolean("visible"));
//				}
//				return null;
//			}
//		});
//
//		return map;
//	}
//
//	public void updateGameMap(final GameMap gameMap){ 
//		logger.debug("updating GameMap:"+genToString(gameMap)); 
//		if (gameMap.getId()==null){ 
//			insertGameMap(gameMap); 
//			return; 
//		} 
//		class Query extends UpdatableSqlQuery { 
//			public Query(DataSource ds, String sql) { 
//				super(ds, sql); 
//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//				compile(); 
//			} 
//			@Override 
//			protected Object updateRow(ResultSet rs, int rowNum,  Map context) 
//					throws SQLException {
//				rs.updateInt("cell_count_x", gameMap.getCellCountX());
//				rs.updateInt("cell_count_y", gameMap.getCellCountY());
//				if (gameMap.getCreateDate()!=null)rs.updateTimestamp("create_date", new Timestamp(gameMap.getCreateDate().getTime())); 
//				if (gameMap.getId()!=null)rs.updateInt("id", gameMap.getId()); 
//				if (gameMap.getName()!=null)rs.updateString("name", gameMap.getName());
//				rs.updateInt("owner_id", gameMap.getOwnerId());
//				return null; 
//
//			}			 
//		}
//
//		Query query = new Query(db.getDataSource(), DaoSql.getGameMap); 
//		query.compile(); 
//		query.execute(new Object[]{gameMap.getId()});
//
//		//		for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
//		//			mow.setMapId(gameMap.getId());
//		//			updateMapObject(mow);
//		//		}
//
//		updateMapLayers(gameMap);
//
//	}
//
//	public void updateMapLayers(final GameMap gameMap) {
//		clearMapLayers(gameMap.getId());
//		for (Map.Entry<Integer, String> e : gameMap.getLayerNames().entrySet()){
//			insertMapLayer(gameMap.getId(), e.getKey(), e.getValue(),gameMap.getLayerVisibilities().get(e.getKey()));
//		}
//	} 
//
//	private void insertMapLayer(int mapId, int layerIndex, String layerName, Boolean visible) {
//		db.update(DaoSql.insertMapLayer, new Object[]{mapId, layerIndex, layerName, ((visible!=null)?visible:true)});
//	}
//
//	private void clearMapLayers(int mapId) {
//		db.update(DaoSql.clearMapLayers, new Object[]{mapId});
//	}
//
//
//	public void deleteMapObject(MapObjectWrapper mow) {
//		if (mow==null){
//			logger.debug("cant delete null MOW");
//		}
//		logger.debug("deleting "+genToString(mow));
//		if (mow.getId()==null){
//			logger.debug("object hasn't been inserted, so no need to delete.");
//			return;
//		}
//		db.update(DaoSql.deleteMapObject, new Object[]{mow.getId()});
//	}
//
//	public void insertGameMap(final GameMap gameMap){
//		logger.debug("inserting game map:"+genToString(gameMap));
//		if (gameMap.getId()!=null){ 
//			updateGameMap(gameMap); 
//			return; 
//		} 
//		tt.execute(new TransactionCallbackWithoutResult() {
//
//			@Override 
//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//				db.update( 
//						DaoSql.insertGameMap,  
//						new Object[]{ 
//								gameMap.getName(),
//								gameMap.getOwnerId()
//						}, 
//						new int[]{ 
//								Types.VARCHAR,
//								Types.INTEGER
//
//						} 
//
//						); 
//
//				gameMap.setId(getLastInsertId()); 
//			} 
//		});
//
//		logger.debug("we have "+gameMap.getMapObjects().size()+" map objects to insert");
//
//		for (MapObjectWrapper mow : gameMap.getMapObjects().values()){
//			mow.setMapId(gameMap.getId());
//			insertMapObject(mow);
//			if (mow.getMapObjectType().equals(MapObjectType.NOTE)){
//				updateNote((Note)mow.getObj());
//			}
//		}
//
//		for (Map.Entry<Integer, String> e : gameMap.getLayerNames().entrySet()){
//			insertMapLayer(gameMap.getId(), e.getKey(),e.getValue(),gameMap.getLayerVisibilities().get(e.getKey()));
//		}
//
//	}
//
//	public Game getGame(int id) {
//
//		if (id==-1){
//			Game game = new Game();
//			game.setId(-1);
//			game.setGameMap(new GameMap());
//			return game;
//		}
//
//
//		final Game game = (Game)db.query(
//				DaoSql.getGame, new Object[]{id}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						return new Game(
//								set.getDate("create_date"),
//								set.getString("description"),
//								set.getInt("owner_id"),
//								set.getInt("id"),
//								set.getInt("map_id"),
//								set.getString("title")
//								);
//					}
//
//				}
//				);
//
//		if (game==null){
//			throw new InvalidGameIdException(id);
//		}
//
//		db.query(DaoSql.getPlayersByGameId, new Object[]{game.getId()}, new RowCallbackHandler(){
//
//			@Override
//			public void processRow(ResultSet set) throws SQLException {
//				User user = getUser(set.getInt("user_id"));
//				user.setCharacterName(set.getString("character_name"));
//				game.addPlayer(user);
//			}
//
//		});
//
//		return game;
//
//	}
//
//	public void updateGame(final Game game){
//
//		if (game.getId()==null || game.getId()==-1)return;
//
//		logger.debug("updating Game"); 
//		if (game.getId()==null){ 
//			insertGame(game); 
//			return; 
//		} 
//		class Query extends UpdatableSqlQuery { 
//			public Query(DataSource ds, String sql) { 
//				super(ds, sql); 
//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//				compile(); 
//			} 
//			@Override 
//			protected Object updateRow(ResultSet rs, int rowNum,  Map context) 
//					throws SQLException { 
//				if (game.getCreateDate()!=null)rs.updateTimestamp("create_date", new Timestamp(game.getCreateDate().getTime())); 
//				if (game.getDescription()!=null)rs.updateString("description", game.getDescription()); 
//				if (game.getDm()!=null)rs.updateInt("owner_id", game.getOwner().getId()); 
//				if (game.getId()!=null)rs.updateInt("id", game.getId()); 
//				if (game.getGameMap()!=null)rs.updateInt("map_id", game.getGameMap().getId()); 
//				if (game.getTitle()!=null)rs.updateString("title", game.getTitle()); 
//				return null; 
//
//			}			 
//		} 
//
//
//		Query query = new Query(db.getDataSource(), DaoSql.getGame); 
//		query.compile(); 
//		query.execute(new Object[]{game.getId()});
//
//		//		for (Entry<User,String> player : game.getPlayers().entrySet()){
//		//			updateGamePlayer(game.getId(), player.getValue(), player.getKey());
//		//		}
//
//		//		updateGameMap(game.getGameMap());
//		//
//		//		for (MapObjectWrapper mo : game.getGameMap().getMapObjects().values()){
//		//			updateMapObject(mo);
//		//		}
//
//	}
//
//	public void insertGame(final Game obj){
//		if (obj.getId()!=null && obj.getId()==-1)return;
//		if (obj.getId()!=null){ 
//			updateGame(obj); 
//			return; 
//		} 
//		tt.execute(new TransactionCallbackWithoutResult() {
//
//			@Override 
//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//				db.update( 
//						DaoSql.insertGame,  
//						new Object[]{ 
//								obj.getCreateDate(),
//								obj.getDescription(),
//								obj.getDm().getId(),
//								obj.getId(),
//								obj.getGameMap().getId(),
//								obj.getTitle(),
//						}, 
//						new int[]{ 
//								Types.TIMESTAMP,
//								Types.VARCHAR,
//								Types.INTEGER,
//								Types.INTEGER,
//								Types.INTEGER,
//								Types.VARCHAR,
//
//						} 
//
//						); 
//
//				obj.setId(getLastInsertId()); 
//			} 
//		});
//
//		for (User u : obj.getPlayers()){
//			insertGamePlayer(obj.getId(), u);
//		}
//		updateGameMap(obj.getGameMap());
//		for (MapObjectWrapper mo : obj.getGameMap().getMapObjects().values()){
//			updateMapObject(mo);
//		}
//
//	}
//
//
//
//	public Map<User, String> getGamePlayers(int gameId) {
//
//		final Map<User, String> ret = new HashMap<User, String>();
//
//		db.query(
//				DaoSql.getPlayersByGameId, 
//				new Object[]{gameId}, 
//				new RowCallbackHandler(){
//
//					@Override
//					public void processRow(ResultSet set) throws SQLException {
//
//						ret.put(getUser(set.getInt("user_id")), set.getString("character_name"));
//
//					}
//
//
//				}
//				);
//
//		return ret;
//
//	}
//
//
//	public void insertGamePlayer(final int gameId, final User user){ 
//		if (user.getGamePlayerId()!=null){ 
//			updateGamePlayer(gameId, user);
//			return; 
//		} 
//		tt.execute(new TransactionCallbackWithoutResult() {
//
//			@Override 
//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//				db.update( 
//						DaoSql.insertGamePlayer,  
//						new Object[]{ 
//								user.getCharacterName(),
//								gameId,
//								user.getId(),
//						}, 
//						new int[]{ 
//								Types.VARCHAR,
//								Types.INTEGER,
//								Types.INTEGER,
//
//						} 
//
//						); 
//
//				user.setGamePlayerId(getLastInsertId()); 
//			} 
//		});
//
//	}
//
//	public void updateGamePlayer(final int gameId, final User user){ 
//		logger.debug("updating GamePlayer"); 
//		if (user.getGamePlayerId()==null){ 
//			insertGamePlayer(gameId, user); 
//			return; 
//		} 
//
//		db.update(
//				DaoSql.updateGamePlayer, 
//				new Object[]{
//						gameId, 
//						user.getCharacterName(), 
//						user.getId(), 
//						user.getGamePlayerId()
//				}
//				);
//
//	} 
//
//	public Tile getTile(int id) {
//		logger.debug("getting tile "+id);
//		Tile tile = (Tile)db.query(
//				DaoSql.getTileById, new Object[]{id}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						return new Tile(
//								set.getInt("height"),
//								set.getString("tile_id"),
//								BASE_IMAGE_PATH+set.getString("filepath"),
//								set.getInt("width")
//								);
//					}
//
//				});
//		
//		tile.setTags(db.queryForList("select tag from tile_tag where tile_id = ?))
//		
//		
//	}
//
//
//	public void updateTile(int tileId, final Integer imageId, final Integer height, final Integer width){ 
//		logger.debug("updating Tile"); 
//		class Query extends UpdatableSqlQuery { 
//			public Query(DataSource ds, String sql) { 
//				super(ds, sql); 
//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//				compile(); 
//			} 
//			@Override 
//			protected Object updateRow(ResultSet rs, int rowNum,  Map context) 
//					throws SQLException { 
//				if (height!=null)rs.updateInt("height", height);
//				if (imageId!=null){
//					int oldImageId = rs.getInt("image_id");
//					deleteImage(oldImageId);
//					rs.updateInt("image_id", imageId);
//				}
//				if (width!=null)rs.updateInt("width", width); 
//				return null; 
//
//			}
//		} 
//
//
//		Query query = new Query(db.getDataSource(), DaoSql.getTileOnlyById); 
//		query.compile(); 
//		query.execute(new Object[]{tileId}); 
//
//	} 
//
//	public void updateDecal(int decalId, final Integer imageId, final Integer height, final Integer width){ 
//		logger.debug("updating Decal"); 
//		class Query extends UpdatableSqlQuery { 
//			public Query(DataSource ds, String sql) { 
//				super(ds, sql); 
//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//				compile(); 
//			} 
//			@Override 
//			protected Object updateRow(ResultSet rs, int rowNum,  Map context) 
//					throws SQLException { 
//				if (height!=null)rs.updateInt("height", height);
//				if (imageId!=null){
//					int oldImageId = rs.getInt("image_id");
//					deleteImage(oldImageId);
//					rs.updateInt("image_id", imageId);
//				}
//				if (width!=null)rs.updateInt("width", width); 
//				return null; 
//
//			}
//		} 
//
//
//		Query query = new Query(db.getDataSource(), DaoSql.getDecalOnlyById); 
//		query.compile(); 
//		query.execute(new Object[]{decalId}); 
//
//	} 
//
//
//
//	private void deleteImage(int imageId) {
//		String path = (String) db.query(DaoSql.getImagePath, new Object[]{imageId}, new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet set) throws SQLException,
//			DataAccessException {
//				if (set.next()){
//					return set.getString(1);
//				} else return null;
//			}
//		});
//
//		try {
//			new File(MapObjectUploadServlet.FILE_PATH+path).delete();
//		} catch (Exception e) {
//			logger.error(e,e);
//		}
//		db.update(DaoSql.deleteImage,new Object[]{imageId});
//	}
//
//	public Decal getDecal(int id) {
//		logger.debug("getting decal "+id);
//		return (Decal)db.query(
//				DaoSql.getDecalById, new Object[]{id}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						return new Decal(
//								set.getInt("height"),
//								set.getInt("decal_id"),
//								BASE_IMAGE_PATH+set.getString("filepath"),
//								set.getInt("width")
//								);
//					}
//
//				});
//	}
//
//
//	public void updateDecal(final Decal obj){ 
//		logger.debug("updating Decal"); 
//		if (obj.getId()==null){ 
//			insertDecal(obj); 
//			return; 
//		} 
//		class Query extends UpdatableSqlQuery { 
//			public Query(DataSource ds, String sql) { 
//				super(ds, sql); 
//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//				compile(); 
//			} 
//			@Override 
//			protected Object updateRow(ResultSet rs, int rowNum, Map context) 
//					throws SQLException { 
//				if (obj.getHeight()!=null)rs.updateInt("height", obj.getHeight()); 
//				if (obj.getId()!=null)rs.updateInt("id", obj.getId()); 
//				if (obj.getWidth()!=null)rs.updateInt("width", obj.getWidth()); 
//				return null; 
//
//			}			 
//		} 
//
//		Query query = new Query(db.getDataSource(), DaoSql.getDecalById); 
//		query.compile(); 
//		query.execute(new Object[]{obj.getId()}); 
//
//	} 
//
//
//	public void insertDecal(final Decal obj){ 
//		if (obj.getId()!=null){ 
//			updateDecal(obj); 
//			return; 
//		} 
//		tt.execute(new TransactionCallbackWithoutResult() {
//
//			@Override 
//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//				db.update( 
//						DaoSql.insertDecal,  
//						new Object[]{ 
//								obj.getHeight(),
//								obj.getId(),
//								obj.getWidth(),
//						}, 
//						new int[]{ 
//								Types.INTEGER,
//								Types.INTEGER,
//								Types.INTEGER,
//						} 
//						); 
//
//				obj.setId(getLastInsertId()); 
//			} 
//		});
//
//	}
//
//
//	public boolean isDuplicateImage(String md5) {
//		return (Boolean)db.query(DaoSql.checkForDuplicateImage, new Object[]{md5}, new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet rs) throws SQLException,
//			DataAccessException {
//
//				if (rs.next()){
//					logger.info("duplicate image found with id "+rs.getInt("id"));
//					return true;
//				}
//				return false;
//
//			}
//		});
//	}
//
//
//	public int insertImage(final String path, final String md5){ 
//		return (Integer)tt.execute(new TransactionCallback() {
//
//			@Override 
//			public Object doInTransaction(TransactionStatus status) {
//				db.update( 
//						DaoSql.insertImage,  
//						new Object[]{ 
//								path.replace(MapObjectUploadServlet.FILE_PATH, ""),
//								md5,
//						}, 
//						new int[]{ 
//								Types.VARCHAR,
//								Types.VARCHAR,
//						} 
//						); 
//				int id = getLastInsertId(); 
//				logger.debug("inserted image "+id+" with path "+path+" and md5 "+md5);
//				return id;
//			}
//		});
//	}
//
//
//	public void insertObject(MapObjectType type, Map<String, String> params,
//			String imagePath, String md5) {
//
//		String[] t = params.get("tags").split(",");
//		List<String> tags = new ArrayList<String>();
//		for (int i = 0; i < t.length; i++) {
//			if (t[i]==null)continue;
//			t[i] = t[i].trim();
//			if (t[i].equals(""))continue;
//			tags.add(t[i]);
//		}
//		String cat = params.get("category");
//		if (cat!=null && !cat.trim().equals("")){
//			tags.add("c:"+cat.trim());
//		}
//
//		Integer imageId = null;
//		if (imagePath!=null){
//			imageId = insertImage(imagePath, md5);
//		}
//		switch (type){
//		case TILE:
//			if (params.get("object-id")!=null){
//				int tileId = Integer.parseInt(params.get("object-id"));
//
//				updateTile(
//						tileId,
//						imageId,
//						Integer.parseInt(params.get("height")),
//						Integer.parseInt(params.get("width"))
//						);
//
//				updateTileTags(Integer.parseInt(params.get("object-id")), tags);
//
//			} else if (imageId!=null){
//				insertTile(
//						new Timestamp(System.currentTimeMillis()), 
//						Integer.parseInt(params.get("height")),
//						imageId, 
//						Integer.parseInt(params.get("width")),
//						tags
//						);
//			}
//			break;
//		case DECAL:
//			if (params.get("object-id")!=null){
//				int tileId = Integer.parseInt(params.get("object-id"));
//
//				updateDecal(
//						tileId,
//						imageId,
//						Integer.parseInt(params.get("height")),
//						Integer.parseInt(params.get("width"))
//						);
//
//				updateDecalTags(Integer.parseInt(params.get("object-id")), tags);
//
//			} else if (imageId!=null){
//				insertDecal(
//						new Timestamp(System.currentTimeMillis()), 
//						Integer.parseInt(params.get("height")),
//						imageId, 
//						Integer.parseInt(params.get("width")),
//						tags
//						);
//			}
//			break;
//		default:logger.fatal("I should not be able to get here");
//		}
//	}
//
//	private void updateTileTags(int tileId, List<String> tags) {
//
//		db.update(DaoSql.deleteAllTileTags, new Object[]{tileId});
//		for (String tag : tags){
//			insertTileTag(tileId,tag);
//		}
//
//	}
//
//	private void updateDecalTags(int decalId, List<String> tags) {
//
//		db.update(DaoSql.deleteAllDecalTags, new Object[]{decalId});
//		for (String tag : tags){
//			insertDecalTag(decalId,tag);
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param createDate
//	 * @param height
//	 * @param imageId
//	 * @param width
//	 * @param tags 
//	 * @return id of inserted tile
//	 */
//	public int insertTile(
//			final Timestamp createDate, 
//			final int height, 
//			final int imageId, 
//			final int width, 
//			List<String> tags
//			) {
//		int tileId = (Integer)tt.execute(new TransactionCallback() {
//
//			@Override
//			public Object doInTransaction(TransactionStatus arg0) {
//				db.update(DaoSql.insertTile, new Object[]{createDate, height, null, imageId, width});
//				return getLastInsertId();
//			}
//		});
//
//		for (String tag : tags){
//			insertTileTag(tileId,tag);
//		}
//
//		return tileId;
//	}
//
//	public int insertDecal(
//			final Timestamp createDate, 
//			final int heightPx50, 
//			final int imageId, 
//			final int widthPx50, 
//			List<String> tags
//			) {
//		int decalId = (Integer)tt.execute(new TransactionCallback() {
//
//			@Override
//			public Object doInTransaction(TransactionStatus arg0) {
//				db.update(DaoSql.insertDecal, new Object[]{createDate, heightPx50, null, imageId, widthPx50});
//				return getLastInsertId();
//			}
//		});
//
//		for (String tag : tags){
//			insertDecalTag(decalId,tag);
//		}
//
//		return decalId;
//
//
//	}
//
//	private void insertDecalTag(int decalId, String tag) {
//		try {
//			db.update(DaoSql.insertDecalTag,new Object[]{decalId,tag});
//			logger.debug("inserted decal tag "+tag);
//		} catch (Exception e){
//			logger.error(e,e);
//		}
//	}
//
//	private void insertTileTag(int tileId, String tag) {
//		try {
//			db.update(DaoSql.insertTileTag,new Object[]{tileId,tag});
//			logger.debug("inserted tile tag "+tag);
//		} catch (Exception e){
//			logger.error(e,e);
//		}
//	}
//
//
//	public List<Tile> getTileList() {
//		return (List<Tile>)db.query(
//				DaoSql.getAllTiles, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//
//						return processTilesResultSet(set);
//					}
//
//				});
//	}
//
//	private List<Tile> processTilesResultSet(ResultSet set)
//			throws SQLException {
//		List<Tile> tiles = new ArrayList<Tile>();
//
//		Tile tile = null;
//		while (set.next()){
//
//			if (tile==null || !set.getString("tile_id").equals(tile.getId())){
//				tile = new Tile(
//						set.getInt("height"),
//						set.getString("tile_id"),
//						BASE_IMAGE_PATH+set.getString("filepath"),
//						set.getInt("width")
//						);
//				tiles.add(tile);
//			}
//
//			tile.addTag(set.getString("tag"));
//		}
//		return tiles;
//	}
//
//	public List<Decal> getDecalList() {
//		return (List<Decal>)db.query(
//				DaoSql.getAllDecals, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//
//						return processDecalsResultSet(set);
//					}
//
//
//				});
//	}
//
//	private List<Decal> processDecalsResultSet(ResultSet set) throws SQLException {
//		List<Decal> decals = new ArrayList<Decal>();
//		Decal decal = null;
//		while (set.next()){
//
//			if (decal==null || !set.getString("decal_id").equals(decal.getId())){
//				decal = new Decal(
//						set.getInt("height"),
//						set.getString("decal_id"),
//						BASE_IMAGE_PATH+set.getString("filepath"),
//						set.getInt("width")
//						);
//				decals.add(decal);
//			}
//
//			decal.addTag(set.getString("tag"));
//		}
//		return decals;
//	}
//
//	public List<MapObject> getMapObjectsByTags(String t, final MapObjectType type) {
//		String sql;
//		switch (type){
//		case FIGURE:
//			sql = DaoSql.getFiguresByTag;
//			break;
//		case DECAL:
//			sql = DaoSql.getDecalsByTag;
//			break;
//		case TILE:
//			sql = DaoSql.getTilesByTag;
//			break;
//		default: 
//			return null;
//		}
//		String tags = "";
//		for (String tag : t.split(",")){
//			tag = tag.trim().toLowerCase();
//			if (tag.equals(""))continue;
//			tags+="'"+tag+"',";
//		}
//		if (tags.equals(""))return new ArrayList<MapObject>();
//		tags = tags.substring(0,tags.length()-1);
//		tags = "("+tags+")";
//		sql = sql.replace("?",tags);
//		return (List<MapObject>)db.query(
//				sql, 
//				new ResultSetExtractor() {
//
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//						switch (type){
//						case FIGURE:return processFiguresResultSet(set);
//						case DECAL:return processDecalsResultSet(set);
//						case TILE:return processTilesResultSet(set);
//
//						}
//						return null;
//					}
//				});
//	}
//
//	public Note getNote(int id) {
//		logger.debug("getting note "+id);
//		return (Note)db.query(
//				DaoSql.getNoteById, new Object[]{id}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						return new Note(
//								set.getInt("id"),
//								set.getString("note")
//								);
//					}
//
//				});
//	}
//
//
//
//	public void updateNote(final Note obj){ 
//		if (obj.getId()==null || obj.getId()<1){ 
//			insertNote(obj); 
//			return; 
//		} 
//		logger.debug("updating Note "+genToString(obj)); 
//		class Query extends UpdatableSqlQuery { 
//			public Query(DataSource ds, String sql) { 
//				super(ds, sql); 
//				declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//				compile(); 
//			} 
//			@Override 
//			protected Object updateRow(ResultSet rs, int rowNum, Map context) 
//					throws SQLException { 
//				if (obj.getId()!=null)rs.updateInt("id", obj.getId()); 
//				if (obj.getNote()!=null)rs.updateString("note", obj.getNote()); 
//				return null; 
//
//			}			 
//		} 
//
//
//		Query query = new Query(db.getDataSource(), DaoSql.getNoteById); 
//		query.compile(); 
//		query.execute(new Object[]{obj.getId()}); 
//
//	} 
//
//
//
//	public void insertNote(final Note obj){ 
//		if (obj.getId()!=null && obj.getId()>0){ 
//			updateNote(obj); 
//			return; 
//		} 
//		logger.debug("inserting Note "+genToString(obj));
//		tt.execute(new TransactionCallbackWithoutResult() {
//
//			@Override 
//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//				db.update( 
//						DaoSql.insertNote,  
//						new Object[]{ 
//								obj.getId(),
//								obj.getNote()
//						}, 
//						new int[]{ 
//								Types.INTEGER,
//								Types.VARCHAR,
//
//						} 
//
//						); 
//
//				obj.setId(getLastInsertId()); 
//			} 
//		});
//
//	}
//
//
//	//	@Deprecated
//	//	public GameMap saveMap(GameMap gameMap) {
//	//		//TODO this needs to be hardened
//	//		logger.info("saving map\n"+gameMap);
//	//		if (gameMap==null){
//	//			logger.error("game map is null");
//	//		}
//	//		updateGameMap(gameMap);
//	//
//	//		return gameMap;
//	//	}
//
//
//	public Map<Integer, String[]> getMapList(Integer userId) {
//		return (Map<Integer, String[]>)db.query(DaoSql.getMapList, new Object[]{userId}, new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet set) throws SQLException,
//			DataAccessException {
//				Map<Integer,String[]> ret = new LinkedHashMap<Integer, String[]>();
//				while (set.next()){
//					ret.put(
//							set.getInt("id"), 
//							new String[]{
//								set.getString("name"), 
//								set.getString("owner"), 
//								set.getString("created_by")
//							}
//							);
//				}
//				return ret;
//			}
//		});
//	}
//
//	public boolean passwordValid(String username, String hash) {
//
//		int c = db.queryForInt(DaoSql.checkPassword,new Object[]{username,hash});
//
//		return (c>0)?true:false;
//
//	}
//
//	public User getUser(final String ip, final String drupalSessionId) {
//		return (User)db.query(DaoSql.getuserByDrupalSession, new Object[]{drupalSessionId},new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet rs) throws SQLException,
//			DataAccessException {
//
//				if (rs.next()){
//					if (ip!=null && !ip.equals("127.0.0.1")){
//						String hostname = rs.getString("hostname");
//						if (!ip.equals(hostname)){
//							//							throw new RuntimeException(
//							//									"HAX! received login attempt from "+
//							//											ip+" but session is for "+hostname
//							//									);
//						}
//					}
//					return new User(
//							rs.getString("account_level"),
//							new Timestamp(rs.getLong("create_date")*1000),
//							rs.getString("email"),
//							rs.getInt("uid"),
//							rs.getString("password_hash"),
//							rs.getString("username")
//							);
//				}
//
//				return null;
//			}
//		});
//	}
//
//	public Map<String,String> getUserPreferences(int userId){
//
//		return (Map<String,String>)db.query(DaoSql.getUserPrefs, new Object[]{userId}, new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet rs) throws SQLException,
//			DataAccessException {
//				Map<String,String> ret = new HashMap<String, String>();
//
//				while (rs.next()){
//					ret.put(rs.getString("key"), rs.getString("value"));
//				}
//
//				return ret;
//			}
//		});
//	}
//
//	public void updateUserPrefs(int userId, Map<String,String> prefs){
//
//		db.update(DaoSql.deleteUserPrefs, new Object[]{userId});
//
//		String sql = DaoSql.insertUserPrefs;
//		List<String> params = new ArrayList<String>();
//
//		for (Map.Entry<String, String> e : prefs.entrySet()){
//			sql+="(?,?,?),";
//			params.add(""+userId);
//			params.add(e.getKey());
//			params.add(e.getValue());
//		}
//		sql = sql.substring(0,sql.length()-1);
//		System.out.println(sql);
//		System.out.println(params);
//
//		db.update(sql,params.toArray());
//
//
//	}
//
//	public List<GameBrief> getGameList() {
//
//		return (List<GameBrief>)db.query(DaoSql.getGameList, new RowMapper() {
//
//			@Override
//			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
//				return new GameBrief(rs.getString("description"),rs.getInt("id"),rs.getString("title"));
//			}
//		});
//
//	}
//
//	public User getUserByEmail(String i){
//
//		return (User)db.query(DaoSql.getUserByEmail, new Object[]{i}, new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet set) throws SQLException,
//			DataAccessException {
//
//				if (!set.next())return null;
//				return new User(
//						set.getString("account_level"),
//						new Timestamp(set.getLong("create_date")*1000),
//						set.getString("email"),
//						set.getInt("uid"),
//						set.getString("password_hash"),
//						set.getString("username")
//						);
//
//			}
//		});
//
//	}
//
//
//	public User getUserByUsername(String i){
//
//		return (User)db.query(DaoSql.getUserByUsername, new Object[]{i}, new ResultSetExtractor() {
//
//			@Override
//			public Object extractData(ResultSet set) throws SQLException,
//			DataAccessException {
//
//				if (!set.next())return null;
//				return new User(
//						set.getString("account_level"),
//						new Timestamp(set.getLong("create_date")*1000),
//						set.getString("email"),
//						set.getInt("uid"),
//						set.getString("password_hash"),
//						set.getString("username")
//						);
//
//			}
//		});
//
//	}
//
//
//	public static void main(String[] args) throws Exception {
//
//		DaoOld old = new DaoOld();
//		Dao dao = new Dao();
//
//		List<Decal> Decals = old.getDecalList();
//		for (Decal o : Decals){
//			dao.saveDecal(o);
//		}
//		
//		
//	}
//
//	public Invite getInvite(String code) {
//
//		return (Invite)db.query(
//				DaoSql.getInvite, new Object[]{code}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						return new Invite(
//								set.getInt("id"),
//								set.getString("email"),
//								set.getInt("game_id"),
//								set.getString("code")
//								);
//					}
//
//				});
//	}
//
//	public void deleteInvite(int id){
//		db.update(DaoSql.deleteInvite, new Object[]{id});
//	}
//
//	public Invite createInvite(String email, int gameId) {
//
//		final Invite invite = new Invite(email, gameId);
//
//		tt.execute(new TransactionCallbackWithoutResult() {
//
//			@Override 
//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//				db.update( 
//						DaoSql.insertInvite,  
//						new Object[]{ 
//								invite.getCode(),
//								invite.getEmail(),
//								invite.getGameId(),
//						}, 
//						new int[]{ 
//								Types.VARCHAR,
//								Types.VARCHAR,
//								Types.INTEGER,
//
//						} 
//
//						); 
//
//				invite.setId(getLastInsertId()); 
//			} 
//		});
//
//		return invite;
//
//	}
//
//	public void changeGamesMap(int gameId, int mapId) {
//		db.update(DaoSql.updateGamesMap, new Object[]{mapId, gameId});
//	}
//
//	public void updateMapName(Integer mapId, String mapName) {
//		db.update(DaoSql.updateMapName, new Object[]{mapName, mapId});
//	}
//
//	public CharacterSheet getCharacterSheet(int id) {
//
//		return (CharacterSheet)db.query(
//				DaoSql.getCharacterSheet, new Object[]{id}, new ResultSetExtractor(){
//
//					@Override
//					public Object extractData(ResultSet set)
//							throws SQLException, DataAccessException {
//
//						if (!set.next())return null;
//						try {
//							CharacterSheet cs = (CharacterSheet)Serializer.fromString(set.getString("character_serialized"));
//							//this is because the character id gets serialized as null on first insert
//							cs.setId(set.getInt("id"));
//							//so we can change these in the db and have it work correctly.
//							cs.setCharacterName(set.getString("character_name"));
//							cs.setGameId(set.getInt("game_id"));
//							cs.setOwnerId(set.getInt("owner_id"));
//							return cs;
//						} catch (IOException e) {
//							logger.error(e,e);
//							throw new RuntimeException(e);
//						} catch (ClassNotFoundException e) {
//							logger.error(e,e);
//							throw new RuntimeException(e);
//						}
//					}
//
//				});
//	}
//
//
//	public void updateCharacterSheet(final CharacterSheet cs){ 
//		try {
//			if (cs.getId()==null){ 
//				insertCharacterSheet(cs); 
//				return; 
//			} 
//			logger.debug("updating CharacterSheet"); 
//			final String serialized = Serializer.toString((CharacterSheetImpl)cs);
//			//			logger.debug(serialized);
//			logger.debug("serialized character length:"+serialized.length());
//			class Query extends UpdatableSqlQuery { 
//				public Query(DataSource ds, String sql) { 
//					super(ds, sql); 
//					declareParameter(new SqlParameter(1,Types.VARCHAR)); 
//					compile(); 
//				} 
//				@Override 
//				protected Object updateRow(ResultSet rs, int rowNum, Map context) 
//						throws SQLException { 
//					rs.updateString("character_serialized", serialized);
//					rs.updateString("character_name", cs.getCharacterName());
//					rs.updateInt("game_id", cs.getGameId()); 
//					rs.updateInt("owner_id", cs.getOwnerId());
//					return null; 
//
//				}			 
//			} 
//
//
//			Query query = new Query(db.getDataSource(), DaoSql.getCharacterSheet); 
//			query.compile(); 
//			query.execute(new Object[]{cs.getId()}); 
//
//		} catch (IOException e) {
//			logger.error(e,e);
//			throw new RuntimeException(e);
//		} 
//	} 
//
//	public void insertCharacterSheet(final CharacterSheet cs){ 
//		if (cs.getId()!=null){ 
//			updateCharacterSheet(cs); 
//			return; 
//		} 
//		tt.execute(new TransactionCallbackWithoutResult() {
//
//			@Override 
//			protected void doInTransactionWithoutResult(TransactionStatus status) {
//				try {
//					db.update( 
//							DaoSql.insertCharacterSheet,  
//							new Object[]{ 
//									cs.getCharacterName(),
//									Serializer.toString((CharacterSheetImpl)cs),
//									cs.getGameId(),
//									cs.getOwnerId(),
//							}, 
//							new int[]{ 
//									Types.VARCHAR,
//									Types.VARCHAR,
//									Types.INTEGER,
//									Types.INTEGER,
//
//							} 
//
//							);
//				} catch (DataAccessException e) {
//					logger.error(e,e);
//					throw new RuntimeException(e);
//				} catch (IOException e) {
//					logger.error(e,e);
//					throw new RuntimeException(e);
//				} 
//
//				cs.setId(getLastInsertId()); 
//			} 
//		});
//
//	}
//
//	public List<CharacterBrief> getCharacterList(Integer userId) {
//
//
//		return (List<CharacterBrief>)db.query(DaoSql.getCharacterList, new Object[]{userId}, new RowMapper<CharacterBrief>(){
//
//			@Override
//			public CharacterBrief mapRow(ResultSet set, int i)
//					throws SQLException {
//
//				try {
//					CharacterSheet cs = (CharacterSheetImpl)Serializer.fromString(set.getString("character_serialized"));
//					return new CharacterBrief(set.getInt("id"), cs.toString()); 
//
//				} catch (Exception e) {
//					logger.error(e, e);
//				}
//
//				return null;
//			}
//
//		}); 
//
//
//	}

}