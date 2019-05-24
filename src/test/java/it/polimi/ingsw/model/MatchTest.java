package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    Player guest,nest,best,test,loser;
    Turn turn,turn2;
    ArrayList<Player> playerList,killShotTrack;
    Match matchTest,matchTest2;

    @BeforeEach
    public void setup(){
        guest = new Player(true,"blue", "Franco");
        test = new Player(false,"red", "Marina");
        loser = new Player(false,"yellow", "Paola");
        nest = new Player(false,"blue", "Matteo");
        best = new Player(false,"blue", "Luca");
        playerList = new ArrayList<>(Arrays.asList(guest,test,loser,nest,best));
        turn = new Turn(false,null,matchTest);
        turn2 = new Turn(false,null,matchTest2);
        matchTest = new Match(playerList,5,4,false,"normal", "/Board/Board1.json");
        matchTest2 = new Match(playerList,3,0,true,"normal", "/Board/Board1.json");
        killShotTrack = new ArrayList<>(Arrays.asList(guest,guest,test,loser,loser,test));

    }

    @Test
    public void testStartTurn(){
        matchTest.startTurn();
        assertEquals(guest,matchTest.getTurn().getCurrent());
        matchTest.startTurn();
        assertEquals(test,matchTest.getTurn().getCurrent());
        matchTest.startTurn();
        assertEquals(loser,matchTest.getTurn().getCurrent());
        matchTest.startTurn();
        assertEquals(nest,matchTest.getTurn().getCurrent());
        matchTest.startTurn();
        assertEquals(best,matchTest.getTurn().getCurrent());
        matchTest.startTurn();
        assertEquals(guest,matchTest.getTurn().getCurrent());
    }

    @Test
    public void testSetFrenzy(){
        assertFalse(matchTest.setFrenzy());
        assertTrue(matchTest2.setFrenzy());
    }

    // WIN A PLAYER WHO DOESN'T MAKE A SINGLE KILL
    @Test
    public void testEndGame(){
        guest.setPoints(0);
        nest.setPoints(17);
        best.setPoints(20);
        test.setPoints(10);
        loser.setPoints(12);
        matchTest.setTurn(turn);
        matchTest.setKillShotTrack(killShotTrack);
        matchTest.endGame();
        assertEquals(8,guest.getPoints());
        assertEquals(17,nest.getPoints());
        assertEquals(20,best.getPoints());
        assertEquals(16,test.getPoints());
        assertEquals(16,loser.getPoints());
    }

    // WIN 2 PLAYERS WHICH DON'T MAKE A SINGLE KILL AND HAVE THE SAME POINTS
    @Test
    public void testEndGame2(){
        guest.setPoints(0);
        nest.setPoints(17);
        best.setPoints(17);
        test.setPoints(10);
        loser.setPoints(12);
        matchTest.setTurn(turn);
        matchTest.setKillShotTrack(killShotTrack);
        matchTest.endGame();
        assertEquals(8,guest.getPoints());
        assertEquals(16,test.getPoints());
        assertEquals(16,loser.getPoints());
    }

    // WIN A PLAYERS WHO MAKE AT LEAST A KILL
    @Test
    public void testEndGame3(){
        guest.setPoints(0);
        nest.setPoints(15);
        best.setPoints(11);
        test.setPoints(10);
        loser.setPoints(13);
        matchTest.setTurn(turn);
        matchTest.setKillShotTrack(killShotTrack);
        matchTest.endGame();
        assertEquals(8,guest.getPoints());
        assertEquals(15,nest.getPoints());
        assertEquals(11,best.getPoints());
        assertEquals(16,test.getPoints());
        assertEquals(17,loser.getPoints());
    }

    // WIN A PLAYERS WHO MAKE AT LEAST A KILL AND HAVE THE SAME POINT OF ANOTHER ONE
    @Test
    public void testEndGame4(){
        guest.setPoints(0);
        nest.setPoints(15);
        best.setPoints(11);
        test.setPoints(10);
        loser.setPoints(12);
        matchTest.setTurn(turn);
        matchTest.setKillShotTrack(killShotTrack);
        matchTest.endGame();
        assertEquals(8,guest.getPoints());
        assertEquals(15,nest.getPoints());
        assertEquals(11,best.getPoints());
        assertEquals(16,test.getPoints());
        assertEquals(16,loser.getPoints());
    }
}