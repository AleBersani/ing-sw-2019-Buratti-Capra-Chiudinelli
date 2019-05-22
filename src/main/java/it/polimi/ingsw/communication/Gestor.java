package it.polimi.ingsw.communication;

import it.polimi.ingsw.controller.Controller;

import java.util.Timer;
import java.util.TimerTask;

public class Gestor implements Runnable {

    private Timer timer= new Timer();
    private int timerDuration;
    private boolean timerIsGoing;
    private Controller controller;

    public Gestor(Controller controller, int timer) {
        this.controller=controller;
        this.timerDuration=timer;
    }

    @Override
    public void run() {

        timerIsGoing=false;
        while (true){
            if(controller.getNicknameList().size()==3 && !timerIsGoing){
                startTimer();
                timerIsGoing=true;
            }
            if (controller.getNicknameList().size()<3){
                resetTimer();
            }
            if(controller.getNicknameList().size()==5){
                timer.cancel();
                startGame();
                break;
            }

        }

    }

    private void startGame() {
        controller.startGame();
    }

    private void resetTimer() {
        //TODO
        timer.cancel();
        timerIsGoing=false;
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
