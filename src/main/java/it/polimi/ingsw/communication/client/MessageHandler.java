package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler {
    private String toSend, toShow;
    private ArrayList<String> slowSend;
    private String[] bigReceive;
    private ViewInterface view;
    private Client client;
    private ExecutorService pool;
    private Timer timer;

    private static final int startFirstEtiquette = 0, endFirstEtiquette = 3, timerDuration = 2, instantTimerResponse = 1, nameEtiquette = 4;

    //TODO aggiungere un modo per capire che una stringa va spezzata

    public MessageHandler(ViewInterface view, Client client) {
        this.view = view;
        this.slowSend = new ArrayList<>();
        this.client = client;
        pool = Executors.newCachedThreadPool();
    }

    public void setToSend(String toSend) {
        this.toSend = toSend;

    }

    public String[] getBigReceive() {
        return bigReceive;
    }

    public String getToShow() {
        return toShow;
    }

    public void setReceive(String receive) {
        this.toShow = receive;
    }

    public void slowSendAdd(String msg){
        this.slowSend.add(msg);
    }

    public String correctToSend(){
        if(this.slowSend.isEmpty()){
            return toSend;
        }
        String sendable = slowSend.get(0);
        this.slowSend.remove(0);
        if(this.slowSend.isEmpty()){
            client.setGo(false);
        }
        return sendable;
    }

    public void understandMessage(String msg){
        switch(msg){
            case "Insert a command:": {

                break;
            }
            case ("Select a board"): {
                view.boardSettingView();
                break;
            }
            case "Select the number of skulls": {

                break;
            }
            case "Do you like to play with frenzy? Y/N": {

                break;
            }
            case "Now you are in the waiting room": {
                view.waitingRoomView();
                timer= new Timer();
                timer.schedule(wrap(this::update),timerDuration*(long)1000);
                break;
            }
            case "Initialize board":{
                timer= new Timer();
                timer.schedule(wrap(this::update),instantTimerResponse*(long)10);
                //TODO sistemare
            }
            default: {

            }
        }
    }

    public void understandReceived(String msg){
        if(msg == null){
            client.setToStop(true);
            view.stopView();
        }
        else{
            switch (msg.substring(startFirstEtiquette,endFirstEtiquette)){
                case ">>>":{
                    this.toShow = msg;
                    view.showMessage();
                    break;
                }
                case "§§§":{
                    bigReceive = msg.substring(nameEtiquette).split("-");
                    break;
                }
                case "+++":{
                    view.dataShow(msg.substring(3));
                    break;
                }
                default:

            }
        }
    }

    public void update(){
        this.toSend = "Ok";
        client.setWaiting(false);
        this.timer.cancel();
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
