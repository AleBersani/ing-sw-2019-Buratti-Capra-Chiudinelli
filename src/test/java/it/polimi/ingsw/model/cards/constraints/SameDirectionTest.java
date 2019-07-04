package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test class of SameDirection
 */
class SameDirectionTest {
    /**
     * constraint to test
     */
    private SameDirection test;
    /**
     * enemy of the owner
     */
    private Player enemy;
    /**
     * enemy square of the owner
     */
    private Square enemySquare;
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
        enemy = new Player(false,"blue", "Fabiano");
        owner = new Player(true,"red", "Fabiolo");
        test = new SameDirection(0);
        target = new TargetParameter(null,owner,null,null,null, null);
        previousTarget = new ArrayList<>();
        previousTarget.add(new ArrayList<>());
        previousTarget.add(new ArrayList<>());
    }

    /**
     * test if one target is on the direction of the owner
     */
    @Test
    void sameDirectionOneTarget(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        try {
            assertTrue(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if one target isn't on the direction of the owner
     */
    @Test
    void notSameDirectionOneTarget(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        try {
            assertFalse(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if two targets are on the same direction of the owner
     */
    @Test
    void sameDirectionTwoTarget(){
        try {
            owner.setPosition(board.find(3,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test.getLevel()).add(enemy);
        target.setConstraintSquare(enemySquare);
        try {
            assertTrue(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if two targets aren't on the same direction of the owner
     */
    @Test
    void notSameDirectionTwoTarget(){
        try {
            owner.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test.getLevel()).add(enemy);
        target.setConstraintSquare(enemySquare);
        try {
            assertFalse(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if two targets are on the same direction of the owner, but they aren't on the same verse
     */
    @Test
    void SameDirectionButNotSameVerseTwoTarget(){
        try {
            owner.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(1,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test.getLevel()).add(enemy);
        target.setConstraintSquare(enemySquare);
        try {
            assertFalse(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }
}