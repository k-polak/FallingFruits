package fallingapples;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.WindowEvent;

/**
 *
 * @author Krzysiek
 */
public class FallingApples extends Application {

    final int width = 400;
    final int height = 500;
    int counter = 0;
    int life = 3;

    Rectangle bucket;
    Pane root;
    Label score;
    List<ImageView> hearts = new ArrayList();
    Stage primaryStage;
    Thrower thrower;
    Thread throwersThread;
    Pane paneForApples;
    PathTransition pathTransition;
    EventHandler handler;

    @Override
    public void start(Stage stage) throws  AWTException {
        primaryStage = stage;
        score = new Label();
        score.setFont(new Font("Arial", 20));
        score.setLayoutX(10);
        score.setLayoutY(10);
        Robot robot = new Robot();
        bucket = new Rectangle(0, height - 50, 77, 50);
        bucket.setId("bucket");
        Image bucketImage = new Image(getClass().getResource("/bucket.png").toExternalForm());
        ImagePattern bucketPattern = new ImagePattern(bucketImage);
        bucket.setFill(bucketPattern);

        paneForApples = new Pane();
        root = new Pane();
        root.getChildren().addAll(paneForApples, bucket, score);
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        handler = (EventHandler<MouseEvent>) (MouseEvent e) -> {
            bucket.setX(e.getX() - 50);
        };
        
        Scene s = new Scene(new Parent() {},10,20);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        scene.addEventHandler(MouseEvent.MOUSE_MOVED, handler);
        scene.setCursor(Cursor.NONE);
        scene.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                double winX = scene.getWindow().getX();
                double winY = scene.getWindow().getY();
                if (x < 0) {
                    robot.mouseMove((int)winX+10,(int)(y+winY+25));
                }
                if(x >= width){
                   robot.mouseMove((int)(scene.getWindow().getX()+width)-10,(int)(y+winY+25));
               }
            }
        });
   

        Image heart = new Image(getClass().getResource("/heart.png").toExternalForm());
        ImageView heartView1 = new ImageView(heart);
        heartView1.setX(width - 10);
        heartView1.setY(0);
        ImageView heartView2 = new ImageView(heart);
        heartView2.setX(width - 30);
        heartView2.setY(0);
        ImageView heartView3 = new ImageView(heart);
        heartView3.setX(width - 50);
        heartView3.setY(0);

        hearts.add(heartView3);
        hearts.add(heartView2);
        hearts.add(heartView1);

        root.getChildren().addAll(heartView1, heartView2, heartView3);
        primaryStage.setTitle("Falling apples");
        primaryStage.setScene(scene);
        
        primaryStage.setOnCloseRequest((WindowEvent t) -> {
            Platform.exit();
            System.exit(0);
        });
        
       // thrower = new Thrower(this);
        throwersThread = new Thread(this.thrower);
        throwersThread.start();
    }

    void restart() {
        counter = 0;
        life = 3;
        try {
            start(primaryStage);
        } catch (Exception e) {
            System.err.println("Error: " + e.getLocalizedMessage());
        }
    }

    public void appleFall() {
        int delta = 0;
        int speed;
        Random random = new Random();
        String fruits[] = {"/apple.png", "/pear.png", "/cherry.png"};
        String evilFruits[] = {"/rotten_apple.png"};
        Image appleImage;

        if(counter > 30){
            delta = random.nextInt(200);
        }
        if(counter < 90){
            speed = 1000 - (int)(8.8*counter)+delta;
        }else{
            speed = 200+delta;
        }


        Fruit fruit = new Fruit((double) (random.nextInt(width - 25) + 25),
                (double) 25, (double) 25, random.nextBoolean());
        if (fruit.isRotten == true) {
            appleImage = new Image(getClass().getResource(evilFruits[random.nextInt(evilFruits.length)]).toExternalForm());
        } else {
            appleImage = new Image(getClass().getResource(fruits[random.nextInt(fruits.length)]).toExternalForm());
        }
        ImagePattern applePattern = new ImagePattern(appleImage);
        fruit.setFill(applePattern);
        fruit.setId("fruit");
        paneForApples.getChildren().add(fruit);
        Path path = new Path();
        path.getElements().add(new MoveTo(fruit.getCenterX(), fruit.getCenterY()));
        path.getElements().add(new LineTo(fruit.getCenterX(), height));
        pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(speed)); //2000
        pathTransition.setPath(path);
        pathTransition.setNode(fruit);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(false);
        pathTransition.onFinishedProperty().set((EventHandler<ActionEvent>) (ActionEvent event) -> {
            if (fruit.getCenterX() > bucket.getX() && fruit.getCenterX() < bucket.getX() + 100) {
                if (fruit.isRotten == true) {
                    life--;
                    if (life >= 0) {
                        hearts.get(0).setVisible(false);
                        hearts.remove(0);
                    }
                    if (life <= 0) {
                        gameOver();
                    }
                } else {
                    counter++;
                    score.setText(String.valueOf(counter));
                }
            } else {
                if (fruit.isRotten == false) {
                    life--;
                    if (life >= 0) {
                        hearts.get(0).setVisible(false);
                        hearts.remove(0);
                    }
                    if (life <= 0) {
                        gameOver();
                    }
                }else{
                    counter++;
                    score.setText(String.valueOf(counter));
                }
            }
            fruit.setVisible(false);

        });
        pathTransition.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void gameOver() {
        pathTransition.stop();
        primaryStage.getScene().removeEventHandler(MouseEvent.MOUSE_MOVED, handler);
        Pane gameOverPane = new Pane();
        gameOverPane.setOpacity(1);
        ColorInput ci = new ColorInput(0, 0, primaryStage.getWidth(), primaryStage.getHeight(), Color.BLACK);
        gameOverPane.setEffect(ci);

        Button restartButton = new Button();
        restartButton.setLayoutX(188);
        restartButton.setLayoutY(200);
        restartButton.setId("restartButton");
        restartButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            restart();
        });

        Button quitButton = new Button();
        quitButton.setLayoutX(72);
        quitButton.setLayoutY(200);
        quitButton.setId("quitButton");
        quitButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            Platform.exit();
            System.exit(0);
        });

        Label finalScore = new Label("Score: " + counter);
        finalScore.setFont(new Font("Arial", 50));
        finalScore.setLayoutX(85);
        finalScore.setLayoutY(140);
        finalScore.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        finalScore.setTextFill(Color.WHITE);
        BoxBlur bb = new BoxBlur();
        bb.setIterations(1);
        bb.setWidth(4);
        bb.setHeight(4);

        finalScore.setEffect(bb);

        root.getChildren().add(gameOverPane);
        root.getChildren().add(restartButton);
        root.getChildren().add(quitButton);
        root.getChildren().add(finalScore);
        primaryStage.getScene().setCursor(Cursor.DEFAULT);
        try {
            throwersThread.join();
        } catch (InterruptedException ie) {
            System.err.println("Error: " + ie.getLocalizedMessage());
        }
    }
}
