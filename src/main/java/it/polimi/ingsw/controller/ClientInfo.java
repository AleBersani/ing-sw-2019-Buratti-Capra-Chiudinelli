package it.polimi.ingsw.controller;

import it.polimi.ingsw.communication.server.ClientHandler;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.cards.Weapon;

public class ClientInfo {
    Weapon weapon;
    Match simulation;
    String shootingOptionals;
    Integer optionalWeaponShooting;
    ClientHandler clientHandler;
    State state;
    boolean suspended;

    ClientInfo(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.state = State.LOGIN;
        this.shootingOptionals="";
        this.suspended=false;
        weapon=null;
    }

    void suspend(){
        this.suspended=true;
        this.clientHandler.getController().numberCheck();
    }

    protected enum State{
        LOGIN, WAIT, GAME, SPAWN, LAY_WEAPON, END, TARGETING, OPTIONAL_WEAPON_SHOOTING, RESPONSE_PU, END_GAME, SHOOTING_FRENZY
    }


    void setState(State state){
        this.state=state;
    }


}
