package myMP3Player;

import java.util.Collection;

public interface MyAudioPlayer {
    public void add(String path);

    public void addAll(Collection<String> paths);

    public void next();

    public void pause();

    public void play();

    public void previous();

    public void remove(int index);

    public void stop();
}
