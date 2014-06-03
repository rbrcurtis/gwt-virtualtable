package com.mut8ed.battlemap.server.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.mut8ed.battlemap.server.dao.util.DaoUtil;

/**
 * class that creates the fields for a new dto class and then creates the dao getter method
 * and echos them to the std out.  
 * @author csarbc
 *
 */
public class CreateDtoCode {

	public static final Logger logger = Logger.getLogger(CreateDtoCode.class);


	/**
	 * 
	 * @param dbname dbname of db connecting to
	 * @param dtoName name of the DTO class
	 * @param sql sql statement that will get all the desired fields. must return data.
	 */
	public static void echoBeanCode(String dtoName, String sql) {

		JdbcTemplate template = DaoUtil.getJdbcTemplate();
		SqlRowSet set = template.queryForRowSet(sql);
		SqlRowSetMetaData meta = set.getMetaData();
		StringBuilder getString = new StringBuilder();
		StringBuilder updateString = new StringBuilder(); 
		StringBuilder insertString = new StringBuilder();
		StringBuilder types = new StringBuilder(); 

		getString.append("public "+dtoName+" get"+dtoName+"(int id) {\n\n");
		getString.append("return ("+dtoName+")db.query(\n");
		getString.append("DaoSql.sql, new Object[]{id}, new ResultSetExtractor(){\n\n");

		getString.append("@Override\n");
		getString.append("public Object extractData(ResultSet set)\n");
		getString.append("throws SQLException, DataAccessException {\n\n");

		getString.append("if (!set.next())return null;\n");

		getString.append("return new "+dtoName+"(\n");

		updateString.append("public void update"+dtoName+"(final "+dtoName+" obj){ \n");

		updateString.append("logger.debug(\"updating "+dtoName+"\"); \n");

		updateString.append("if (obj.getId()==null){ \n");
		updateString.append("insert"+dtoName+"(obj); \n");
		updateString.append("return; \n");
		updateString.append("} \n");

		updateString.append("class Query extends UpdatableSqlQuery { \n");



		updateString.append("public Query(DataSource ds, String sql) { \n");
		updateString.append("super(ds, sql); \n");
		updateString.append("declareParameter(new SqlParameter(1,Types.VARCHAR)); \n");
		updateString.append("compile(); \n");
		updateString.append("} \n");

		updateString.append("@SuppressWarnings({ \"rawtypes\" })  \n");
		updateString.append("@Override \n");
		updateString.append("protected Object updateRow(ResultSet rs, int rowNum, Map context) \n");
		updateString.append("throws SQLException { \n");


		insertString.append("public void insert"+dtoName+"(final "+dtoName+" obj){ \n");
		insertString.append("if (obj.getId()!=null){ \n");
		insertString.append("update"+dtoName+"(obj); \n");
		insertString.append("return; \n");
		insertString.append("} \n");
		insertString.append("tt.execute(new TransactionCallbackWithoutResult() {\n\n");
		insertString.append("@Override \n");
		insertString.append("protected void doInTransactionWithoutResult(TransactionStatus status) {\n");

		insertString.append("db.update( \n");
		insertString.append("DaoSql.insert"+dtoName+",  \n");
		insertString.append("new Object[]{ \n");







		if (!set.next()){
			System.out.println("no data");
			return;
		}

		Map<String,String> fields = new TreeMap<String, String>(); 

		for (int i = 1;i<=meta.getColumnCount(); i++) {
			String field = meta.getColumnName(i);
			if (field.toLowerCase().equals("id"))continue;
			String className = meta.getColumnClassName(i);
			className = className.substring(className.lastIndexOf(".")+1);
			if (className.equals("BigDecimal"))className = "Integer";
			//put everything in a treemap then iterate over that
			//for output so that all the variables are alphabetical
			fields.put(field,className);
		}

		for (Map.Entry<String, String> entry : fields.entrySet()){
			String field = entry.getKey();
			String variable = field;
			variable = variable.toLowerCase();
			int index;
			while ((index = variable.indexOf('_'))>-1){
				variable = variable.substring(0,index)+
				variable.substring(index+1,index+2).toUpperCase()+
				variable.substring(index+2);
			}
			String className = entry.getValue();
			if (className.equals("Timestamp")){
				System.out.println("private Date "+variable+";");	
			} else {
				System.out.println("private "+className+" "+variable+";");
			}


			if (className.equals("String")){
				types.append("Types.VARCHAR,\n");
			} else {
				if (className.equals("Date"))className = "Timestamp";
				types.append("Types."+className.toUpperCase()+",\n");
			}

			if (className.equals("Integer"))className = "Int";
			String getter = "get"+variable.substring(0,1).toUpperCase()+variable.substring(1);
			if (className.equals("Timestamp")){
				getString.append("set.getDate(\""+field+"\"),\n");
			} else {
				getString.append("set.get"+className+"(\""+field+"\"),\n");
			}
			insertString.append("obj."+getter+"(),\n");
			if (className.equals("Timestamp")){
				updateString.append("if (obj."+getter+"()!=null)rs.update"+className+"(\""+field+"\", new Timestamp(obj."+getter+"().getTime())); \n");	
			} else {
				updateString.append("if (obj."+getter+"()!=null)rs.update"+className+"(\""+field+"\", obj."+getter+"()); \n");
			}
		}

		String sqlStatement = "insert into "+dtoName+" ("+fields.keySet()+")";
		sqlStatement = sqlStatement.replaceAll("[\\[\\]]", "");
		sqlStatement+=" values (";
		for (int i=0;i<fields.size();i++)sqlStatement+="?,";
		sqlStatement = sqlStatement.substring(0,sqlStatement.length()-1);
		sqlStatement+=")";


		getString.append(");\n");
		getString.append("}\n\n");
		getString.append("});\n");
		getString.append("}\n");

		updateString.append("return null; \n\n");
		updateString.append("}			 \n");
		updateString.append("} \n\n\n");
		updateString.append("Query query = new Query(db.getDataSource(), DaoSql.sql); \n");
		updateString.append("query.compile(); \n");
		updateString.append("query.execute(new Object[]{obj.getId()}); \n\n");
		updateString.append("} \n");


		insertString.append("}, \n");
		insertString.append("new int[]{ \n");
		insertString.append(types.toString()+" \n");
		insertString.append("} \n\n");
		insertString.append("); \n\n");
		insertString.append("obj.setId(getLastInsertId()); \n");
		insertString.append("} \n");
		insertString.append("});\n\n}\n");

		System.out.println("\n\n"+getString+"\n***************************\n\n");
		System.out.println("\n\n"+updateString+"\n***************************\n\n");
		System.out.println("\n\n"+insertString+"\n***************************\n\n");

		System.out.println(sqlStatement);


	}

	public static void main(String[] args){
		String sql = "select * from invite";
		echoBeanCode("Invite", sql);
	}

}


