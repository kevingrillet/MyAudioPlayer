package myMusicPlayer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;

public class Bean {

    private DoubleProperty time = new SimpleDoubleProperty();
    private ObjectProperty<List<String>> queue = new SimpleObjectProperty();

    public List<String> getQueue() {
        return queue.get();
    }

    public void setQueue(List<String> queue) {
        this.queue.set(queue);
    }

    public ObjectProperty<List<String>> queueProperty() {
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
