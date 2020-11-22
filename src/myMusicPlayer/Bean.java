package myMusicPlayer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public class Bean {
    private final ListProperty<String> queue = new SimpleListProperty<>();
    ;
    private final DoubleProperty time = new SimpleDoubleProperty();

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
}
