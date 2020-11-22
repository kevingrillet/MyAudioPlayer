package myMusicPlayer.AudioPlayer;

import javafx.collections.FXCollections;
import myMusicPlayer.Bean;
import myMusicPlayer.Utils.UtilsProperties;

import java.util.Collection;
import java.util.List;

public abstract class MyAudioPlayerAbstract implements MyAudioPlayerInterface {
    private final static String formats = "*.mp3, *.wav, *.WAV";
    protected final Bean bean;

    public MyAudioPlayerAbstract(Bean bean) {
        this.bean = bean;
        this.bean.setQueue(FXCollections.<String>observableArrayList());
    }

    @Override
    public void add(String path) {
        bean.getQueue().add(path);
    }

    @Override
    public void addAll(Collection<String> paths) {
        bean.getQueue().addAll(paths);
    }

    @Override
    public List<String> getFormats() {
        return UtilsProperties.readFormats(formats);
    }

    @Override
    public void remove(int index) {
        bean.getQueue().remove(index);
        if (index == 0) setMedia();
    }
}
