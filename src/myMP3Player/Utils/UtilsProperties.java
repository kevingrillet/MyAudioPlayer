package myMP3Player.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UtilsProperties {

    /**
     *  Load properties
     * @param pathToConfig Path to config file
     * @param configFile Name of the config file
     * @return Map<String key, String value>
     */
    public static Map<String, String> readProperties(String pathToConfig, String configFile) {
        Map<String, String> map = new HashMap<>();
        File file = new File(pathToConfig + "/" + configFile);
        if(!file.exists()) {
            try {
                Files.createDirectories(Paths.get(pathToConfig));
                new FileOutputStream(pathToConfig + "/" + configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (InputStream inputStream = new FileInputStream("src/myMP3Player/Resources/config.properties")) {
//        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream ("Resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            properties.forEach((key, value) -> map.put(key.toString(), value.toString()));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     * Save properties
     * @param pathToConfig Path to config file
     * @param configFile Name of the config file
     * @param map Map<String key, String value>
     */
    public static void writeProperties(String pathToConfig, String configFile, Map<String,String> map) {
        try (OutputStream outputStream = new FileOutputStream(pathToConfig + "/" + configFile)) {
            Properties properties = new Properties();

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                properties.setProperty(key, value);
            }

            properties.store(outputStream, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Format formats
     * @param formatsProperty ex: *.mp3, *.wav
     * @return [*.mp3; *.wav]
     */
    public static List<String> readFormats(String formatsProperty) {
        return Arrays.asList(formatsProperty.split(","));
    }
}
