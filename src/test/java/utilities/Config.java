package utilities;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
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

}
