package com.healthcloud.qa.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
		String s = df.format(new Date());
		return s;
	}
	public static String getShortCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("ddHHmmss");
		String s = df.format(new Date());
		return s;
	}
	public static void main(String[] args) {
		System.out.println(TimeUtil.getCurrentTime());
	}
}
