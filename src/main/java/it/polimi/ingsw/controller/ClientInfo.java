package it.polimi.ingsw.controller;

import it.polimi.ingsw.communication.server.ClientHandler;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.cards.Weapon;

/**
 * This class contains info of one client
 */
class ClientInfo {
    /**
     * This attribute is the weapon that the client is using
     */
    Weapon weapon;
    /**
     * This attribute is a copy of the match that can be reverted in case of errors
     */
    Match simulation;
    /**
     * This attribute represent the order of optional effect used on a weaponOptional
     */
    String shootingOptionals;
    /**
     * This attribute is the position in the hand of the weaponOptional the  client is using
     */
    Integer optionalWeaponShooting;
    /**
     * This attribute is the ClientHandler of the client
     */
    ClientHandler clientHandler;
    /**
     * This attribute represent in what state of the game the client is
     */
    State state;
    /**
     * This attribute is turned to true when a client is suspended from the game or is disconnected
     */
    boolean suspended;

    /**
     * This constructor initialize the clientInfo
     * @param clientHandler This parameter is the clientHandler of this client
     */
    ClientInfo(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.state = State.LOGIN;
        this.shootingOptionals="";
        this.suspended=false;
        weapon=null;
    }

    /**
     * This method is called when a client is suspended from the game or is disconnected
     */
    void suspend(){
        this.suspended=true;
        this.clientHandler.getController().numberCheck();
    }

    /**
     * This is the enum of all the possible states of the game
     */
    protected enum State{
        LOGIN, WAIT, GAME, SPAWN, LAY_WEAPON, END, TARGETING, OPTIONAL_WEAPON_SHOOTING, RESPONSE_OFFENSIVE_PU, RESPONSE_DEFENSIVE_POWERUP, END_GAME, SHOOTING_FRENZY
    }


    /**
     * This method is used to change the state of the game
     * @param state This parameter is the value that the attribute state will assume
     */
    void setState(State state){
        this.state=state;
    }


}
