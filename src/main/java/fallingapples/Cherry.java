package fallingapples;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Cherry extends Fruit {

    public Cherry(double centerX, double centerY) {
        super(centerX, centerY, 25, false);
        Image apple = new Image(getClass().getResource("/cherry.png").toExternalForm());
        ImagePattern applePattern = new ImagePattern(apple);
        this.setFill(applePattern);
    }

}
