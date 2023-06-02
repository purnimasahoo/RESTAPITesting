package datadriventesting;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.TestBase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.APIHelper;

public class APITestDelete extends TestBase {
	
	@BeforeClass
	public void setUp() {
		logger.info(" -----------Starting APITestDelete-----------");
		RequestSpecification httpRequest;
		Response response;
	}
	
	@Test
	public void deleteOneId() {
		
		logger.info(" -----------Checking delete one data Method-----------");
		String params="/cves/entities/cves";
		String CVE= "CVE-2012-4782";
		try {
			Response response = deleteOneId(params, CVE);
				
    	
	        System.out.println(response.getBody().asString());
	        
	        int resourceAffected=response.jsonPath().get("meta.writes.resources_affected");
			System.out.println(resourceAffected);
			Assert.assertEquals(resourceAffected, 1);		
	    	//Asserting that the status code is 200 for the request
	        Assert.assertEquals(response.getStatusCode(), 200);
	        Assert.assertEquals(response.getStatusLine(), "HTTP/1.0 200 OK");
	        
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
	public void deleteInvalidId() {
	
	}
	
	@Test
	public void deleteMoreThan500Ids() {
	
	}
	
	// delete the first 5 cves returned by the API and ensure the operation	completed successfully.
	@Test
	public void delete5Ids() {
		logger.info(" -----------Checking delete5Ids Method-----------");
		try {
			String paramsGet="/cves/queries/cves";
			String paramsDelete="/cves/entities/cves/";
			int offset=0;
			int limit = 5;
			Response response = delete5Ids(paramsGet, paramsDelete, offset, limit);
				
    	
	        System.out.println(response.getBody().asString());
	        
	        int resourceAffected=response.jsonPath().get("meta.writes.resources_affected");
			System.out.println(resourceAffected);
			Assert.assertEquals(resourceAffected, 5);		
	    	//Asserting that the status code is 200 for the request
	        Assert.assertEquals(response.getStatusCode(), 200);
	        Assert.assertEquals(response.getStatusLine(), "HTTP/1.0 200 OK");
	        
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
	public void deleteAll() {
		
		logger.info(" -----------Checking delete5Ids Method-----------");
		try {
			String paramsGet="cves/queries/cves";
			String paramsDelete="cves/entities/cves";
			int offset=0;
			int limit = 4;
			JSONArray payload = APIHelper.parseJSONFile();
			int total =payload.size();
			int pagination= APIHelper.readPagination();
			
			Response response = deleteAll(paramsGet, paramsDelete, pagination, offset, limit, total);
				
    	
	        System.out.println(response.getBody().asString());
	        
	       
			Assert.assertEquals(totalDelete, 1497);		
	    	//Asserting that the status code is 200 for the request
	        Assert.assertEquals(response.getStatusCode(), 200);
	        Assert.assertEquals(response.getStatusLine(), "HTTP/1.0 200 OK");
	        
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

}
