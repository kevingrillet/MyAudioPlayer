package myMP3Player;

import java.util.Collection;

public interface MyAudioPlayer {
    /**
     * Add music to the listening queue
     * @param path (String) : Path to the music
     */
    void add(String path);

    /**
     * Add music to the listening queue
     * @param paths (Collection<String>) : Collection of paths to the musics
     */
    void addAll(Collection<String> paths);

    /**
     * Get End time
     * @return (double): end time in milliseconds
     */
    double getDuration();

    String getMediaName();

    /**
     * Get the current time
     * @return (double): current time in milliseconds
     */
    double getTime();

    /**
     *  Return Volume of mediaPlayer.
     * @return Return volume between 0 and 1. -1 is return if MediaPlayer is null.
     */
    double getVolume();

    /**
     * Play the next music
     */
    void next();

    /**
     * Pause music
     */
    void pause();

    /**
     * Play the music, resume if paused
     */
    void play();

    void previous();

    void remove(int index);

    /**
     * Go to time position
     * @param time (long): time in milliseconds
     */
    void seek(double time);

    void setMedia();

    /**
     * Value between 0 and 1
     * @param volume (double): volume between 0 and 1
     */
    void setVolume(double volume);

    /**
     * Stop music
     */
    void stop();
}
