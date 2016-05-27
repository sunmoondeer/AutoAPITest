package com.healthcloud.qa.utils;

import java.awt.Color;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.skyscreamer.jsonassert.JSONCompareResult;

public class DataWriter {

	
	public static void writeData(XSSFSheet sheet, String result, String iD, String test_case) {
		try {
			writeSheet(sheet.createRow(Integer.parseInt(iD)),iD,test_case,result);
		}catch (NumberFormatException e) {
		    e.printStackTrace();
		}
	}
	
	public static int writeComparisonData(XSSFSheet sheet, String result, String iD, String test_case, int num) {
		try {
			writeSheet(sheet.createRow(num),iD,test_case,result);
			num++;
		}catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		return num;
	}
	/*
	 * Summary:Test result write into resultSheet
	 * @param XSSFWorkbook: Excel instance
	 * @param XSSFSheet: sheet instance
	 * @param JSONCompareResult: json compare result
	 */
	public static void writeData(XSSFWorkbook wb,XSSFSheet resultSheet, JSONCompareResult result, String iD, String test_case) {
		String testresult = null;
		if(result.passed()) {
			testresult = "PASSED";
		} else {
			testresult = "FAILED";
		}
		
		try {
			if("PASSED".equals(testresult)) {
				writeSheet(resultSheet.createRow(Integer.parseInt(iD)),iD,test_case,testresult);
			} else {
				writeSheet(resultSheet.createRow(Integer.parseInt(iD)),iD,test_case,testresult);
				setCellResultFail(wb,resultSheet,iD);
			}
		}catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		
	}
	/*
	 * Summary: Set cell background color to red when test fail
	 */
	public static void setCellFail(XSSFWorkbook wb,XSSFSheet sheet,String iD) {
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		XSSFRow rowPf = sheet.getRow(Integer.parseInt(iD)); 
		XSSFCell cellPf = rowPf.getCell(1);
		System.out.println("cell content:" + cellPf.getStringCellValue());
		//cellPf.setCellValue("X39");
		cellPf.setCellStyle(style);
	}
	
	public static void setCellResultFail(XSSFWorkbook wb,XSSFSheet sheet,String iD) {
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		XSSFRow rowPf = sheet.getRow(Integer.parseInt(iD)); 
		XSSFCell cellPf = rowPf.getCell(2);
		System.out.println("cell content:" + cellPf.getStringCellValue());
		//cellPf.setCellValue("X39");
		cellPf.setCellStyle(style);
	}
	public static void writeSheet(XSSFRow row, String... data){
		for(int i=0;i<data.length;i++){
			row.createCell(i).setCellValue(data[i].toString());
		}
	}

/*	public static void writeData(XSSFSheet resultSheet, String string, String iD, String test_case, int i) {
		int lastNum = resultSheet.getLastRowNum();
		writeSheet(resultSheet.createRow(lastNum+1),string,iD,test_case);
	}*/
	

	public static void writeData(XSSFSheet comparsionSheet, String string, String iD, String iD2, String test_case) {
		int lastNum = comparsionSheet.getLastRowNum();
		writeSheet(comparsionSheet.createRow(lastNum+1),string,iD,iD2,test_case);
		
	}

	public static void writeData(XSSFSheet resultSheet, double totalcase, double failedcase, String startTime,
			String endTime) {
		
		writeSheet(resultSheet.createRow(resultSheet.getPhysicalNumberOfRows()),"用例总数: " + String.valueOf(totalcase),"失败的用例数: " + String.valueOf(failedcase),"开始时间: " + startTime,"结束时间: " + endTime);
	}

	

}
