package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    Player guest,test,loser;
    Turn turn,turn2;
    ArrayList<Player> playerList,killShotTrack;
    Match matchTest,matchTest2;

    @BeforeEach
    public void setup(){
        guest = new Player(true,"blue", "Franco");
        test = new Player(true,"red", "France");
        loser = new Player(false,"yellow", "Paola");
        playerList = new ArrayList<>(Arrays.asList(guest,test,loser));
        turn = new Turn(null,false,null,matchTest);
        turn2 = new Turn(null,false,null,matchTest2);
        matchTest = new Match(playerList,3,4,false,"normal");
        matchTest2 = new Match(playerList,3,0,true,"normal");
        killShotTrack = new ArrayList<>(Arrays.asList(guest,guest,test,loser,loser,test));

    }

    @Test
    public void testStartTurn(){
        matchTest.setTurn(turn);
        matchTest.startTurn();
        assertEquals(guest,matchTest.getTurn().getCurrent());
        matchTest.startTurn();
        assertEquals(test,matchTest.getTurn().getCurrent());
        matchTest.startTurn();
        assertEquals(loser,matchTest.getTurn().getCurrent());
        matchTest.startTurn();
        assertEquals(guest,matchTest.getTurn().getCurrent());
    }

    @Test
    public void testSetFrenzy(){
        assertFalse(matchTest.setFrenzy());
        assertTrue(matchTest2.setFrenzy());
    }

    @Test
    public void testEndGame(){
        matchTest.setTurn(turn);
        matchTest.setKillShotTrack(killShotTrack);
    }
}