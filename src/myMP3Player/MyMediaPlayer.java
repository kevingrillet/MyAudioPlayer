package myMP3Player;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MyMediaPlayer implements MyAudioPlayer {
    private final List<String> queue;
    private long duration;
    private MediaPlayer mediaPlayer;

    public MyMediaPlayer() {
        queue = new LinkedList<>();
    }

    @Override
    public void add(String path) {
        queue.add(path);
    }

    @Override
    public void addAll(Collection<String> paths) {
        queue.addAll(paths);
    }

    @Override
    public double getDuration() {
        return mediaPlayer.getTotalDuration().toMillis();
    }

    @Override
    public double getTime() {
        return mediaPlayer.getCurrentTime().toMillis();
    }

    @Override
    public double getVolume() {
        if (!(mediaPlayer == null)) {
            return mediaPlayer.getVolume();
        } else {
            return -1;
        }
    }

    @Override
    public void next() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING
                    || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
                    || mediaPlayer.getStatus() == MediaPlayer.Status.READY
                    || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                mediaPlayer.seek(mediaPlayer.getTotalDuration());
            }
        }
    }

    @Override
    public void pause() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            }
        }
    }

    @Override
    public void play() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
                    || mediaPlayer.getStatus() == MediaPlayer.Status.READY
                    || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                mediaPlayer.play();
            }
        }
    }

    @Override
    public void previous() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING
                    || mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED
                    || mediaPlayer.getStatus() == MediaPlayer.Status.READY
                    || mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                mediaPlayer.seek(new Duration(0));
            }
        }
    }

    @Override
    public void remove(int index) {
        queue.remove(index);
    }

    @Override
    public void seek(double time) {
        if (!(mediaPlayer == null)) {
            mediaPlayer.seek(new Duration(time));
        }
    }

    @Override
    public void setVolume(double volume) {
        assert (volume >= 0 && volume <= 1);
        if (!(mediaPlayer == null)) {
            mediaPlayer.setVolume(volume);
        }
    }

    @Override
    public void stop() {
        if (!(mediaPlayer == null)) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.stop();
                mediaPlayer.seek(mediaPlayer.getStartTime());
            }
        }
    }
}
