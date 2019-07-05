package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is composed by tests of Match class
 */
class MatchTest {

    /**
     * This attributes are the players
     */
    private Player guest,nest,best,test,loser;

    /**
     * This attributes are the turns
     */
    private Turn turn,turn2;

    /**
     * This attributes are array lists of player
     */
    private ArrayList<Player> playerList,killShotTrack;

    /**
     * This attributes are different matches
     */
    private Match matchTest,matchTest2;

    /**
     * This attributes are array lists of boolean
     */
    private ArrayList<Boolean> doubleOnKillshotTrack;

    /**
     * Builder method of the parameters needed for every tests of Match test class
     */
    @BeforeEach
    void setup(){
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
        killShotTrack = new ArrayList<>(Arrays.asList(guest,test,loser,test));
        doubleOnKillshotTrack= new ArrayList<>(Arrays.asList(true,false,true,false));
    }

    /**
     * Test the various end/start turn of every player in game
     */
    @Test
    void testStartTurn(){
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

    /**
     * Test the frenzy setter for different match where the frenzy is enable or not
     */
    @Test
    void testSetFrenzy(){
        assertFalse(matchTest.setFrenzy());
        assertTrue(matchTest2.setFrenzy());
    }

    /**
     * Test the end game of the match, in this case if win a player who doesn't make a kill
     */
    @Test
    void testEndGame(){
        guest.setPoints(0);
        nest.setPoints(17);
        best.setPoints(20);
        test.setPoints(10);
        loser.setPoints(12);
        matchTest.setTurn(turn);
        turn.setMatch(matchTest);
        matchTest.setKillShotTrack(killShotTrack);
        matchTest.setDoubleOnKillShotTrack(doubleOnKillshotTrack);
        matchTest.endGame();
        assertEquals(8,guest.getPoints());
        assertEquals(17,nest.getPoints());
        assertEquals(20,best.getPoints());
        assertEquals(16,test.getPoints());
        assertEquals(16,loser.getPoints());
    }

    /**
     * Test the end game of the match, in this case if win two players which don't make a kill and they have the same points
     */
    @Test
    void testEndGame2(){
        guest.setPoints(0);
        nest.setPoints(17);
        best.setPoints(17);
        test.setPoints(10);
        loser.setPoints(12);
        matchTest.setTurn(turn);
        turn.setMatch(matchTest);
        matchTest.setKillShotTrack(killShotTrack);
        matchTest.setDoubleOnKillShotTrack(doubleOnKillshotTrack);
        matchTest.endGame();
        assertEquals(8,guest.getPoints());
        assertEquals(16,test.getPoints());
        assertEquals(16,loser.getPoints());
    }

    /**
     * Test the end game of the match, in this case if win a player who make at least a kill
     */
    @Test
    void testEndGame3(){
        guest.setPoints(0);
        nest.setPoints(15);
        best.setPoints(11);
        test.setPoints(10);
        loser.setPoints(13);
        matchTest.setTurn(turn);
        turn.setMatch(matchTest);
        matchTest.setKillShotTrack(killShotTrack);
        matchTest.setDoubleOnKillShotTrack(doubleOnKillshotTrack);
        matchTest.endGame();
        assertEquals(8,guest.getPoints());
        assertEquals(15,nest.getPoints());
        assertEquals(11,best.getPoints());
        assertEquals(16,test.getPoints());
        assertEquals(17,loser.getPoints());
    }

    /**
     * Test the end game of the match, in this case if win a player who make a kill and he has the same point of another one
     */
    @Test
    void testEndGame4(){
        guest.setPoints(0);
        nest.setPoints(15);
        best.setPoints(11);
        test.setPoints(10);
        loser.setPoints(12);
        matchTest.setTurn(turn);
        turn.setMatch(matchTest);
        matchTest.setKillShotTrack(killShotTrack);
        matchTest.setDoubleOnKillShotTrack(doubleOnKillshotTrack);
        matchTest.endGame();
        assertEquals(8,guest.getPoints());
        assertEquals(15,nest.getPoints());
        assertEquals(11,best.getPoints());
        assertEquals(16,test.getPoints());
        assertEquals(16,loser.getPoints());
    }
}