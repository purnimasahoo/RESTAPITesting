package datadriventesting.movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import base.TestBase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class GetMoviesTitle extends TestBase{
	
	public static Logger logger = LogManager.getLogger(GetMoviesTitle.class);
	
	@BeforeClass
	public void setUp() {
		logger.info(" -----------Starting APITestGet-----------");
		RequestSpecification httpRequest;
		Response response;
	}
	
	//To access the movie details by subtitle , make a GET request to https://jsonmock.hackerrank.com/api/movies/search/?Title=substr&page=1
	private static final String URL = "https://jsonmock.hackerrank.com/api/movies/search/?Title=%s";
		
	
	/**The query response from the website is JSON response with the following fields:
		page: The current page
		per_page: The maximum number of results per page.
		total: The total number of movies in the search result
		total_pages: The total number of pages which must be queried to get all the results.
		Data: An array of JSON objects containing movie information where the Title field denotes the 
		title of the movie. In order to get all results, you may have to make multiple page requests. 
		To request a page by number, your query should read 
		https://jsonmock.hackerrank.com/api/movies/search/?Title=substr&page=pageNumber, 
		replacing substr and pageNumber
	**/
	@Test
	public static void testMovieTitles() 
			throws InterruptedException, IOException {
		
		RequestSpecification httpRequest;
		Response response = null;
		List<String> movieTitles = new ArrayList<>();
		try {
			logger.info(" -----------Starting DataDrivenTest_testMovieTitles-----------");
			String params = "superman";	
			int page = 1;
			int totalPage=1;
			while(page<=totalPage) {
				response=getMovieTitles(params,page);	
				
				int responseCode = response.getStatusCode();
				Assert.assertEquals(responseCode, 200);	
				
				totalPage = response.jsonPath().getInt("total_pages");
				List<LinkedHashMap<String,String>> titles;
				
				titles=response.jsonPath().getList("data");
				System.out.println(" Title " +titles);
				System.out.println(" Title 0th " +titles.get(0));
				
				for (LinkedHashMap<String, String> e : titles) {				
					System.out.println("Purnima" + " -- "
	                        + e.get("Title"));
					movieTitles.add(e.get("Title"));
				    
					/**
			        // printing the elements of LinkedHashMap
			        Set<String> keys = e.keySet();
			        for (String key : keys) {
			            System.out.println(key + " -- "
			                               + e.get("Title"));
			        }	**/			
				}
				
				Assert.assertEquals(movieTitles.size(), 2);
				page++;
			}
			
				
		}
		catch(NullPointerException npe) {
			npe.printStackTrace();
		}
		catch(InterruptedException ine) {
			ine.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException{
		
		//List<String> titles = getMovieTitles("superman");
		//Collections.sort(titles);
		//System.out.println(titles.size());
		//titles.forEach(System.out::println);
	}
}

