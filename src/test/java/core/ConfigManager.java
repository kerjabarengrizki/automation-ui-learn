package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    public static Properties loadProperties(String env) {
        Properties prop = new Properties();
        if (env == null || env.isEmpty()) env = "staging";


        String path = System.getProperty("user.dir") + "/src/test/resources/config/" + env + ".properties";

        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("Config file not found for env '" + env + "': " + path
                    + " (check the file name/location or pass -Denv=<name>)");
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + path, e);
        }


        return prop;
    }
}
