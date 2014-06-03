package com.mut8ed.battlemap.server.dao.util;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class DaoUtil {

	private static DataSource ds = null;

	public static DataSource getDataSource(){

		if (ds==null){
			MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
			ds.setUser("battlemap");
			ds.setPassword("qwe123");
			ds.setServerName("mt-db");
			ds.setPort(3306);
			ds.setDatabaseName("mystictriad");
//			ds.setUrl("jdbc:mysql://localhost:3306");
			DaoUtil.ds = ds;
		}

		return ds;
	}
	
	public static void main(String[] args){

		JdbcTemplate template = new JdbcTemplate(getDataSource());
		System.out.println(template.queryForObject("select now()", String.class));
	
	}

	public static JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}
}
