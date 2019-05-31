package it.polimi.ingsw.communication.client;

import java.util.Timer;
import java.util.TimerTask;

public class TimersHandler implements Runnable {
    private Timer timer;
    private int timerDuration;
    private Client client;
    private MessageHandler messageHandler;

    public TimersHandler(int timerDuration, Client client, MessageHandler messageHandler) {
        this.timerDuration = timerDuration;
        this.client = client;
        this.messageHandler = messageHandler;
    }

    public void update(){
        messageHandler.setToSend("Ok");
        synchronized (client){
            client.notify();
        }
    }

    @Override
    public void run() {
        timer= new Timer();
        timer.schedule(wrap(this::update),timerDuration*(long)1000);
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
