package it.polimi.ingsw;

import it.polimi.ingsw.Map.Board;

import java.util.ArrayList;

public class Match {

    private ArrayList<Player> players= new ArrayList<Player>();
    private int numPlayers,skulls;
    private ArrayList<Player> killShotTrack= new ArrayList<Player>();
    private boolean frenzyEn;
    private String mode;
    private Turn turn;
    private Board board;

    public Match(ArrayList<Player> players, int numPlayers, int skulls, ArrayList<Player> killShotTrack, boolean frenzyEn, String mode, Turn turn, Board board) {
        this.players = players;
        this.numPlayers = numPlayers;
        this.skulls = skulls;
        this.killShotTrack = killShotTrack;
        this.frenzyEn = frenzyEn;
        this.mode = mode;
        this.turn = turn;
        this.board = board;
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
}
