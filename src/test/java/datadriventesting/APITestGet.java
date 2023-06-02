package datadriventesting;

import java.io.IOException;
import java.util.List;

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

public class APITestGet extends TestBase {
	
	@BeforeClass
	public void setUp() {
		logger.info(" -----------Starting APITestGet-----------");
		RequestSpecification httpRequest;
		Response response;
	}
	
	@Test
	public void getOneId() {
		
		logger.info(" -----------Checking data by using GET Method-----------");
		String params="cves/entities/cves";
		String CVE= "CVE-2011-3970";
		try {
			Response responseGet = getOneId(params, CVE);
				
    	
	        System.out.println(responseGet.getBody().asString());
	    	//Asserting that the status code is 200 for the request
	        Assert.assertEquals(responseGet.getStatusCode(), 200);
	        Assert.assertEquals(responseGet.getStatusLine(), "HTTP/1.0 200 OK");
	        List<String> numberOfResources=responseGet.jsonPath().getList("resources.id");
	        Assert.assertEquals("CVE-2011-3970", numberOfResources.get(0)) ;
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
	public void testGetAll() {
		
		RequestSpecification httpRequest;
		Response response = null;
		try {
			logger.info(" -----------Starting DataDrivenTest_AddAll-----------");
			String params = "/cves/queries/cves";	
			JSONArray payload = APIHelper.parseJSONFile();
			int total =payload.size();
			
			System.out.println("Payload Size " +total);
			int pagination= APIHelper.readPagination();
			
			response=getAll(params, pagination, total);	
			
			int responseCode = response.getStatusCode();
			Assert.assertEquals(responseCode, 200);	
			
			String statusLine = response.getStatusLine();
			Assert.assertEquals(statusLine, "HTTP/1.0 200 OK");
						
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
	public void getInvalidId() {
		
	}
	
	@Test
	public void getMax600() {
		
	}
	
	@AfterClass
	public void tearDown() throws Exception{
				
		logger.info("------------------Finished APITestGet----------------------");
		
	}

}
