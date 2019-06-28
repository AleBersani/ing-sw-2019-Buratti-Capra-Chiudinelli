package it.polimi.ingsw.controller;

import it.polimi.ingsw.communication.server.ClientHandler;
import it.polimi.ingsw.model.Match;

public class ClientInfo {
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
    }

    ClientInfo(ClientHandler clientHandler, State state) {
        this.clientHandler = clientHandler;
        this.state = state;
    }

    protected enum State{
        LOGIN, WAIT, GAME, SPAWN, LAY_WEAPON, END, TARGETING, OPTIONAL_WEAPON_SHOOTING
    }


    void setState(State state){
        this.state=state;
    }


}
