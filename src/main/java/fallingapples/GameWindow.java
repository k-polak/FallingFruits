package fallingapples;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameWindow {
    public static final int width = 400;
    public static final int height = 500 ;

    Stage stage;
    private Scene gameView, menuView;
    private Menu menu;
    private Game game;

    Thrower thrower;

    public GameWindow(Stage stage){
        this.stage = stage;
        menu = new Menu(this);
        game = new Game(this);
    }

    public void build(){
        System.out.println("1");

        menu.build();
        game.build();
        menuView = new Scene(menu.getRoot(),width,height);
        gameView = new Scene(game.getRoot(),width,height);

        menuView.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        gameView.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(gameView);
        game.start();
        stage.show();
    }

    public void handleQuitButton(){
        Platform.exit();
        System.exit(0);
    }

    public void handleResetButton(){
        game.restart();
        stage.setScene(gameView);
    }

    public void handleGameOver(int score){
        menu.setScore(score);
        stage.setScene(menuView);
    }
}
