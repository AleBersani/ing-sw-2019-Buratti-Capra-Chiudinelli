package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This class is composed by tests of Turn class
 */
class TurnTest {

    /**
     * This attributes are the players
     */
    private Player test,loser,guest;

    /**
     * This attribute is turn
     */
    private Turn turn;

    /**
     * This attributes are the array lists of player
     */
    private ArrayList<Player> damageList,damageList2,damageList3,playerList;

    /**
     * This attribute is match
     */
    private Match testMatch;

    /**
     * This attribute is the board
     */
    private Board board;

    /**
     * Builder method of the parameters needed for every tests of Turn test class
     */
    @BeforeEach
    void setup(){
        guest = new Player(true,"blue", "Franco");
        test = new Player(true,"red", "France");
        loser = new Player(false,"yellow", "Paola");
        playerList = new ArrayList<>(Arrays.asList(guest,test,loser));
        testMatch = new Match(playerList,3,5,true,"normal", "/Board/Board1.json");
        turn = new Turn(false,test,testMatch);
        board = new Board(testMatch, "/Board/Board1.json");
        damageList = new ArrayList<>(Arrays.asList(test,guest,guest,guest,guest,guest,guest,test,test,test,test));
        damageList2 = new ArrayList<>(Arrays.asList(guest,guest,guest,guest,guest,guest,guest,test,test,test,test));
        damageList3 = new ArrayList<>(Arrays.asList(test,loser,loser,loser,test,test,test,test,test,test,test,test));
    }

    /**
     * Test if the players are added to the dead list
     */
    @Test
    void testAddDead() {
        turn.addDead(loser);
        assertEquals(1,turn.getDeads().size());
        turn.addDead(guest);
        assertEquals(2,turn.getDeads().size());
    }

    /**
     * Test the right behaviour of the end turn phase, control if the all map is full of ammo tile and weapon
     */
    @Test
    void testEndTurn() {
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        turn.endTurn();
        for(int i=0;i<this.turn.getMatch().getBoard().getRooms().size();i++) {
            for (int j = 0; j < this.turn.getMatch().getBoard().getRooms().get(i).getSquares().size(); j++) {
                assertFalse(this.turn.getMatch().getBoard().getRooms().get(i).getSquares().get(j).require());
            }
        }
    }

    /**
     * Test the right calculation of points when kill a player who is never dead
     */
    @Test
    void testSetPoint() {
        loser.setDamage(damageList);
        assertEquals(11,loser.getDamage().size());
        turn.addDead(loser);
        turn.setPoint();
        assertEquals(8,guest.getPoints());
        assertEquals(7,test.getPoints());
        assertEquals(1,loser.getSkull());
        assertEquals(0,turn.getDeads().size());
        assertEquals(0,loser.getDamage().size());
    }

    /**
     * Test the right calculation of points when kill a player who is already dead one time
     */
    @Test
    void testSetPoint2() {
        testSetPoint();
        loser.setDamage(damageList2);
        assertEquals(11,loser.getDamage().size());
        turn.addDead(loser);
        turn.setPoint();
        assertEquals(15,guest.getPoints());
        assertEquals(11,test.getPoints());
        assertEquals(2,loser.getSkull());
        assertEquals(0,turn.getDeads().size());
        assertEquals(0,loser.getDamage().size());
    }

    /**
     * Test the right calculation of points when make a double kill too
     */
    @Test
    void testSetPoint3() {
        testSetPoint();
        loser.setDamage(damageList2);
        guest.setDamage(damageList3);
        assertEquals(12,guest.getDamage().size());
        turn.addDead(loser);
        turn.addDead(guest);
        turn.setPoint();
        assertEquals(15,guest.getPoints());
        assertEquals(21,test.getPoints());
        assertEquals(6,loser.getPoints());
        assertEquals(2,loser.getSkull());
        assertEquals(1,guest.getSkull());
        assertEquals(0,turn.getDeads().size());
        assertEquals(0,guest.getDamage().size());
    }

    /**
     * Test the kill shot track length
     */
    @Test
    void testSetPoint4(){
        testSetPoint3();
        assertEquals(3,testMatch.getKillShotTrack().size());
    }

    /**
     * Test the right points calculation when the plank of the dead player is turned
     */
    @Test
    void testSetPoint5(){
        testSetPoint();
        loser.setDamage(damageList2);
        assertEquals(11,loser.getDamage().size());
        loser.setTurnedPlank(true);
        loser.setSkull(3);
        turn.addDead(loser);
        turn.setPoint();
        assertEquals(9,guest.getPoints());
        assertEquals(8,test.getPoints());
        assertEquals(4,loser.getSkull());
        assertEquals(0,turn.getDeads().size());
        assertEquals(0,loser.getDamage().size());
    }
}