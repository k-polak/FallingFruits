package fallingapples;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Menu {
    private StackPane root;
    private Label finalScore;
    private Button quitButton,restartButton;
    private GameWindow gameWindow;

    public Menu(GameWindow gameWindow){
        this.gameWindow = gameWindow;
    }

    public void build(){
        root = new StackPane();
        quitButton = new Button("");
        restartButton = new Button("");
        finalScore = new Label("TEST");
        buildMenu();
    }

    private void buildMenu(){
        HBox hbox = new HBox();
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(80,0,0,0));

        finalScore.setFont(new Font("Arial", 50));
        finalScore.setId("finalScore");



        restartButton.setId("restartButton");
        quitButton.setId("quitButton");
        root.setId("menuBackground");

        restartButton.setOnMouseClicked(e ->{
            gameWindow.handleResetButton();
        });

        quitButton.setOnMouseClicked(e ->{
            gameWindow.handleQuitButton();
        });

        hbox.getChildren().addAll(quitButton,restartButton);
        vbox.getChildren().addAll(finalScore,hbox);

        root.getChildren().addAll(vbox);
    }

    public void setScore(int score){
        finalScore.setText("Score: "+score);
    }

    public StackPane getRoot(){
        return root;
    }
}
