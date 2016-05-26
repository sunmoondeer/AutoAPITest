package com.healthcloud.qa.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/*
 * 解析properties文件，传入key,返回value
 */

public class ParseProperties {
	public static String getValue(String keyname,String propertiespath) {
		Properties pro = new Properties();
		try {
			
			InputStream in = new FileInputStream(propertiespath);
			InputStreamReader inr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inr);
			pro.load(br);
			in.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		 return pro.getProperty(keyname).trim();
	}
	
/*	public static void main(String[] args) {
		String filepath = System.getProperty("user.dir")+"\\data" + "\\server.properties";
		ParseProperties p = new ParseProperties();
		String ss = p.getValue("fingertip.server.host", filepath);
		System.out.println(ss);
		
	}*/

}
