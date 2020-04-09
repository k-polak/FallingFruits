/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fallingapples;

import javafx.application.Platform;

/**
 *
 * @author Krzysiek
 */
public class Thrower implements Runnable {

    Game game;

    public Thrower(Game game) {
        this.game = game;

    }

    @Override
    public void run() {
        while (game.life > 0) {
            new Thread() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            game.throwFruit();
                        }
                    });
                }
            }.start();
            int delay = getDelay();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    private int getDelay(){
        if(game.counter < 90){
            return (int)(1000 - 7.7*game.counter);
        }else{
            return 300;
        }
    }

    public void end() throws InterruptedException {
        Thread.currentThread().join();
    }
}
