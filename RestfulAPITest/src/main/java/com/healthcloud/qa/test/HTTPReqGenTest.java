package com.healthcloud.qa.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.healthcloud.qa.utils.DataReader;
import com.healthcloud.qa.utils.DataWriter;
import com.healthcloud.qa.utils.FileOperationsUtil;
import com.healthcloud.qa.utils.HTTPReqGen;
import com.healthcloud.qa.utils.RecordHandler;
import com.healthcloud.qa.utils.SheetUtils;
import com.healthcloud.qa.utils.StringUtil;
import com.jayway.restassured.response.Response;

public class HTTPReqGenTest implements ITest {

    private Response response;
    private DataReader myInputData;
    private DataReader myBaselineData;
    private String template;
    private int num = 1;

    public String getTestName() {
        return "API Test";
    }
    
   private String userDir = System.getProperty("user.dir");
   String filePath = "";
   String templatePath =  userDir + File.separator + "http_request_template.txt";
   String reportPath = null;
    
    XSSFWorkbook wb = null;
    XSSFSheet inputSheet = null;
    XSSFSheet baselineSheet = null;
    XSSFSheet outputSheet = null;
    XSSFSheet comparsionSheet = null;
    XSSFSheet resultSheet = null;
    
    private double totalcase = 0;
    private double failedcase = 0;
    private String startTime = "";
    private String endTime = "";

    
    @BeforeTest
    @Parameters("workBook")//使用testng.xml中的parameter选项作为参数传递给setup方法，这里传递的是Http_Request_workbook_Data.xlsx
    public void setup(String fileName) {
        reportPath = FileOperationsUtil.copyFile(fileName);
     
        try {
        	//创建excel文件实例
            wb = new XSSFWorkbook(new FileInputStream(reportPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputSheet = wb.getSheet("Input");//创建Input sheet实例
        baselineSheet = wb.getSheet("Baseline");//创建baseline sheet实例
        outputSheet = wb.getSheet("Output");
        comparsionSheet = wb.getSheet("Comparison");
        resultSheet = wb.getSheet("Result");
        

        try {    	   
               FileInputStream fis = new FileInputStream(new File(templatePath));//创建到http_request_template.txt的输入流
        	   template = IOUtils.toString(fis, Charset.defaultCharset());
        } catch (Exception e) {
            Assert.fail("Problem fetching data from input file:" + e.getMessage());
        }
        
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = sf.format(new Date());
    }

    @DataProvider(name = "WorkBookData")
    protected Iterator<Object[]> testProvider(ITestContext context) {

        List<Object[]> test_IDs = new ArrayList<Object[]>();

            myInputData = new DataReader(inputSheet, true, true, 0);

            // sort map in order so that test cases ran in a fixed order
            Map<String, RecordHandler> sortmap = new TreeMap<String,RecordHandler>(new Comparator<String>(){

				public int compare(String key1, String key2) {
					return key1.compareTo(key2);
				}
            	
            });
            
            sortmap.putAll(myInputData.get_map());
           
            
            for (Map.Entry<String, RecordHandler> entry : sortmap.entrySet()) {
                String test_ID = entry.getKey();
                String test_case = entry.getValue().get("TestCase");
                if (!test_ID.equals("") && !test_case.equals("")) {
                    test_IDs.add(new Object[] { test_ID, test_case });
                }
                totalcase++;
            }
            
            myBaselineData = new DataReader(baselineSheet, true, true, 0);

        return test_IDs.iterator();
    }

    @Test(dataProvider = "WorkBookData", description = "ReqGenTest")
    public void api_test(String ID, String test_case) {

        HTTPReqGen myReqGen = new HTTPReqGen();

        try {
            myReqGen.generate_request(template, myInputData.get_record(ID));
            response = myReqGen.perform_request();
        } catch (Exception e) {
            Assert.fail("Problem using HTTPRequestGenerator to generate response: " + e.getMessage());
        }
        
        String baseline_message = myBaselineData.get_record(ID).get("Response");
        System.out.println("Reponse code: " + response.statusCode());

        if (response.statusCode() == 200)
            try {
            	//Write response json to outputSheet
                DataWriter.writeData(outputSheet, response.asString(), ID, test_case);
            	//System.out.println(outputSheet.getSheetName() + "\t\t"+response.asString() + "\t\t" + ID+"\t\t"+test_case); 
            	//Get the json compare result
                JSONCompareResult result = JSONCompare.compareJSON(StringUtil.removeSpaces(baseline_message), StringUtil.removeSpaces(response.asString()), JSONCompareMode.NON_EXTENSIBLE);
                //Write json compare result into resultSheet
                DataWriter.writeData(wb,resultSheet, result, ID, test_case);
                
                if (!result.passed()) {
                	//Set failed testcase in outputSheet background to Red
                	DataWriter.setCellFail(wb,outputSheet, ID);
                	//Get json compare message and write into comparsionSheet
                	//DataWriter.writeComparisonData(comparsionSheet, result, iD, test_case, num)
                    num = DataWriter.writeComparisonData(comparsionSheet, result.getMessage(), ID, test_case, num);
                    //System.out.println(comparsionSheet.getSheetName() + "\t\t"+result + "\t\t" + ID+"\t\t"+test_case);
                    //DataWriter.writeData(resultSheet, "false", ID, test_case, 0);
                   // System.out.println(resultSheet.getSheetName() + "\t\tfalse\t\t" + ID+"\t\t"+test_case +"\t\t0");
                  //  DataWriter.writeData(outputSheet);
                    failedcase++;
                    Assert.fail(result.getMessage());
                } 
            } catch (JSONException e) {
                DataWriter.writeData(comparsionSheet, "", "Problem to assert Response and baseline messages: "+e.getMessage(), ID, test_case);
                DataWriter.writeData(resultSheet, "error", ID, test_case, 0);
            	 // System.out.println(comparsionSheet.getSheetName() + "\t\tProblem to assert Response and baseline messages: "+e.getMessage()+ "\t\t"+ ID +"\t\t"+ test_case);
                
                
                failedcase++;
                Assert.fail("Problem to assert Response and baseline messages: " + e.getMessage());
            }
        else {
           DataWriter.writeData(outputSheet, response.statusLine(), ID, test_case);
           //  System.out.println(outputSheet.getSheetName() + "\t\t" +response.statusLine()+ "\t\t" + ID+"\t\t" + test_case );

            if (baseline_message.equals(response.statusLine())) {
             DataWriter.writeData(resultSheet, "true", ID, test_case, 0);
             //  	System.out.println(resultSheet.getSheetName() + "\t\ttrue"+"\t\t"+ ID+"\t\t"+test_case + "\t\t0");
            } else {
                DataWriter.writeData(comparsionSheet, baseline_message, response.statusLine(), ID, test_case);
                DataWriter.writeData(resultSheet, "false", ID, test_case, 0);
                // System.out.println(resultSheet.getSheetName() + "\t\tfalse\t\t"+ ID + "\t\t0" );
//             DataWriter.writeData(outputSheet);
                failedcase++;
                Assert.assertFalse(true);
            }
        }
    }


    @AfterTest
    public void teardown() {
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        endTime = sf.format(new Date());
        DataWriter.writeData(resultSheet, totalcase, failedcase, startTime, endTime);
        //   System.out.println(resultSheet.getSheetName() + "\t\t"+ totalcase + "\t\t" + failedcase + "\t\t" + startTime + "\t\t" + endTime);
        
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(reportPath);
            wb.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}