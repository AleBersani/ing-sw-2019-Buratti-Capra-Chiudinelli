package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.gui.GUI;

import java.util.ArrayList;

public class MessageHandler {
    private String toSend, toShow;
    private ArrayList<String> slowSend, bigReceive;
    private ViewInterface view;
    private Client client;

    //TODO aggiungere un modo per capire che una stringa va spezzata

    public MessageHandler(ViewInterface view, Client client) {
        this.view = view;
        this.slowSend = new ArrayList<>();
        this.client = client;
    }

    public void setToSend(String toSend) {
        this.toSend = toSend;

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
                break;
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
            if(msg.startsWith(">>>")){
                this.toShow = msg;
                view.showMessage();
            }
        }
    }
}
