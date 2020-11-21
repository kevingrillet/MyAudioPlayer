package myMP3Player;

import javafx.collections.MapChangeListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import myMP3Player.Utils.UtilsProperties;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MyMediaPlayer implements MyAudioPlayer {
    private final static String formats = "*.aif, *.aiff, *.aifc, *.m4a, *.mp3, *.wav, *.WAV";
    private final Bean bean;
    private final List<String> queue;
    private MediaPlayer mediaPlayer;

    public MyMediaPlayer(Bean bean) {
        queue = new LinkedList<>();
        this.bean = bean;
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
    public List<String> getFormats() {
        return UtilsProperties.readFormats(formats);
    }

    @Override
    public String getMediaName() {
        if (!(mediaPlayer == null)) {
            String title = mediaPlayer.getMedia().getSource();
            title = title.substring(0, title.length() - ".mp3".length());
            title = title.substring(title.lastIndexOf("/") + 1).replaceAll("%20", " ");
            // TODO Find how to get title out of the listener.
            mediaPlayer.getMedia().getMetadata().addListener((MapChangeListener.Change<? extends String, ?> c) -> {
                if (c.wasAdded()) {
                    if ("title".equals(c.getKey())) {
                        System.out.println(c.getValueAdded().toString());
//                        title = c.getValueAdded().toString();
                    }
                }
            });
            return title;
        } else {
            return null;
        }
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
    public void setVolume(double volume) {
        assert (volume >= 0 && volume <= 1);
        if (!(mediaPlayer == null)) {
            mediaPlayer.setVolume(volume);
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
    public void setMedia() {
        Media media = new Media(new File(queue.get(0)).toURI().toString());

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.currentTimeProperty().addListener(observable -> bean.setTime(getTime()));
        mediaPlayer.setOnReady(() -> bean.setTime(getTime()));
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer.seek(new Duration(0));
            queue.remove(0);
            // TODO refresh interface
            bean.getQueue().remove(0);
            if (queue.size() > 0) {
                setMedia();
                mediaPlayer.play();
            }
        });
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
