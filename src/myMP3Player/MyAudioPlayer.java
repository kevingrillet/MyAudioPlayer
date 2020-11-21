package myMP3Player;

import java.util.Collection;

public interface MyAudioPlayer {
    void add(String path);

    void addAll(Collection<String> paths);

    double getDuration();

    double getTime();

    /**
     *  Return Volume of mediaPlayer.
     * @return Return volume between 0 and 1. -1 is return if MediaPlayer is null.
     */
    double getVolume();

    void next();

    void pause();

    void play();

    void previous();

    void remove(int index);

    void seek(double time);

    void setVolume(double volume);

    void stop();
}
