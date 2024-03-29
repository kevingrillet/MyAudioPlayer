package myAudioPlayer.AudioPlayer;

import javafx.collections.FXCollections;
import myAudioPlayer.Bean;
import myAudioPlayer.Utils.UtilsProperties;

import java.util.Collection;
import java.util.List;

public abstract class MyAudioPlayerAbstract implements MyAudioPlayerInterface {
    protected final Bean bean;
    protected String formats = "*.aif, *.aiff, *.aifc, *.m4a, *.wav, *.WAV";
    private double duration;

    public MyAudioPlayerAbstract(Bean bean) {
        this.bean = bean;
        this.bean.setQueue(FXCollections.observableArrayList());
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
    public double getDuration() {
        return duration;
    }

    @Override
    public void setDuration(double duration) {
        this.duration = duration;
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
