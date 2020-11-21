package myMP3Player;

import java.util.Collection;

public interface MyAudioPlayer {
    void add(String path);

    void addAll(Collection<String> paths);

    void next();

    void pause();

    void play();

    void previous();

    void remove(int index);

    void stop();
}
