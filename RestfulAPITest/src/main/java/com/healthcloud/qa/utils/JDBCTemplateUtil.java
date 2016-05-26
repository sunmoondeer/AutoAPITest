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
		
		


}
