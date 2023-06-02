package datadriventesting.sort;

import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.TestBase;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APISortTest extends TestBase {
	
	RequestSpecification httpRequest;
	RequestSpecification httpRequest1;
	Response response;
	
	@Test
	public void sortIdsTC() throws InterruptedException{
		
		logger.info(" -----------SortById Test Case-----------");
		String CVE= "CVE-2012-4782";
		String params= "cves/queries/cves";
		int offset=0;
		int limit=5;
		String sort = "cve.id%7Cdsc";
		
		try {
			//Request specification
	        httpRequest = RestAssured.given();
	        
	        response = sortById(params, offset, limit, sort);
	        
	        System.out.println(response.getBody().asString());
	        
	        
	        List<String> numberOfResources=response.jsonPath().getList("resources");
	        System.out.println(numberOfResources.size()); 
	        boolean isSort =isSortedDesc(numberOfResources);
	        
	        Assert.assertEquals(isSort, false);	
	        
	        String responseBody = response.getBody().asString();
			System.out.println(responseBody);
						
			int responseCode = response.getStatusCode();
			Assert.assertEquals(responseCode, 200);	
			
			String statusLine = response.getStatusLine();
			Assert.assertEquals(statusLine, "HTTP/1.0 200 OK");
			
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
	
	public boolean isSortedDesc(List<String> list) {
		for(int i=0;i<list.size()-1;i++) {
			if(list.get(i+1).compareToIgnoreCase(list.get(i)) > 0) {
				return false;
			}
		}
		return true;
	}
	
	public void filterById() throws InterruptedException{
		
		logger.info(" -----------filterById-----------");
		String CVE= "CVE-2012-4782";
		String params= "/cves/queries/cves/v1";
		
		try {
			        
	        response = filterById(params, CVE);
	        
	        System.out.println(response.getBody().asString());
	        
	        
	        List<String> numberOfResources=response.jsonPath().getList("resources.id");
	        System.out.println(numberOfResources.get(0)); 
	        
	        String responseBody = response.getBody().asString();
			System.out.println(responseBody);
			int resourceAffected=response.jsonPath().get("meta.writes.resources_affected");
			System.out.println(resourceAffected);
			Assert.assertEquals(resourceAffected, 1);	
			
			int responseCode = response.getStatusCode();
			Assert.assertEquals(responseCode, 200);	
			
			String statusLine = response.getStatusLine();
			Assert.assertEquals(statusLine, "HTTP/1.0 200 OK");
			
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
