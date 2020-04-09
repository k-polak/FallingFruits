package fallingapples;

import javafx.animation.PathTransition;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.awt.AWTException;
import java.awt.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game {
    private Pane root;
    private GameWindow gameWindow;
    private Rectangle bucket;
    private List<ImageView> hearts;
    private Label score;
    Thread throwersThread;
    int counter;
    int life;
    Thrower thrower;

    public Game(GameWindow gameWindow){
        this.gameWindow = gameWindow;
        thrower = new Thrower(this);
        root = new Pane();
        bucket = new Rectangle(0, GameWindow.height - 50, 77, 50);
        hearts = new ArrayList();
        score = new Label("Score: "+counter);
        life = 3;
    }

    public void build(){
        root.setOnMouseMoved(e ->{
            bucket.setX(e.getX() - 50);
        });

        Image bucketImage = new Image(getClass().getResource("/bucket.png").toExternalForm());
        ImagePattern bucketPattern = new ImagePattern(bucketImage);
        bucket.setFill(bucketPattern);

        buildHearts();

        score.setFont(new Font("Arial", 20));
        score.setLayoutX(10);
        score.setLayoutY(10);

        root.setId("gameBackground");
        root.getChildren().addAll(bucket,score);

        Robot robot = null;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        Robot finalRobot = robot;
        root.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                double winX = gameWindow.stage.getX();
                double winY = gameWindow.stage.getY();
                if (x < 0) {
                    finalRobot.mouseMove((int) winX + 10, (int) (y + winY + 25));
                }
                if (x >= GameWindow.width) {
                    finalRobot.mouseMove((int) (winX + GameWindow.width) - 1, (int) (y + winY + 25));
                }
            }
        });
    }

    private void buildHearts(){
        Image heart = new Image(getClass().getResource("/heart.png").toExternalForm());
        ImageView heartView1 = new ImageView(heart);
        heartView1.setX(GameWindow.width - 20);
        heartView1.setY(0);
        ImageView heartView2 = new ImageView(heart);
        heartView2.setX(GameWindow.width - 40);
        heartView2.setY(0);
        ImageView heartView3 = new ImageView(heart);
        heartView3.setX(GameWindow.width - 60);
        heartView3.setY(0);

        hearts.add(heartView3);
        hearts.add(heartView2);
        hearts.add(heartView1);

        root.getChildren().addAll(heartView1,heartView2,heartView3);
    }

    public void throwFruit(){
        int speed = getSpeed();
        Fruit fruit = getFruit();
        Path path = new Path();

        root.getChildren().add(0,fruit);

        path.getElements().add(new MoveTo(fruit.getCenterX(), fruit.getCenterY()));
        path.getElements().add(new LineTo(fruit.getCenterX(), GameWindow.height));


        PathTransition pathTransition = new PathTransition(Duration.millis(speed),path,fruit);
        pathTransition.setNode(fruit);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(false);
        pathTransition.setOnFinished(e -> {
            handleFruitDrop(fruit);
        });

        pathTransition.play();
    }

    private void handleFruitDrop(Fruit fruit){
        if(isFruitCaught(fruit)){
            handleFruitCaught(fruit);
        }else{
            handleFruitMissed(fruit);
        }
        fruit.setVisible(false);
        root.getChildren().remove(fruit);
    }

    private void handleFruitCaught(Fruit fruit){
        if(fruit.isRotten){
            life--;
            if (life > 0) {
                hideHeart();
            }
            if (life <= 0) {
                gameOver();
            }
        }else{
            counter++;
            score.setText(String.valueOf(counter));
        }
    }
    private void handleFruitMissed(Fruit fruit){
        if(fruit.isRotten){
            counter++;
            score.setText(String.valueOf(counter));
        }else{
            life--;
            if (life >= 0) {
                System.out.println("remove heart");
                hideHeart();
            }
            if (life <= 0) {
                gameOver();
            }
        }
    }

    private boolean isFruitCaught(Fruit fruit){
        if (fruit.getCenterX() > bucket.getX() && fruit.getCenterX() < bucket.getX() + 100) {
            return true;
        }else{
            return false;
        }
    }

    private void gameOver(){
        stop();
        gameWindow.handleGameOver(counter);
    }

    public void restart(){
        hearts.forEach(e->{
            e.setVisible(true);
        });
        life = 3;
        counter = 0;
        score.setText(String.valueOf(counter));
        start();
    }

    private void hideHeart(){
        for(int i = 0;i<hearts.size();i++){
            if(hearts.get(i).isVisible()){
                hearts.get(i).setVisible(false);
                break;
            }
        }
    }

    public Pane getRoot() {
        return root;
    }

    private int getSpeed(){
        int delta = 0;
        int speed;
        Random random = new Random();
        if(counter > 30){
            delta = random.nextInt(200);
        }
        if(counter < 90){
            speed = 1000 - (int)(8.8*counter)+delta;
        }else{
            speed = 200 + delta;
        }
        return speed;
    }

    private Fruit getFruit(){
        Random random = new Random();
        int num = random.nextInt() % 4;
        double x = random.nextInt(GameWindow.width - 25) + 25;
        switch(num){
            case 0:
                return new Pear(x,0);
            case 1:
                return new Apple(x,0);
            case 2:
                return new Cherry(x,0);
            case 3:
                return new RottenApple(x,0);
        }
        return new RottenApple(x,0);
    }

    public void start(){
        throwersThread = new Thread(this.thrower);
        throwersThread.start();
    }

    public void stop(){
        try {
            throwersThread.join();
        } catch (InterruptedException ie) {
            System.err.println("Error: " + ie.getLocalizedMessage());
        }
    }
}
