package myMP3Player;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author David Rochinha (310280)
 */
public class Bean {

    private DoubleProperty time = new SimpleDoubleProperty();

    public double getTime() {
        return time.get();
    }

    public DoubleProperty timeProperty() {
        return time;
    }

    public void setTime(double time) {
        this.time.set(time);
    }
}
