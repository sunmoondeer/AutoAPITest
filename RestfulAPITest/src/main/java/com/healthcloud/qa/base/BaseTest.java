package com.healthcloud.qa.base;

import java.io.File;
import java.util.Hashtable;

import javax.security.auth.login.LoginException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;


public class BaseTest {
    @Rule 
    public TestName tn = new TestName(); // must be public

	@BeforeClass
	public void staticBasicPrepare() throws Exception{
        System.out.println("+++++++++++++++++++++++" +  this.getClass().getName() + ": Start" + "+++++++++++++++++++++++");
	}
	  
    @AfterClass
    public void staticBasicUnPrepare(){
    	System.out.println("+++++++++++++++++++++++" +  this.getClass().getName() + ": End" + "+++++++++++++++++++++++");
    }

    @Before
    public void setUpBasic() throws Exception{
        System.out.println("+++++++++++++++++++++++ " + tn.getMethodName() + ": Start" + "+++++++++++++++++++++++ ");
    }
	
    @After
    public void tearDownBasic() {
    	System.out.println("+++++++++++++++++++++++ " + tn.getMethodName() + ": End" + "+++++++++++++++++++++++ ");
    }

    public static void printBlock(String msg){
    	System.out.println();
    	System.out.println(msg);
    }

    public static void println(String msg){
    	System.out.println(msg);
    }
}
