package myMP3Player;

import java.util.Collection;

public interface MyAudioPlayer {
    /**
     * Add music to the listening queue
     * @param path String : Path to the music
     */
    void add(String path);

    /**
     * Add music to the listening queue
     * @param paths (Collection<String>) : Collection of paths to the musics
     */
    void addAll(Collection<String> paths);

    double getDuration();

    String getMediaName();

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

    void setMedia();

    void setVolume(double volume);

    void stop();
}
