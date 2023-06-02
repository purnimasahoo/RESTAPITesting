package datadriventesting;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.TestBase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.APIHelper;

public class APITest extends TestBase {
	
	@BeforeClass
	public void setUp() {
		logger.info(" -----------Starting APITest-----------");
	}
	
	@Test
	public void testAddAll() {
		
		RequestSpecification httpRequest;
		Response response = null;
		int totalWrite=0;
		try {
			logger.info(" -----------Starting DataDrivenTest_AddAll-----------");
			String params = "cves/entities/cves/v1";	
			JSONArray payload = APIHelper.parseJSONFile();
			
			System.out.println("PayLoad size " +payload.size());
			
			totalWrite=addAll(params, payload);	
		}
		catch(ParseException eParse) {
			logger.debug(eParse.toString());
		}
		catch(IOException eIO) {
			logger.debug(eIO.toString());
		}
		catch(InterruptedException eInterruption) {
			logger.debug(eInterruption.toString());
		}
		catch(Exception e) {
			logger.debug(e.toString());
			e.printStackTrace();
		}
		
		//Assert.assertEquals(totalWrite, 1043);
				
	}
	
	@Test
	public void testAddAllWithError() {
		
		RequestSpecification httpRequest;
		Response response = null;
		int totalWrite=0;
		try {
			logger.info(" -----------Starting DataDrivenTest_AddAll-----------");
			String params = "cves/entities/cves/v1";	
			JSONArray payload = APIHelper.parseJSONFile();
			
			
			response=addAllWithError(params, payload);	
			
			int responseCode = response.getStatusCode();
			Assert.assertEquals(responseCode, 400);
			
			String error=response.jsonPath().getString("errors.message");
			System.out.println(error);
			Assert.assertEquals(error, "[Number of elements given bigger than max accepted 500]");	
					
			long responseTime = response.getTime(); //getting Response Time
			
			logger.info(" Response time is ---->" +responseTime);
			
			if(responseTime> 2000) {
				logger.warn("Response Time is greater than 2000");
			}
			
			Assert.assertTrue(responseTime<2000);
		}
		catch(ParseException eParse) {
			logger.debug(eParse.toString());
		}
		catch(IOException eIO) {
			logger.debug(eIO.toString());
		}
		catch(InterruptedException eInterruption) {
			logger.debug(eInterruption.toString());
		}
		catch(Exception e) {
			logger.debug(e.toString());
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testAddJSONObjAsInput() {
		
		RequestSpecification httpRequest;
		Response response = null;
		try {
			logger.info(" -----------Starting DataDrivenTest_AddAll-----------");
			String params = "cves/entities/cves/v1";	
			JSONArray payload = APIHelper.parseOneJSONFile();		
			
			response=addJSONObjAsInput(params, payload);	
			
			int responseCode = response.getStatusCode();
			Assert.assertEquals(responseCode, 500);	
			
			String error=response.jsonPath().getString("message");
			System.out.println(error);
			Assert.assertEquals(error, "Internal Server Error");
			
			long responseTime = response.getTime(); //getting Response Time
			
			logger.info(" Response time is ---->" +responseTime);
			
			if(responseTime> 2000) {
				logger.warn("Response Time is greater than 2000");
			}
			
			Assert.assertTrue(responseTime<2000);
		}
		catch(ParseException eParse) {
			logger.debug(eParse.toString());
		}
		catch(IOException eIO) {
			logger.debug(eIO.toString());
		}
		catch(InterruptedException eInterruption) {
			logger.debug(eInterruption.toString());
		}
		catch(Exception e) {
			logger.debug(e.toString());
			e.printStackTrace();
		}		
	}
	
	@AfterClass
	public void tearDown() throws Exception{
				
		logger.info("------------------Finished APITest----------------------");
		
	}
}
