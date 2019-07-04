package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This class understand the message and request a specific action to the view
 */
public class MessageHandler {
    /**
     * message to show in every moment of the game
     */
    private String toShow;
    /**
     * list of player from the server
     */
    private String[] bigReceive;
    /**
     * reference to the view
     */
    private ViewInterface view;
    /**
     * reference to the Client
     */
    private Client client;
    /**
     * actual state of the game
     */
    private State state;
    /**
     * true if this client is suspended for inactivity
     */
    private boolean suspend = false;
    /**
     * true if the game is finished
     */
    private boolean endGame = false;
    /**
     * constant for the etiquette of the messages to show
     */
    private static final int TO_SHOW_ETIQUETTE = 3;
    /**
     * constant for the etiquette of every message
     */
    private static final int NAME_ETIQUETTE = 4;

    /**
     * represent the different states of the game
     */
    public enum State{
        LOGIN, MENU, WAIT, GAME, END_GAME
    }

    /**
     * Constructor method of MessageHandler
     * @param view the view of the client
     * @param client the client
     */
    public MessageHandler(ViewInterface view, Client client) {
        this.view = view;
        this.client = client;
        this.state = State.LOGIN;
    }

    /**
     * first method to separate the message from the server using the state of the game
     * @param msg the message from the server
     */
    synchronized void understandMessage(String msg){
        if(!suspend) {
            if ((msg.startsWith(">>>"))&&(!endGame)) {
                this.toShow = msg;
                view.showMessage();
            } else {
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
                    case END_GAME:{
                        break;
                    }
                }
            }
        }
    }

    /**
     * understander method of the Login state
     * @param msg the message from the server
     */
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
            case "Match started":{
                this.state = State.GAME;
                break;
            }
            default:
        }
    }

    /**
     * understander method of the Menu state
     * @param msg the message from the server
     */
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
            case "Match started":{
                this.state = State.GAME;
                break;
            }
            default:
        }
    }

    /**
     * method to split the message of the player list
     * @param msg the message from the server
     */
    private void waitUnderstand(String msg){
        if(msg.substring(0,TO_SHOW_ETIQUETTE).equals(":::")){
            bigReceive = msg.substring(NAME_ETIQUETTE).split("-");
            view.waitingRoomView();
        }
        if(msg.equals("Match started")){
            this.state = State.GAME;
        }
    }

    /**
     * understander method of the Game state
     * @param msg the message from the server
     */
    private void gameUnderstand(String msg){
        switch (msg.substring(0,NAME_ETIQUETTE)){
            case "BGD-":{
                view.gameShow(msg.substring(NAME_ETIQUETTE));
                break;
            }
            case "SPW-":{
                view.spawn(msg.substring(NAME_ETIQUETTE));
                break;
            }
            case "END-":{
                view.endTurnShow();
                break;
            }
            case "RLD-":{
                if(!msg.substring(NAME_ETIQUETTE).equals("")){
                    view.reloadShow(msg.substring(NAME_ETIQUETTE));
                }
                else {
                    client.send("RLD-");
                }
                break;
            }
            case "TRG-":{
                view.targetShow(msg.substring(NAME_ETIQUETTE));
                break;
            }
            case "TRW-":{
                view.preShoot(msg.substring(NAME_ETIQUETTE));
                break;
            }
            case "WPN-":{
                view.discardWeapon(msg.substring(NAME_ETIQUETTE));
                break;
            }
            case "OWS-":{
                view.optionalShoot(msg.substring(NAME_ETIQUETTE));
                break;
            }
            case "INS-":{
                view.gameReShow();
                break;
            }
            case "SPD-":{
                suspend = true;
                view.suspend();
                break;
            }
            case "RPU-":{
                view.interruptPowerUp(msg.substring(NAME_ETIQUETTE));
                break;
            }
            case "ENG-":{
                this.endGame=true;
                this.state = State.END_GAME;
                view.winnerShow(msg.substring(NAME_ETIQUETTE));
                break;
            }
            case "FNZ-":{
                view.frenzyShootShow();
                break;
            }
            default:
        }
    }

    /**
     * method to split the menu parameters
     * @param msg the message from the server
     * @return the list of setting to show
     */
    private ArrayList<String> stringToArrayList(String msg){
        ArrayList<String> data = new ArrayList<>();
        Collections.addAll(data, msg.substring(1, msg.length() - 1).split(","));
        return data;
    }

    /**
     * Getter method of State
     * @return the state of the game
     */
    public State getState() {
        return state;
    }

    /**
     * getter method of Big Receive
     * @return return the list of String
     */
    public String[] getBigReceive() {
        return bigReceive;
    }

    /**
     * getter method of ToShow
     * @return the message to show
     */
    public String getToShow() {
        return toShow;
    }

    /**
     * setter method of suspended
     * @param suspend true if the player is suspended
     */
    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }
}
