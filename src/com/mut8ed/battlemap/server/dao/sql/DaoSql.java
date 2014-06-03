package com.mut8ed.battlemap.server.dao.sql;


public class DaoSql {

	//	public static final String checkForDuplicateImage = "select * from image where md5 = ?";
	//
	public static final String checkPassword = "select count(1) from user where username = ? and password_hash = ?";
	//
	//	public static final String clearMapLayers = "delete from map_layer where map_id = ?";
	//
	//	public static final String deleteAllDecalTags = "delete from decal_tag where decal_id = ?";
	//
	//	public static final String deleteAllTileTags = "delete from tile_tag where tile_id = ?";
	//
	//	public static final String deleteImage = "delete from image where id = ?";
	//
	//	public static final String deleteMapObject = "delete from map_object where id = ?";
	//
	public static final String deleteUserPrefs = "delete from user_pref where user_id = ?";
	//
	//	public static final String getAllDecals = "select *, decal.id decal_id, image.id image_id from decal, image, decal_tag tag where image.id = decal.image_id and decal.id = tag.decal_id";
	//
	//	public static final String getAllTiles = 
	//		"select *, tile.id tile_id, image.id image_id from tile, image, tile_tag tag where image.id = tile.image_id and tile.id = tag.tile_id order by tile.id";
	//
	//	public static final String getDecalById = "select *, decal.id as decal_id, image.id as image_id from decal, image where image.id = decal.image_id and decal.id = ?";
	//
	//	public static final String getDecalOnlyById = "select * from decal where id = ?";
	//
	//	public static final String getDecalsByTag = 
	//		"select decal.id, height, width, image.id image_id, filepath, md5, tag \n" +
	//		"from decal, image, decal_tag tag \n" +
	//		"where image.id = decal.image_id \n" +
	//		"and decal.id = tag.decal_id \n" +
	//		"and decal.id in (select * from (select distinct decal.id \n" +
	//		"from decal, decal_tag tag where decal.id = tag.decal_id and tag in ?) alias) \n" +
	//		"order by decal.id, tag \n";
	//
	//	public static final String getFigureById = 
	//		"select *, f.id as figure_id, i.id as image_id, fs.id as stance_id, ft.id as tag_id from figure f, figure_stance fs, figure_tag ft, image i where 1=1 and f.id=fs.figure_id and f.id = ft.figure_id and fs.image_id = i.id and f.id = ? order by f.id";
	//
	//	public static final String getFiguresByTag = 
	//		"select f.id, f.name, stance, i.id image_id, filepath, md5, ft.tag  \n" +
	//		"from figure f, figure_stance fs, figure_tag ft, image i  \n" +
	//		"where 1=1  \n" +
	//		"and f.id=fs.figure_id  \n" +
	//		"and f.id = ft.figure_id  \n" +
	//		"and fs.image_id = i.id  \n" +
	//		"and f.id in (select * from (select distinct f.id  \n" +
	//		"from figure f, figure_tag tag where f.id = tag.figure_id and tag in ?) alias) \n" +
	//		"order by f.id, stance, tag\n";
	//
	//	public static final String getFigureStancesByFigureId = "select * from figure_stance where figure_id = ?";
	//
	//	public static final String getFigureTagsByFigureId = "select * from figure_tag where figure_id = ?";
	//
	//	public static final String getGame = 
	//		"select * from game where id = ?";
	//
	//	public static final String getGameList = "select * from game";
	//
	//	public static final String getGameMap = 
	//		"select * from game_map where id = ?";
	//
	//	public static final String getImagePath = "select filepath from image where id = ?";
	//
	//	public static final String getMapLayers = "select * from map_layer where map_id = ?";
	//
	//	public static final String getMapList = "select gm.*, if(u.name<>'',u.name,'public') " +
	//			"owner from game_map gm left outer join d_users u on u.uid = gm.owner_id " +
	//			"where gm.owner_id in (0,?) order by owner_id desc";
	//
	//	public static final String getMapObject = 
	//		"select * from map_object where id = ?";
	//
	//	public static final String getMapObjectsByMapId = "select * from map_object where map_id = ?";
	//
	//	public static final String getNoteById = "select * from note where id = ?";
	//
	//	public static final String getPlayersByGameId = 
	//		"select * from game_player where game_id = ?";
	//
	//	public static final String getSomeRandomFigures = 
	//		"select *, i.id as image_id, fs.id as stance_id, ft.id as tag_id from figure f,  \n" +
	//		"figure_stance fs, figure_tag ft, image i \n" +
	//		"where 1=1 \n" +
	//		"and f.id=fs.figure_id  \n" +
	//		"and f.id = ft.figure_id  \n" +
	//		"and fs.image_id = i.id \n" +
	//		"and f.id in (select * from (select id from figure order by rand() limit 20) alias) \n" +
	//		"order by f.id \n";
	//
	//	public static final String getTileById = "select *, tile.id as tile_id, image.id as image_id from tile, image where image.id = tile.image_id and tile.id = ?";
	//
	//	public static final String getTileOnlyById = "select * from tile where id = ?";
	//
	//	//	public static final String checkIfMapObjectExists = "select count(1) from map_object where map_id = ? and counter_id = ?";
	//
	//	public static final String getTilesByTag = 
	//		"select tile.id, height, width, image.id image_id, filepath, tag \n" +
	//		"from tile, image, tile_tag tag  \n" +
	//		"where image.id = tile.image_id and tile.id = tag.tile_id  \n" +
	//		"and tile.id in (select * from (select distinct tile.id \n" +
	//		"from tile, tile_tag tag where tile.id = tag.tile_id and tag in ?) alias) \n" +
	//		"order by tile.id, tag \n";
	//
	public static final String getuserByDrupalSession = 
			"select r.name account_level, u.uid,u.name username,u.created create_date,u.mail email,u.pass password_hash, s.hostname \n" +
					"from d_users u \n" +
					"left outer join mystictriad.d_users_roles ur on ur.uid = u.uid \n" +
					"left outer join mystictriad.d_role r on r.rid = ur.rid \n" +
					"join d_sessions s on u.uid = s.uid \n" +
					"where u.name is not null and u.name<>'' \n" +
					"and s.sid = ?\n";

	public static final String getUserByEmail =
			"select r.name account_level, u.uid,u.name username,u.created create_date,u.mail email,u.pass password_hash \n" +
					"from d_users u \n" +
					"left outer join mystictriad.d_users_roles ur on ur.uid = u.uid \n" +
					"left outer join mystictriad.d_role r on r.rid = ur.rid \n" +
					"where u.name is not null and u.name<>'' and u.mail = ? \n";

	public static final String getUserById = 
			"select r.name account_level, u.uid,u.name username,u.created create_date,u.mail email,u.pass password_hash \n" +
					"from d_users u \n" +
					"left outer join mystictriad.d_users_roles ur on ur.uid = u.uid \n" +
					"left outer join mystictriad.d_role r on r.rid = ur.rid \n" +
					"where u.name is not null and u.name<>'' and u.uid = ? \n";

	public static final String getUserByUsername = 
			"select r.name account_level, u.uid,u.name username,u.created create_date,u.mail email,u.pass password_hash \n" +
					"from d_users u \n" +
					"left outer join mystictriad.d_users_roles ur on ur.uid = u.uid \n" +
					"left outer join mystictriad.d_role r on r.rid = ur.rid \n" +
					"where u.name is not null and u.name<>'' and u.name = ? \n";

	public static String getUsersByGroup = 
			"select r.name account_level, u.uid,u.name username, u.created create_date,u.mail email,u.pass password_hash \n" +
					"from d_users u \n" +
					"left outer join mystictriad.d_users_roles ur on ur.uid = u.uid \n" +
					"left outer join mystictriad.d_role r on r.rid = ur.rid \n" +
					"join d_og_uid ou on u.uid = ou.uid \n" +
					"where u.name is not null and ou.nid = ? \n";
	
	public static String getGameList =
			"select n.nid, n.title, o.og_description description \n" + 
			"from d_og o, d_node n, d_og_uid ou \n" +
			"where 1=1 \n" +
			"and o.nid = n.nid \n" +
			"and ou.nid = n.nid \n" +
			"and ou.uid = ?";
			
	public static final String getUserPrefs = "select * from user_pref where user_id = ?";

	public static final String insertUserPrefs = "insert into user_pref (`user_id`,`key`,`value`) values ";

	public static final String getGame = 
			"select n.title, o.og_description description, n.uid dm from d_og o, d_node n " +
					"where o.nid = n.nid and n.nid = ?";

}
