package myMusicPlayer;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class Bean {
    private final ListProperty<String> queue = new SimpleListProperty<>();

    private final DoubleProperty time = new SimpleDoubleProperty();

    private final StringProperty title = new SimpleStringProperty();

    public ObservableList<String> getQueue() {
        return queue.get();
    }

    public void setQueue(ObservableList<String> queue) {
        this.queue.set(queue);
    }

    public ListProperty<String> queueProperty() {
        return queue;
    }

    public double getTime() {
        return time.get();
    }

    public void setTime(double time) {
        this.time.set(time);
    }

    public DoubleProperty timeProperty() {
        return time;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }
}
