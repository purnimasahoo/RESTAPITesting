package base;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.APIHelper;

public class TestBase {
	
	private static String localHost;

	private static int totalWrites=0;
	
	public static int totalGet=0;
	
	public static int totalDelete=0;
	
	public static Logger logger = LogManager.getLogger(TestBase.class);
	
	//Read headers from a file
	
	@BeforeClass
	public void setup() throws IOException {
		
		logger.debug(Level.DEBUG);
		logger.info(" -----------SetUp Call-----------");
		
		//"https://jsonmock.hackerrank.com/api/movies/search/?Title=Superman"
		
		localHost= APIHelper.readURL();
		logger.info(" -----------localhost-----------" + localHost);
		
	}
	
	public static Response getMovieTitles(String params, int pageNumber) 
			throws InterruptedException, IOException {
		
		logger.info(" -----------Checking data by using GET Method-----------");
		
		RequestSpecification httpRequest1;
		Response response;
		RestAssured.baseURI = localHost;
		
		System.out.println(" Get Method " +localHost);
		
        //Request specification
        httpRequest1 = RestAssured.given().given().header("Content-Type", "application/json");
		
        response =  httpRequest1.request(Method.GET, "/?Title=" +params+"&page="+pageNumber);		
		return response;
	}
	
	public static Response getAll(String params, int pagination, int total)
			throws InterruptedException, IOException {
		
		RequestSpecification httpRequest1;
		Response response;
		RestAssured.baseURI = localHost+params;
		
		System.out.println(" Get Method " +localHost+params);
		              
        //Request specification
        httpRequest1 = RestAssured.given().given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient());
        
        response =  httpRequest1.request(Method.GET, "/v1?offset=0"+"&limit=5");
        
        int paginationTotal = response.jsonPath().getInt("meta.pagination.total");
        
        System.out.println(" Get Method " +paginationTotal); 
        
        System.out.println(" Get Method " +response.getBody().asString()); 
        
        int current=0;
        List<String> idtodelete= new ArrayList<String>();
        while(true) {
        	RequestSpecification httpRequest2;
        	httpRequest2 = RestAssured.given().given().header("Content-Type", "application/json").given()
                    .header("CLIENT_ID", APIHelper.readClient());
        	int idlimit;
        	/*if(current+pagination < paginationTotal) {
        		idlimit=pagination;
        	}
        	else {
        		idlimit=paginationTotal-current;
        	}*/
        	idlimit=pagination;
        	System.out.println("current="+current+ " limit="+idlimit);
			//process list
        	response =  httpRequest2.request(Method.GET, "/v1?offset="+current+"&limit="+idlimit);
        	
        	System.out.println(" Get Method " +response.getBody().asString());
			
        	List<String> numberOfResources=response.jsonPath().getList("resources");
        	
        	System.out.println("Number Of Resources " +numberOfResources.size());
        	/**
        	for(int i=0; i<=numberOfResources.size()-1; i++) {
        		idtodelete.add(numberOfResources.get(i));
            }**/
        	totalGet+=numberOfResources.size();
			current+=pagination;
			System.out.println("Total Get " +totalGet);
			if(current>paginationTotal)
				break;
        }                    
        Assert.assertEquals(paginationTotal, totalGet);
        return response;
	}
	
	public static int addAll(String params, JSONArray payload)
			throws ParseException, IOException, InterruptedException {
		RestAssured.baseURI = localHost+params;
		RequestSpecification httpRequest;
		Response response;
		System.out.println(localHost+params);
		//System.out.println(payload.toJSONString());
		httpRequest = RestAssured.given();
		
		httpRequest = RestAssured.given().given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient());
		int total=payload.size();
		
		int current = 0;
		while(true) {
			List list = payload.subList(current, current+APIHelper.readPagination()>total?total:current+APIHelper.readPagination());
			//process list
			JSONArray array1 = new JSONArray();
			for(int i=0;i<=list.size()-1;i++){
				JSONObject obj =  (JSONObject)list.get(i);
				System.out.println(obj.toJSONString());
				
				array1.add(obj);
				//Request specification 
	        	
        		httpRequest.body(array1.toString());
        		response =  httpRequest.request(Method.POST);
        		array1.remove(obj);
        		System.out.println("Response " +response);
        		System.out.println("Response " +response.getBody().asString());	
        		int resourceAffected=response.jsonPath().get("meta.writes.resources_affected");
        		int responseCode = response.getStatusCode();
        		Assert.assertEquals(responseCode, 200);
        		totalWrites+= resourceAffected;
        		System.out.println("Response " +response.getBody().asString());
        		System.out.println(" Total Writes " +totalWrites);
			}
			System.out.println("List " +list.size());
			current+=500;
			if(current>total)
				break;
		}
		
		return totalWrites;
	}
	
	public static Response addAllWithError(String params, JSONArray payload)
			throws ParseException, IOException, InterruptedException {
		
		Response response;
		RestAssured.baseURI = localHost+params;
				
		payload = APIHelper.parseJSONFile();
		
		response = RestAssured.given().given().header("Content-Type", "application/json").given()
		         .header("CLIENT_ID", APIHelper.readClient()).given().body(payload.toString()).request(Method.POST);
				
		return response;
	}
	
	public static Response addJSONObjAsInput(String params, JSONArray payload)
			throws ParseException, IOException, InterruptedException {
		RestAssured.baseURI = localHost+params;
		
		RequestSpecification httpRequest;
		Response response;
		
		httpRequest = RestAssured.given().given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient());
		JSONObject obj =  (JSONObject)payload.get(0);
	    System.out.println(obj.toJSONString());
			
		httpRequest.body(obj.toJSONString());
        response =  httpRequest.request(Method.POST);				
        System.out.println("Response " +response.getBody().asString());
        	
		return response;
	}
	
	public static Response getOneId(String params, String CVE)
			throws InterruptedException, IOException {
		
		logger.info(" -----------Checking data by using GETOneId Method-----------");
		RestAssured.baseURI = "https://jsonmock.hackerrank.com/api/movies/search";
		
		System.out.println(localHost+params);
		
        RequestSpecification httpRequest = RestAssured.given();

        Response responseGet = httpRequest.given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient()).
                request(Method.GET, "/?Title="+"Superman");
    	
        System.out.println(responseGet.getBody().asString());
    	       
        return responseGet;
		
	}	
	
	public static Response sortById(String params, int offset, int limit, String sort)
			throws InterruptedException, IOException {
		
		RequestSpecification httpRequest;	
		
		logger.info(" -----------Checking deleteOneId Method-----------");
		RestAssured.baseURI = localHost+params;
		System.out.println("sortById" +localHost+params);
		
        httpRequest = RestAssured.given();
        
        Response responseGet = httpRequest.given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient()).
                request(Method.GET, "/v1?offset="+offset+ "&limit="+limit+ "&sort="+sort);
        
        System.out.println(responseGet.getBody().asString());
	       
        return responseGet;
		
	}
	
	public static Response filterById(String params, String CVE)
			throws InterruptedException, IOException {
		
		RequestSpecification httpRequest;	
		
		logger.info(" -----------Checking filterById Method-----------");
		RestAssured.baseURI = localHost+params;
		System.out.println("sortById" +localHost+params);
		
        httpRequest = RestAssured.given();
        
        Response responseGet = httpRequest.given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient()).
                request(Method.GET, "/v1?ids="+CVE);
        
        System.out.println(responseGet.getBody().asString());
	       
        return responseGet;
		
	}
	
	public static Response deleteOneId(String paramsDelete, String CVE) 
			throws InterruptedException, IOException{
		RequestSpecification httpRequest;
		Response response;
		logger.info(" -----------Checking deleteOneId Method-----------");
		
		RestAssured.baseURI = localHost+paramsDelete;
		System.out.println("delete5Ids" +localHost+paramsDelete);
		
		//Request specification
        httpRequest = RestAssured.given();
        
        httpRequest = RestAssured.given().given().header("Content-Type", "application/json").given()
	                .header("CLIENT_ID", APIHelper.readClient());
	   
        response =  httpRequest.request(Method.DELETE, "/v1?ids="+CVE);
        System.out.println(response.getBody().asString());
         
        return response;        
	}	
	
	public static Response delete5Ids(String paramsGet, String paramsDelete, int offset, int limit) 
			throws InterruptedException, IOException{
		RequestSpecification httpRequest;
		RequestSpecification httpRequest1;
		RequestSpecification httpRequest2;
		Response response;
		logger.info(" -----------Checking delete5Ids Method-----------");
		
		RestAssured.baseURI = localHost+paramsGet;
		System.out.println("delete5Ids" +localHost+paramsGet);
		
		//Request specification
        httpRequest = RestAssured.given();
        
		response =  httpRequest.given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient()).
                request(Method.GET, "/v1?offset="+offset +"&limit="+limit);
		
		 List<String> idtodelete= new ArrayList<String>();
		 List<String> numberOfResources=response.jsonPath().getList("resources");
		 for(int i=0; i<=numberOfResources.size()-1; i++) {
			 idtodelete.add(numberOfResources.get(i));
			 
		 }
		 
		 RestAssured.baseURI = localHost+paramsDelete;
		 System.out.println("delete5Ids" +localHost+paramsDelete); 
	     httpRequest1 = RestAssured.given().given().header("Content-Type", "application/json").given()
	                .header("CLIENT_ID", APIHelper.readClient());
	     
	    String ids="";
        for(int i=0; i<=idtodelete.size()-1; i++) { 
         	if(i!=0) ids+="&ids=";
 	       	ids+=idtodelete.get(i);
         }
		 System.out.println(ids);
         response =  httpRequest1.request(Method.DELETE, "/v1?ids="+ids);
         System.out.println(response.getBody().asString());
         
         return response;
        
	}
	
	public static Response deleteAll(String paramsGet, String paramsDelete, int pagination, int offset, int limit, int total) 
			throws InterruptedException, IOException{
		
		RequestSpecification httpRequest;
		RequestSpecification httpRequest3;
		Response response;
		RestAssured.baseURI = localHost+paramsGet;
		System.out.println("Get " +localHost+paramsGet); 
		
        //Request specification

        httpRequest = RestAssured.given().given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient());
        
        response =  httpRequest.request(Method.GET, "/v1?offset=0"+"&limit=5");
        
        int paginationTotal = response.jsonPath().getInt("meta.pagination.total");
        
        System.out.println(" DeleteAll Method " +paginationTotal); 
        
        System.out.println(" DeleteAll Method " +response.getBody().asString()); 
        
        int current=0;
        List<String> idtodelete= new ArrayList<String>();
        while(true) {
        	RequestSpecification httpRequest2;
        	httpRequest2 = RestAssured.given().given().header("Content-Type", "application/json").given()
                    .header("CLIENT_ID", APIHelper.readClient());
        	int idlimit;
        	/*if(current+pagination < paginationTotal) {
        		idlimit=pagination;
        	}
        	else {
        		idlimit=paginationTotal-current;
        	}*/
        	idlimit=pagination;
        	System.out.println("current="+current+ " limit="+idlimit);
			//process list
        	response =  httpRequest2.request(Method.GET, "/v1?offset="+current+"&limit="+idlimit);
        	
        	System.out.println(" Delete Method " +response.getBody().asString());
			
        	List<String> numberOfResources=response.jsonPath().getList("resources");
        	
        	System.out.println("Number Of Resources " +numberOfResources.size());
        	
        	for(int i=0; i<=numberOfResources.size()-1; i++) {
        		idtodelete.add(numberOfResources.get(i));
            }
        	totalGet+=numberOfResources.size();
			current+=pagination;
			System.out.println("Total Get " +totalGet);
			if(current>paginationTotal)
				break;
        }                    
        Assert.assertEquals(paginationTotal, totalGet);
        
                
        RestAssured.baseURI = localHost+paramsDelete;
        System.out.println("deleteAll" +localHost+paramsDelete);
        httpRequest3 = RestAssured.given().given().header("Content-Type", "application/json").given()
                .header("CLIENT_ID", APIHelper.readClient());
        
        int crnt=0;
        int tot=idtodelete.size();
        while(true) {
    		List sublist=idtodelete.subList(crnt, crnt+500<tot?crnt+500:tot);
    		String ids="";
    		System.out.println("sublist size"+sublist.size());
            for(int i=0; i<=sublist.size()-1; i++) {
            	//Request specification 
            	if(i!=0) ids+="&ids=";
    	       	ids+=idtodelete.get(i);
            }
            System.out.println(ids);
            response =  httpRequest3.request(Method.DELETE, "/v1?ids="+ids);
            System.out.println(response.getBody().asString());
            
            crnt+=500;
            if(crnt>tot) 
            	break;	
        }
        return response;	
		
	}
	
}
