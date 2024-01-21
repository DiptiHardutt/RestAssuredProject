package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;



public class ConfigReader {
	private static Properties properties;

    static {
        properties = new Properties();
        try {
        	FileInputStream input = new FileInputStream("src/main/java/util/config.properties");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
	
	/*  private static Properties properties;

	   
	public static void readConfig() {
		try {
			properties = new Properties();
			FileReader fileReader = new FileReader("src\\main\\java\\util\\config.properties");
			properties.load(fileReader);
			
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
		
	}*/
	}


