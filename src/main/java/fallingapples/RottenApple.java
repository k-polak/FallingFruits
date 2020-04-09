package fallingapples;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class RottenApple extends Fruit {

    public RottenApple(double centerX, double centerY) {
        super(centerX, centerY, 25, true);
        Image apple = new Image(getClass().getResource("/rotten_apple.png").toExternalForm());
        ImagePattern applePattern = new ImagePattern(apple);
        this.setFill(applePattern);
    }
}
