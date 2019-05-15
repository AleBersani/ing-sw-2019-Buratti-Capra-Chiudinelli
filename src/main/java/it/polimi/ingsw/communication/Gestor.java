package it.polimi.ingsw.communication;

import java.util.Timer;
import java.util.TimerTask;

import static it.polimi.ingsw.communication.MultiServer.nicknameList;

public class Gestor implements Runnable {

    Timer timer= new Timer();
    int timerDuration;

    boolean timerIsGoing;

    @Override
    public void run() {
        timerIsGoing=false;
        while (true){
            if(nicknameList.size()==3 && !timerIsGoing){
                startTimer();
                timerIsGoing=true;
            }
            if (nicknameList.size()<3){
                resetTimer();
            }
            if(nicknameList.size()==5){
                timer.cancel();
                startGame();
                break;
            }

        }
    }

    private void startGame() {
        //TODO
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
