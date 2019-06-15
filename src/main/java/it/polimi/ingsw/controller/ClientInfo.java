package it.polimi.ingsw.controller;

import it.polimi.ingsw.communication.server.ClientHandler;

public class ClientInfo {
    protected ClientHandler clientHandler;
    protected State state;

    public ClientInfo(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        this.state = State.LOGIN;
    }

    public ClientInfo(ClientHandler clientHandler, State state) {
        this.clientHandler = clientHandler;
        this.state = state;
    }

    protected enum State{
        LOGIN, WAIT, GAME, SPAWN
    }

    public void nextState(){
        switch (this.state){
            case LOGIN:{
                this.state = State.WAIT;
                break;
            }
            case WAIT:{
                this.state = State.GAME;
                break;
            }
            case GAME:{
                this.state = State.SPAWN;
                break;
            }
            case SPAWN:{
                this.state = State.GAME;
            }

        }
    }

}
