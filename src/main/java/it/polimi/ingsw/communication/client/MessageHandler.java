package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageHandler {
    private String toShow;
    private String[] bigReceive;
    private ViewInterface view;
    private Client client;
    private State state;
    private static final int TO_SHOW_ETIQUETTE = 3;
    private static final int NAME_ETIQUETTE = 4;

    public enum State{
        LOGIN, MENU, WAIT, GAME
    }

    public MessageHandler(ViewInterface view, Client client) {
        this.view = view;
        this.client = client;
        this.state = State.LOGIN;
    }

    protected synchronized void understandMessage(String msg){
        if(msg.startsWith(">>>")){
            this.toShow = msg;
            view.showMessage();
        }
        else {
            switch (this.state) {
                case LOGIN: {
                    loginUnderstand(msg);
                    break;
                }
                case MENU: {
                    menuUnderstand(msg);
                    break;
                }
                case WAIT: {
                    waitUnderstand(msg);
                    break;
                }
                case GAME: {
                    gameUnderstand(msg);
                    break;
                }
            }
        }
    }

    private void loginUnderstand(String msg){
        switch (msg){
            case "Login":{
                view.loginView();
                break;
            }
            case "Now you are in the waiting room":{
                this.state = State.WAIT;
                view.waitingRoomView();
                break;
            }
            case "setting":{
                this.state = State.MENU;
                break;
            }
            default:
        }
    }

    private void menuUnderstand(String msg){
        String[] stringo = msg.split("-");
        switch (stringo[0]){
            case "Select a board":{
                view.boardSettingView(stringToArrayList(stringo[1].replaceAll(" ", "")),"Board");
                break;
            }
            case "Select the number of skulls":{
                view.boardSettingView(stringToArrayList(stringo[1].replaceAll(" ", "")),"Skull");
                break;
            }
            case "Do you like to play with frenzy? Y/N":{
                view.boardSettingView(new ArrayList<>(Arrays.asList("Y","N")),"Frenzy");
                break;
            }
            case "Now you are in the waiting room":{
                this.state = State.WAIT;
                view.waitingRoomView();
                break;
            }
            default:
        }
    }

    private void waitUnderstand(String msg){
        if(msg.substring(0,TO_SHOW_ETIQUETTE).equals("§§§")){
            bigReceive = msg.substring(NAME_ETIQUETTE).split("-");
            view.waitingRoomView();
        }
        if(msg.equals("Match started")){
            this.state = State.GAME;
        }
    }

    private void gameUnderstand(String msg){
        switch (msg.substring(0,NAME_ETIQUETTE)){
            case "BGD-":{
                view.gameShow(msg.substring(NAME_ETIQUETTE));
                break;
            }
            default:
        }
    }

    private ArrayList<String> stringToArrayList(String msg){
        ArrayList<String> data = new ArrayList<>();
        for(String s: msg.substring(1,msg.length()-1).split(",")){
            data.add(s);
        }
        return data;
    }

    public State getState() {
        return state;
    }

    public String[] getBigReceive() {
        return bigReceive;
    }

    public String getToShow() {
        return toShow;
    }
}
