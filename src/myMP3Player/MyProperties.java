package myMP3Player;

import myMP3Player.Utils.UtilsProperties;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class MyProperties {
    private Map<String, String> map;
    private Mixer.Info audioOutput;
    private boolean autoSave;
    private List<String> formats;
    private double masterVolume;
    private Path pathToMusic;

    public MyProperties(){
        autoSave = false;
        load();
    }

    public void load() {
        map = UtilsProperties.readProperties();

        String mixerName = map.getOrDefault("audioOutput","");
        if (mixerName.isEmpty()) {
            // set default output with getMixer(null)
            setAudioOutput(AudioSystem.getMixer(null).getMixerInfo());
        }else{
            for (Mixer.Info info : AudioSystem.getMixerInfo()) {
                if (info.getName().equals(mixerName)) {
                    setAudioOutput(AudioSystem.getMixer(info).getMixerInfo());
                    break;
                }
            }
        }
        setFormats(UtilsProperties.readFormats(map.getOrDefault("formats","*.mp3, *.wav")));
        setMasterVolume(Double.parseDouble(map.getOrDefault("masterVolume","100.0")));
        setPathToMusic(Paths.get(map.getOrDefault("pathToMusic", "")));
    }

    public void save() {
        UtilsProperties.writeProperties(map);
    }

    private void setInMap(String key, String value) {
        if (map.containsKey(key)){
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
        setInMap("audioOutput",audioOutput.getName());
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
        setInMap("formats", formats.stream().reduce((acc, n) -> (acc + "," + n)).get());
        if (autoSave) save();
    }

    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double masterVolume) {
        if (this.masterVolume == masterVolume) return;
        this.masterVolume = masterVolume;
        setInMap("masterVolume",String.valueOf(masterVolume));
        if (autoSave) save();
    }

    public Path getPathToMusic() {
        return pathToMusic;
    }

    public void setPathToMusic(Path pathToMusic) {
        if (this.pathToMusic == pathToMusic) return;
        this.pathToMusic = pathToMusic;
        setInMap("pathToMusic",String.valueOf(pathToMusic.toString()));
        if (autoSave) save();
    }
    /*_____ GETTER & SETTER _____*/
}
