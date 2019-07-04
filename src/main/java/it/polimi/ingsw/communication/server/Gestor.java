package it.polimi.ingsw.communication.server;

import it.polimi.ingsw.controller.Controller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used to handle the number of players before the game starts
 */
public class Gestor implements Runnable {

    /**
     * This attribute is the timer that starts when you reach 3 players
     */
    private Timer timer= new Timer();
    /**
     * This attribute is the duration of the timer in seconds
     */
    private int timerDuration;
    /**
     * This attribute is true when the timer is running
     */
    private boolean timerIsRunning;
    /**
     * This attribute is the controller that handle the game
     */
    private Controller controller;

    /**
     * This constructor initialize the gestor
     * @param controller This parameter is the controller that handle the game
     * @param timer This parameter is the duration of the timer in seconds
     */
    Gestor(Controller controller, int timer) {
        this.controller=controller;
        this.timerDuration=timer;
    }

    /**
     * This method is called when the server starts for handling the access request from the clients
     */
    @Override
    public synchronized void run() {
        this.timerIsRunning = true;
        boolean timerIsGoing=false;
        while (timerIsRunning){
            if(controller.getNicknameList().size()==3 && !timerIsGoing){
                timerIsGoing=true;
                this.startTimer();
            }
            if (controller.getNicknameList().size()<3){
                timerIsGoing=false;
                this.resetTimer();
            }
            if(controller.getNicknameList().size()==5){
                timer.cancel();
                this.startGame();
            }

        }

    }

    /**
     * This method is called when the game needs to start
     */
    private void startGame() {
        this.timerIsRunning = false;
        timer.cancel();
        controller.startGame();
    }

    /**
     * This method is called whrn the timer needs to be cancelled
     */
    private void resetTimer() {
        timer.cancel();
    }

    /**
     * This method starts a new timer
     */
    private void startTimer() {
        timer= new Timer();
        timer.schedule(wrap(this::startGame),timerDuration*(long)1000);
    }

    /**
     * This method transform a Runnable in a TimerTask
     * @param r This parameter is the Runnable that needs to be converted
     * @return The TimerTask equivalent to the Runnable
     */
    private static TimerTask wrap(Runnable r){
        return new TimerTask() {
            @Override
            public void run() {
                r.run();
            }
        };
    }
}