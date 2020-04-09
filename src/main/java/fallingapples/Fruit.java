package fallingapples;

import javafx.scene.shape.Circle;

public class Fruit extends Circle {
    boolean isRotten;

    public Fruit(double centerX, double centerY, double radius, boolean isRotten) {
        super(centerX, centerY, radius);
        this.isRotten = isRotten;
    }
}
