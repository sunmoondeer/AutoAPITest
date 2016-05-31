package com.healthcloud.qa.test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.healthcloud.qa.base.BaseTest;
import com.healthcloud.qa.utils.HTTPReqGen;
import com.healthcloud.qa.utils.JDBCTemplateUtil;
import com.healthcloud.qa.utils.ParseProperties;
import com.healthcloud.qa.utils.XMLUtils;
import com.jayway.restassured.response.Response;

public class DBTest extends BaseTest{
	private static JdbcTemplate jdbcTemplate = null;
	private static String dbUrl = null;
	private static String template = null;
	private static String templatePath = null;
	private static Response response = null;
	
	@BeforeClass
	public static void staticPrepare() throws Exception {
		ParseProperties file = new ParseProperties();
		String filepath = System.getProperty("user.dir") + File.separator + "server.properties";//服务器配置文件
		String dbHostName = file.getValue("b2bsettle.db.host", filepath);
	    String dbPort = file.getValue("b2bsettle.db.port", filepath);
	    String dbName = file.getValue("b2bsettle.db.database", filepath);
	    String dbUser = file.getValue("b2bsettle.db.user", filepath);
	    String dbPasswd = file.getValue("b2bsettle.db.password", filepath);
	    String dbUrl = "jdbc:mysql://" + dbHostName + ":" + dbPort + "/" + dbName;
	    dbUrl = "jdbc:mysql://" + dbHostName + ":" + dbPort + "/" + dbName;
	    jdbcTemplate = JDBCTemplateUtil.getMySQLJdbcTemplate(dbUrl, dbUser, dbPasswd);
	    templatePath =  System.getProperty("user.dir") + File.separator + "http_request_template.txt";
	}
	
	 @AfterClass
	    public static void staticUnPrepare(){
	    }
	 
	 @Test
	    public void testLoginSuccess() throws Exception {
		 printBlock("Step1. load test data");
		 HashMap<String, String> loginDataMap = new XMLUtils().getXMLNodeValues(System.getProperty("user.dir") + File.separator + "testdata" + File.separator + "login.xml", "test_001");
		 HTTPReqGen myReq = new HTTPReqGen();
		 try {    	   
	            FileInputStream fis = new FileInputStream(new File(templatePath));//创建到http_request_template.txt的输入流
	     	   template = IOUtils.toString(fis, Charset.defaultCharset());
	     } catch (Exception e) {
	         Assert.fail("Problem fetching data from input file:" + e.getMessage());
	     }
		 
		 try {
			 printBlock("Step2. send http request");	
			 myReq.generate_request(template, loginDataMap);
			 response = myReq.perform_request();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 printBlock("Step3. print response");
		 System.out.println(response.asString());
		 printBlock("Step4. response change to jsonobject");
		 JSONObject resJson = JSONObject.fromObject(response.asString());
		 printBlock("Step4. execute sql query");
		 String[] columnName = JDBCTemplateUtil.getColumnName(loginDataMap.get("sql"), jdbcTemplate);
		 String[] rowValue = JDBCTemplateUtil.getRowsValue(loginDataMap.get("sql"), jdbcTemplate);
		 printBlock("Step5. compare response with db");
		 if(resJson.get("retCode").equals(rowValue[0]) && resJson.get("sendTime").equals(rowValue[1])) {
			 Assert.assertTrue(true);
		 } else {
			 System.out.println("retCode:" + resJson.get("retCode"));
			 System.out.println("DBretCode:" + rowValue[0]);
			 System.out.println("sendTime:" + resJson.get("sendTime"));
			 System.out.println("DBsendTime:" + rowValue[1]);
			 Assert.assertTrue(false);
		 }
	 }
}
