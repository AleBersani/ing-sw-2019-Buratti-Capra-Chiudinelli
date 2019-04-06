package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Map.Board;

import java.util.ArrayList;

public class Match {

    private ArrayList<Player> players= new ArrayList<Player>();
    private int numPlayers,skulls;
    private ArrayList<Player> killShotTrack= new ArrayList<Player>();
    private boolean frenzyEn;
    private String mode;
    private Turn turn;
    private Board board;

    public Match(ArrayList<Player> players, int numPlayers, int skulls, boolean frenzyEn, String mode) {
        this.players = players;
        this.numPlayers = numPlayers;
        this.skulls = skulls;
        this.frenzyEn = frenzyEn;
        this.mode = mode;
    }

    public void start(){

        return;
    }

    public void setFrenzy(){

        return;
    }

    public void endGame(){

        return;
    }

    public Board getBoard() {
        return board;
    }
}
