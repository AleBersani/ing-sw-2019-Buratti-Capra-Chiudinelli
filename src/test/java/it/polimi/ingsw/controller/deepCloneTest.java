package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class deepCloneTest {

    private Player player;
    private Player player2;
    private ArrayList<Player> players;
    private Match match;
    private Match clone;

    @BeforeEach
    void setup(){
        player= new Player(true,"red", "Franco");
        player2= new Player(false, "blue","Giorgio");
        players= new ArrayList<>();
        players.add(player);
        players.add(player2);
        match= new Match(players,2,3,true,"base", "/Board/Board1.json");
        clone=null;

    }


    @Test
    void deepClone() {
        try {
            clone= Controller.deepClone(match);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        match.getPlayers().get(0).setColor("purple");
        assertNotEquals(match.getPlayers().get(0).getColor(),clone.getPlayers().get(0).getColor());

    }
}