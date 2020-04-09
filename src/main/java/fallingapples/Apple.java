package fallingapples;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Apple extends Fruit {

    public Apple(double centerX, double centerY) {
        super(centerX, centerY, 25, false);
        Image apple = new Image(getClass().getResource("/apple.png").toExternalForm());
        ImagePattern applePattern = new ImagePattern(apple);
        this.setFill(applePattern);
    }
}
