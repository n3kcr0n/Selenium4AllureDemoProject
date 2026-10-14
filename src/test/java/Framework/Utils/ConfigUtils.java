package Framework.Utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {
    //properties initialized
    Properties properties;

    //Singleton Pattern
    private static ConfigUtils configUtils;
    //Constructor
    private ConfigUtils(){
        properties = readProperties();
    }
    public static ConfigUtils getInstance(){
        if(configUtils == null){
            configUtils = new ConfigUtils();
        }
        return configUtils;
    }

    private Properties readProperties(){
        InputStream is = null;
        try {
            String env = System.getProperty("env","DEV");
            switch(env) {
                case "DEV" -> {
                    is = new FileInputStream("src/test/resources/env/dev.properties");
                }
                case "QA" -> {
                    is = new FileInputStream("src/test/resources/env/qa.properties");
                }
                case "UAT" -> {
                    is = new FileInputStream("src/test/resources/env/uat.properties");
                }
                default -> {
                    throw new RuntimeException("Environment properties is not supported!");
                }
            }
            properties = new Properties();
            properties.load(is);

        } catch (Exception error){
            System.out.println("ERROR CAUSE: " + error.getCause());
            System.out.println("ERROR: " + error.getMessage());
        }
        return properties;
    }

    public String getBaseUrl() {
        return properties.getProperty("URL");
    }

    public String getProperty(String prop) {
        return properties.getProperty(prop);
    }
}
