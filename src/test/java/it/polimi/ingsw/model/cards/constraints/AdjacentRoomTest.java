package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test class of AdjacentRoom
 */
class AdjacentRoomTest {
    /**
     * constraint to test
     */
    private AdjacentRoom test;
    /**
     * player that use this constraint
     */
    private Player owner;
    /**
     * board of the game
     */
    private Board board;
    /**
     * parameters of weapon's targets
     */
    private TargetParameter target;
    /**
     * previous targets of the weapon
     */
    private ArrayList<ArrayList<Player>> previousTarget;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setup(){
        board = new Board(null,"/Board/Board1.json");
        owner = new Player(true,"Yellow","Bruno");
        test = new AdjacentRoom(0);
        previousTarget = new ArrayList<>();
        previousTarget.add(new ArrayList<>());
        previousTarget.add(new ArrayList<>());
        target = new TargetParameter(null,owner,null,null,null,null);
    }

    /**
     * test if the owner can shoot at a near room
     */
    @Test
    void nearRoom(){
        try {
            owner.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(board.getRooms().get(0));
        try {
            assertTrue(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can't shoot at a distant room
     */
    @Test
    void notNearRoom(){
        try {
            owner.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(board.getRooms().get(0));
        try {
            assertFalse(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can't shoot in his room
     */
    @Test
    void inMyRoom(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(board.getRooms().get(0));
        try {
            assertFalse(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can't shoot when there isn't a room
     */
    @Test
    void noRoom(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertFalse(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }
}