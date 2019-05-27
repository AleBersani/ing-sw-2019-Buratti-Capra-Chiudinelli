package it.polimi.ingsw.communication.server;

import it.polimi.ingsw.controller.Controller;

import java.util.Timer;
import java.util.TimerTask;

public class Gestor implements Runnable {

    private Timer timer= new Timer();
    private int timerDuration;
    private boolean timerIsRunning;
    private Controller controller;

    public Gestor(Controller controller, int timer) {
        this.controller=controller;
        this.timerDuration=timer;
    }

    @Override
    public void run() {
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

    private void startGame() {
        this.timerIsRunning = false;
        controller.startGame();
    }

    private void resetTimer() {
        //TODO
        timer.cancel();
    }

    private void startTimer() {
        timer.schedule(wrap(this::startGame),timerDuration*(long)1000);
    }
    private static TimerTask wrap(Runnable r){
        return new TimerTask() {
            @Override
            public void run() {
                r.run();
            }
        };
    }
}
