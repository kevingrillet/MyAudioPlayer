package myAudioPlayer;

import myAudioPlayer.Utils.UtilsProperties;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Properties of myMP3Player
 */
public class MyProperties {
    private final static String AUDIO_OUTPUT = "audioOutput";
    private final static String FORMATS = "formats";
    private final static String MASTER_VOLUME = "masterVolume";
    private final static String PATH_TO_MUSIC = "pathToMusic";

    private final static String configFile = "config.properties";
    private final static String pathToConfig = "src/myAudioPlayer/Resources";

    private final static String defaultAudioOutput = "";
    private final static String defaultFormats = "*.mp3, *.wav";
    private final static String defaultMasterVolume = "100.0";
    private final static String defaultPathToMusic = "";

    private Map<String, String> map;
    private Mixer.Info audioOutput;
    private boolean autoSave;
    private List<String> formats;
    private double masterVolume;
    private Path pathToMusic;

    /**
     * Constructor
     */
    public MyProperties() {
        autoSave = false;
        load();
    }

    /**
     * Load data from my config.properties file
     * If empty data found set default values.
     */
    public void load() {
        map = UtilsProperties.readProperties(pathToConfig, configFile);

        String mixerName = map.getOrDefault(AUDIO_OUTPUT, defaultAudioOutput);
        if (mixerName.isEmpty()) {
            // set default output with getMixer(null)
            setAudioOutput(AudioSystem.getMixer(null).getMixerInfo());
        } else {
            for (Mixer.Info info : AudioSystem.getMixerInfo()) {
                if (info.getName().equals(mixerName)) {
                    setAudioOutput(AudioSystem.getMixer(info).getMixerInfo());
                    break;
                }
            }
        }
        setFormats(UtilsProperties.readFormats(map.getOrDefault(FORMATS, defaultFormats)));
        setMasterVolume(Double.parseDouble(map.getOrDefault(MASTER_VOLUME, defaultMasterVolume)));
        setPathToMusic(Paths.get(map.getOrDefault(PATH_TO_MUSIC, defaultPathToMusic)));
    }

    /**
     * Save my properties in config.properties
     */
    public void save() {
        UtilsProperties.writeProperties(pathToConfig, configFile, map);
    }

    /**
     * Check if exists, then add or update in the map
     *
     * @param key   Map<key,_>
     * @param value Map<_,value>
     */
    private void setInMap(String key, String value) {
        if (map.containsKey(key)) {
            map.replace(key, value);
        } else {
            map.put(key, value);
        }
    }

    /*_____ GETTER & SETTER _____*/
    public Mixer.Info getAudioOutput() {
        return audioOutput;
    }

    public void setAudioOutput(Mixer.Info audioOutput) {
        if (this.audioOutput == audioOutput) return;
        this.audioOutput = audioOutput;
        setInMap(AUDIO_OUTPUT, audioOutput.getName());
        if (autoSave) save();
    }

    public void setAutoSave(boolean autoSave) {
        if (this.autoSave == autoSave) return;
        this.autoSave = autoSave;
        if (autoSave) save();
    }

    public List<String> getFormats() {
        return formats;
    }

    public void setFormats(List<String> formats) {
        if (this.formats == formats) return;
        this.formats = formats;
        setInMap(FORMATS, formats.stream().reduce((acc, n) -> (acc + "," + n)).get());
        if (autoSave) save();
    }

    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double masterVolume) {
        if (this.masterVolume == masterVolume) return;
        this.masterVolume = masterVolume;
        setInMap(MASTER_VOLUME, String.valueOf(masterVolume));
        if (autoSave) save();
    }

    public Path getPathToMusic() {
        return pathToMusic;
    }

    public void setPathToMusic(Path pathToMusic) {
        if (this.pathToMusic == pathToMusic) return;
        this.pathToMusic = pathToMusic;
        setInMap(PATH_TO_MUSIC, String.valueOf(pathToMusic.toString()));
        if (autoSave) save();
    }
    /*_____ GETTER & SETTER _____*/
}
