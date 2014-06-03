package com.mut8ed.battlemap.server.dao.document;

import com.mut8ed.battlemap.server.dao.Dao;

public class MongoMigration {

	public String conversion;
	public String timestamp;
	
	public MongoMigration(String conversion){
		this.conversion = conversion;
		this.timestamp = Dao.getTimeString();
	}
	
}
