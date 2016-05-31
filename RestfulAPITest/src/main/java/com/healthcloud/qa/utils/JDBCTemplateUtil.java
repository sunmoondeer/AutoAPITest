package com.healthcloud.qa.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class JDBCTemplateUtil {
	static String mysqlDriver = "com.mysql.jdbc.Driver";
	
	/**
	 * url: example-jdbc:mysql://localhost/contactdb
	 * @param url
	 * @param driver
	 * @param dbUser
	 * @param dbPasswd
	 * @return
	 */
	public static JdbcTemplate getMySQLJdbcTemplate(String url, String driver, String dbUser, String dbPasswd){
		JdbcTemplate mysqlJdbcTemplate = null;
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUsername(dbUser);
		dataSource.setPassword(dbPasswd);
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setMaxIdle(3);
		mysqlJdbcTemplate = new JdbcTemplate(dataSource);

		
		return mysqlJdbcTemplate;
	}

	/**
	 * url: example-jdbc:mysql://localhost/contactdb
	 * @param url
	 * @param driver
	 * @param dbUser
	 * @param dbPasswd
	 * @return
	 */
	public static JdbcTemplate getMySQLJdbcTemplate(String url, String dbUser, String dbPasswd){
		JdbcTemplate mysqlJdbcTemplate = null;
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUsername(dbUser);
		dataSource.setPassword(dbPasswd);
		dataSource.setUrl(url);
		dataSource.setDriverClassName(mysqlDriver);
		dataSource.setMaxIdle(3);
		mysqlJdbcTemplate = new JdbcTemplate(dataSource);

		
		return mysqlJdbcTemplate;
	}
	/*
	 * Get results columns name	
	 */
	public static String[] getColumnName(String sql, JdbcTemplate mysqlJdbcTemplate) {
		RowCountCallbackHandler rcch = new RowCountCallbackHandler();
		mysqlJdbcTemplate.query(sql,rcch);
		String[] str= rcch.getColumnNames();
		return str;
	}	
	
	public static String[] getRowsValue(String sql, JdbcTemplate mysqlJdbcTemplate) {
		RowCountCallbackHandler rcch = new RowCountCallbackHandler();
		mysqlJdbcTemplate.query(sql,rcch);
		String[] str= rcch.getColumnNames();
		Map result = mysqlJdbcTemplate.queryForMap(sql);
		for(int i = 0;i<str.length;i++){
			  System.out.println("Column name: " + str[i]);
			  str[i] = result.get(str[i]).toString();
	          System.out.println("Value: " + str[i]);
	       }
		return str;
	}	
	
	public static int[] getRowsType(String sql, JdbcTemplate mysqlJdbcTemplate) {
		RowCountCallbackHandler rcch = new RowCountCallbackHandler();
		mysqlJdbcTemplate.query(sql,rcch);
		int[] columnType = rcch.getColumnTypes();
		Map result = mysqlJdbcTemplate.queryForMap(sql);
		for(int i = 0;i<columnType.length;i++){
			  System.out.println("Column type: " + columnType[i]);
	       }
		return columnType;
	}	

	public static void main(String args[]) {
		String sql = "select name,parent_id from nodes_hierarchy where id = 47";
		String dbUrl = "jdbc:mysql://" + "10.100.141.25" + ":" + "3306" + "/" + "testlink";
		JdbcTemplate db = new JDBCTemplateUtil().getMySQLJdbcTemplate(dbUrl, "jman", "UIOWQ129jiqP");
		/*List<Map> result = db.query(sql, new RowMapper<Map>() {  

		      @Override  

		      public Map mapRow(ResultSet rs, int rowNum) throws SQLException {  

		          Map row = new HashMap();  

		          row.put(rs.getInt("parent_id"), rs.getString("name"));  

		          return row;  

		  }});*/
		
		getRowsValue(sql, db);
		getRowsType(sql, db);
	}
		
}
