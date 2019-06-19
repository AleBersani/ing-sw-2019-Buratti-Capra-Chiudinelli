package it.polimi.ingsw.controller;

import it.polimi.ingsw.communication.server.ClientHandler;

public class ClientInfo {
    ClientHandler clientHandler;
    State state;

    ClientInfo(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.state = State.LOGIN;
    }

    ClientInfo(ClientHandler clientHandler, State state) {
        this.clientHandler = clientHandler;
        this.state = state;
    }

    protected enum State{
        LOGIN, WAIT, GAME, SPAWN, LAY_WEAPON, END
    }


    void setState(State state){
        this.state=state;
    }


}
