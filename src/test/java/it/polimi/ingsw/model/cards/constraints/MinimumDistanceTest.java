package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * test class of MinimumDistance
 */
class MinimumDistanceTest {

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
     * board of the game
     */
    private Board board;
    /**
     * constraint to test
     */
    private MinimumDistance test;
    /**
     * constraint to test
     */
    private MinimumDistance test1;
    /**
     * constraint to test
     */
    private MinimumDistance test2;
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
    void setup() {
        owner = new Player(true,"blue", "Franco");
        enemy = new Player(false,"red", "Fabiano");
        enemy2 = new Player(false,"green", "Fazzio");
        board = new Board(null,"/Board/Board1.json");
        target = new TargetParameter(null,owner,null,null,null, null);
        test = new MinimumDistance(2,false,0);
        test1 = new MinimumDistance(3,false,0);
        test2 = new MinimumDistance(2,true,0);
        previousTarget = new ArrayList<>();
        previousTarget.add(new ArrayList<>());
        previousTarget.add(new ArrayList<>());

    }

    /**
     * test if the owner can't shoot at a near enemy
     */
    @Test
    void nearEnemyMinimum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertFalse(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can shoot at a distant enemy
     */
    @Test
    void distantEnemyMinimum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertTrue(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can't shoot at a distant enemy
     */
    @Test
    void distantEnemyMaximum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertFalse(test1.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can shoot at a near enemy
     */
    @Test
    void nearEnemyMaximum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertTrue( test1.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can shoot at a series of near enemy
     */
    @Test
    void nearConcatenateMaximum(){
        try {
            enemy.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test2.getLevel()).add(enemy);
        previousTarget.get(test2.getLevel()).add(enemy2);
        try {
            assertTrue(test2.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can't shoot at a series of distant enemy
     */
    @Test
    void distantConcatenateMaximum(){
        try {
            enemy.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test2.getLevel()).add(enemy);
        previousTarget.get(test2.getLevel()).add(enemy2);
        try {
            assertFalse(test2.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can't shoot at a series of near enemy
     */
    @Test
    void nearConcatenateMinimum(){
        try {
            enemy.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test2.getLevel()).add(enemy);
        previousTarget.get(test2.getLevel()).add(enemy2);
        try {
            assertFalse(test2.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if the owner can shoot at a series of distant enemy
     */
    @Test
    void distantConcatenateMinimum(){
        try {
            enemy.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test2.getLevel()).add(enemy);
        previousTarget.get(test2.getLevel()).add(enemy2);
        try {
            assertTrue(test2.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }
}