package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class deepCloneTest {

    @BeforeEach
    void setup(){

    }


    @Test
    void deepClone() {
        Player player= new Player(true,"red", "Franco");
        Player player2= new Player(false, "blue","Giorgio");
        ArrayList<Player> players= new ArrayList<>();
        players.add(player);
        players.add(player2);
        Match match= new Match(players,2,3,true,"base", "/Board/Board1.json");
        Match clone=null;
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