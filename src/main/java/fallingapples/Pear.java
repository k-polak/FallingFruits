package fallingapples;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

public class Pear extends Fruit {

    public Pear(double centerX, double centerY) {
        super(centerX, centerY, 25, false);
        Image apple = new Image(getClass().getResource("/pear.png").toExternalForm());
        ImagePattern applePattern = new ImagePattern(apple);
        this.setFill(applePattern);
    }

}
