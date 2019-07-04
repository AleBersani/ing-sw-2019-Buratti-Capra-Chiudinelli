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
 * test class of See
 */
class SeeTest {

    /**
     * constraint to test
     */
    private See test;
    /**
     * constraint to test
     */
    private See test2;
    /**
     * player that use this constraint
     */
    private Player owner;
    /**
     * enemy of the owner
     */
    private Player enemy;
    /**
     * enemy of the owner
     */
    private Player enemy2;
    /**
     * enemy square of the owner
     */
    private Square enemySquare;
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
        owner = new Player(true,"blue", "Bellocchio");
        enemy = new Player(true, "green", "Lucio");
        enemy2 = new Player(true, "red", "Fabio");
        test = new See(false,0);
        test2 = new See(true,0);
        target = new TargetParameter(null,owner,null,null,null, null);
        previousTarget = new ArrayList<>();
        previousTarget.add(new ArrayList<>());
        previousTarget.add(new ArrayList<>());
    }

    /**
     * test if the owner can see the enemy
     */
    @Test
    void testCanSee(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,1);
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
     * test if the owner can't see the enemy
     */
    @Test
    void testCanNotSee(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(4,3);
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
     * test if the owner can't see the enemy
     */
    @Test
    void testCanNotSeeNotCase(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(4,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        try {
            assertTrue(test.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can see the enemy
     */
    @Test
    void testCanSeeNotCase(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        try {
            assertFalse(test.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can see a previous target that can see the enemy
     */
    @Test
    void canSeeConcatenate(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        target.setConstraintSquare(enemySquare);
        try {
            assertTrue(test2.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can't see a previous target that can see the enemy
     */
    @Test
    void canNotSeeConcatenateNotCase(){
        try {
            owner.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(4,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        target.setConstraintSquare(enemySquare);
        try {
            assertTrue(test2.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can see a previous target that can't see the enemy
     */
    @Test
    void canNotSeeOnePreviousConcatenate(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        target.setConstraintSquare(enemySquare);
        try {
            assertFalse(test2.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can see a previous target that can see the enemy
     */
    @Test
    void canSeeLastTargetConcatenateNotCase(){
        try {
            owner.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        target.setConstraintSquare(enemySquare);
        try {
            assertFalse(test2.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void noOwnerConcatenate(){
        try {
            owner.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(4,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        target.setConstraintSquare(enemySquare);
        target.setOwner(null);
        assertThrows(NoOwnerException.class,()-> test2.canShoot(target,false,previousTarget));
    }
}