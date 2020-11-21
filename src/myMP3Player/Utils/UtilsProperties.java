package myMP3Player.Utils;

import java.io.*;
import java.util.*;

public class UtilsProperties {
    final private static String pathToConfig = "src/myMP3Player/Resources/config.properties";

    /**
     *  Load properties on startup
     * @return Map<String key, String value>
     */
    public static Map<String, String> readProperties() {
        Map<String, String> map = new HashMap<>();
        File file = new File(pathToConfig);
        if(!file.exists()) {
            try {
                new FileOutputStream(pathToConfig);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try (InputStream inputStream = new FileInputStream("src/myMP3Player/Resources/config.properties")) {
//        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream ("Resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            map.put("masterVolume", properties.getProperty("masterVolume", "100.0"));
            map.put("audioOutput", properties.getProperty("audioOutput", ""));
            map.put("pathToMusic", properties.getProperty("pathToMusic", ""));
            map.put("formats", properties.getProperty("formats", "*.mp3,*.wav"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     *  Save properties
     * @param map Map<String key, String value>
     */
    public static void writeProperties(Map<String,String> map) {
        try (OutputStream outputStream = new FileOutputStream(pathToConfig)) {
            Properties properties = new Properties();

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                properties.setProperty(key, value);
            }

            // Just for info
//            properties.setProperty("masterVolume", map.get("masterVolume"));
//            properties.setProperty("audioOutput", map.get("audioOutput"));
//            properties.setProperty("pathToMusic", map.get("pathToMusic"));
//            properties.setProperty("formats", map.get("formats"));

            properties.store(outputStream, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static List<String> readFormats(String formatsProperty) {
        return Arrays.asList(formatsProperty.split(","));
    }
}
