package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class APIHelper {
	
	FileReader fileReader;
	static Properties props;
	
	static {
		try {
			props = new Properties();
			props.load(new FileInputStream(System.getProperty("user.dir") + "/src/test/config/config.properties"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String readURL() throws IOException {
		// Returns a string
		String str = props.getProperty("url");
        return str;     
	}
	
	public static String readClient() throws IOException {
		// Returns a string
		String client_id = props.getProperty("client_id");
        return client_id;     
	}
	
	public static int readPagination() throws IOException {
		// Returns a string
		String str = props.getProperty("pagination");
		int pagination= Integer.parseInt(str);
        return pagination;     
	}
	
	public static JSONArray parseJSONFile() throws ParseException, IOException {
		JSONParser parser = new JSONParser();
		FileReader fileReader;
		File file = new File(System.getProperty("user.dir") + "/src/test/data/cves_input.json");
		fileReader = new FileReader(file);
		// Parser returns an object, we need an explicit cast to covert it into a JSONArray
		JSONArray array = (JSONArray) parser.parse(fileReader);
        return array;      
    }
	
	public static JSONArray parseOneJSONFile() throws ParseException, IOException {
		JSONParser parser = new JSONParser();
		FileReader fileReader;
		File file = new File(System.getProperty("user.dir") + "/src/test/data/data.JSON");
		fileReader = new FileReader(file);
		// Parser returns an object, we need an explicit cast to covert it into a JSONArray
		JSONArray array = (JSONArray) parser.parse(fileReader);
        return array;      
    }
	
	public static int offset() {
		int offset= 0;
		return offset;
	}
	
	public static int limit() {
		int limit= 100;
		return limit;
	}
	
	public static void main(String args[]) {
		
		try {
			JSONArray array = parseJSONFile();
			//System.out.println(array);
			JSONArray array1 = new JSONArray();
			//Traverse the list
			for(int i=0;i<array.size();i++)
			{
				JSONObject obj =  (JSONObject)array.get(i);
				array1.add(obj);
				System.out.println(array1 + " " +i);
				array1.remove(obj);
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


}
