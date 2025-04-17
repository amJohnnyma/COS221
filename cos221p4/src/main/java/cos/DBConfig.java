package cos;

import java.io.IOException;
import java.util.Properties;

public class DBConfig {
    public static String DB_URL;
    public static String DB_USER;
    public static String DB_PASSWORD;

    static {
        try {
            Properties props = new Properties();
            props.load(DBConfig.class.getClassLoader().getResourceAsStream(".dbconfig.properties")); 
                       DB_URL = props.getProperty("DB_URL");
            DB_USER = props.getProperty("DB_USER");
            DB_PASSWORD = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}