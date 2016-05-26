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

	
	public static void writeData(XSSFSheet comparsionSheet, String result, String iD, String test_case) {
		int lastNum = comparsionSheet.getLastRowNum();
		//System.out.println("lastNum:"+lastNum);
		if(0 == lastNum){
			writeSheet(comparsionSheet.createRow(lastNum),"ID","TestCase","Response");
			//lastNum ++;
		}
		try {
			writeSheet(comparsionSheet.createRow(Integer.parseInt(iD)),iD,test_case,result);
			//writeSheet(comparsionSheet.createRow(lastNum),result,iD,test_case);原代码这块写的有问题，当iD传入为2时，
			//lastNum=1,创建的是第一行，把之前的第一行覆盖掉了
		}catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		
		/*if(0 == lastNum){
			writeSheet(comparsionSheet.createRow(lastNum),"comparsionDetail","ID","TestCase");
			lastNum ++;
		}
		writeSheet(comparsionSheet.createRow(lastNum),result,iD,test_case);*/
	}
	
	public static void writeData(XSSFWorkbook wb,XSSFSheet resultSheet, JSONCompareResult result, String iD, String test_case) {
		String testresult = null;
		if(result.passed()) {
			testresult = "PASSED";
		} else {
			testresult = "FAILED";
		}
		
		int lastNum = resultSheet.getLastRowNum();
		//System.out.println("lastNum:"+lastNum);
		if(0 == lastNum){
			writeSheet(resultSheet.createRow(lastNum),"ID","TestCase","Result");
			//lastNum ++;
		}
		try {
			if("PASSED".equals(testresult)) {
				writeSheet(resultSheet.createRow(Integer.parseInt(iD)),iD,test_case,testresult);
			} else {
				writeSheet(resultSheet.createRow(Integer.parseInt(iD)),iD,test_case,testresult);
				setCellResultFail(wb,resultSheet,iD);
			}
			
			//writeSheet(comparsionSheet.createRow(lastNum),result,iD,test_case);原代码这块写的有问题，当iD传入为2时，
			//lastNum=1,创建的是第一行，把之前的第一行覆盖掉了
		}catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		
	}
	public static void setCellFail(XSSFWorkbook wb,XSSFSheet comparsionSheet,String iD) {
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		XSSFRow rowPf = comparsionSheet.getRow(Integer.parseInt(iD)); 
		XSSFCell cellPf = rowPf.getCell(1);
		System.out.println("cell content:" + cellPf.getStringCellValue());
		//cellPf.setCellValue("X39");
		cellPf.setCellStyle(style);
	}
	
	public static void setCellResultFail(XSSFWorkbook wb,XSSFSheet comparsionSheet,String iD) {
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		XSSFRow rowPf = comparsionSheet.getRow(Integer.parseInt(iD)); 
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

	public static void writeData(XSSFSheet resultSheet, String string, String iD, String test_case, int i) {
		int lastNum = resultSheet.getLastRowNum();
		writeSheet(resultSheet.createRow(lastNum+1),string,iD,test_case);
	}

	public static void writeData(XSSFSheet comparsionSheet, String string, String iD, String iD2, String test_case) {
		int lastNum = comparsionSheet.getLastRowNum();
		writeSheet(comparsionSheet.createRow(lastNum+1),string,iD,iD2,test_case);
		
	}

	public static void writeData(XSSFSheet resultSheet, double totalcase, double failedcase, String startTime,
			String endTime) {
		
		writeSheet(resultSheet.createRow(resultSheet.getPhysicalNumberOfRows()),"用例总数: " + String.valueOf(totalcase),"失败的用例数: " + String.valueOf(failedcase),"开始时间: " + startTime,"结束时间: " + endTime);
	}

	

}
