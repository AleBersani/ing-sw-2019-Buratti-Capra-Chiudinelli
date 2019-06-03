package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler {
    private String toSend, toShow;
    private ArrayList<String> slowSend;
    private String[] bigReceive;
    private ViewInterface view;
    private Client client;
    private TimersHandler timerUpdate;
    private ExecutorService pool;

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
                timerUpdate = new TimersHandler(3,client,this);
                pool.submit(timerUpdate);
                break;
            }
            case "Initialize board":{
                timerUpdate=new TimersHandler(0,client,this);
                pool.submit(timerUpdate);
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
            String stringo = msg.substring(0,3);
            switch (stringo){
                case ">>>":{
                    this.toShow = msg;
                    view.showMessage();
                    break;
                }
                case "§§§":{
                    String befDivid = msg.substring(4);
                    bigReceive = befDivid.split("-");

                    break;
                }
                default:

            }
        }
    }
}
