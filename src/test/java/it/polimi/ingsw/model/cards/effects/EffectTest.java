package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.constraints.*;
import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test if multiple constraint can be applied to a single effect
 */
class EffectTest {
    /**
     * a constraint to test
     */
    private See see;
    /**
     * a constraint to test
     */
    private See see2;
    /**
     * a constraint to test
     */
    private SameSquare sameSquare;
    /**
     * a constraint to test
     */
    private SameSquare sameSquare2;
    /**
     * a constraint to test
     */
    private SamePlayer samePlayer;
    /**
     * a constraint to test
     */
    private SameDirection sameDirection;
    /**
     * a constraint to test
     */
    private MinimumDistance minimumDistanceConcatenate;
    /**
     * a constraint to test
     */
    private MinimumDistance minimumDistanceConcatenate2;
    /**
     * a constraint to test
     */
    private MinimumDistance minimumDistance2;
    /**
     * a constraint to test
     */
    private MinimumDistance minimumDistance3;
    /**
     * a constraint to test
     */
    private AdjacentRoom adjacentRoom;
    /**
     * owner of the weapon to test
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
     * enemy of the owner
     */
    private Player enemy3;
    /**
     * parameters of the target
     */
    private TargetParameter target;
    /**
     * board of the game
     */
    private Board board;
    /**
     * previous targets of the weapon
     */
    private ArrayList<ArrayList<Player>> previousTarget;
    /**
     * effect to test
     */
    private Effect effect;
    /**
     * list of constraints to test
     */
    private ArrayList<Constraint> constraints;
    /**
     * list of the behavior of the constraints to test
     */
    private ArrayList<Boolean> constrainPositivity;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setup(){
        see = new See(false,0);
        see2 = new See(true,0);
        sameSquare = new SameSquare(false,0);
        sameSquare2 = new SameSquare(true,0);
        samePlayer = new SamePlayer(0);
        sameDirection = new SameDirection(0);
        minimumDistanceConcatenate = new MinimumDistance(1,true,0);
        minimumDistanceConcatenate2 = new MinimumDistance(2,true,0);
        minimumDistance2 = new MinimumDistance(2,false,0);
        minimumDistance3 = new MinimumDistance(3,false,0);
        adjacentRoom = new AdjacentRoom(0);
        board = new Board(null,"/Board/Board1.json");
        owner = new Player(true,"blue", "Bellocchio");
        enemy = new Player(false, "green", "Lucio");
        enemy2 = new Player(false, "red", "Fabio");
        enemy3 = new Player(false, "yellow", "Ciccio");
        target = new TargetParameter(null,owner,null,null,null,null);
        previousTarget = new ArrayList<>();
        previousTarget.add(new ArrayList<>());
        previousTarget.add(new ArrayList<>());
        constraints = new ArrayList<>();
        constrainPositivity = new ArrayList<>();
        effect = new Effect(0,0,0,"elio",constraints,constrainPositivity) {
            @Override
            public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) {

            }

            @Override
            protected void constraintSquareGenerator(TargetParameter targetParameter) {

            }
        };
    }

    /**
     * test if the owner can shoot at a near room
     */
    @Test
    void adjacentRoomCheck(){
        try {
            owner.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(board.getRooms().get(0));
        constraints.add(adjacentRoom);
        constrainPositivity.add(true);
        previousTarget.get(0).add(enemy);
        try {
            assertTrue(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException | InvalidTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if a minimum and maximum distance constraints ca be applied on the same effect
     */
    @Test
    void doubleDistanceCheck() {
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
        constraints.add(minimumDistance2);
        constrainPositivity.add(true);
        constraints.add(minimumDistance3);
        constrainPositivity.add(false);
        target.setEnemyPlayer(enemy);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertTrue(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException | InvalidTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if a minimum and maximum distance constraints ca be applied on the same effect, when the enemy is distant from the owner
     */
    @Test
    void doubleDistanceTooLongDistanceCheck() {
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        constraints.add(minimumDistance2);
        constrainPositivity.add(true);
        constraints.add(minimumDistance3);
        constrainPositivity.add(false);
        target.setEnemyPlayer(enemy);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertFalse(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException | InvalidTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if a minimum and maximum  concatenate distance constraints ca be applied on the same effect
     */
    @Test
    void doubleDistanceConcatenateCheck() {
        try {
            enemy2.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        constraints.add(minimumDistanceConcatenate);
        constrainPositivity.add(true);
        constraints.add(minimumDistanceConcatenate2);
        constrainPositivity.add(false);
        previousTarget.get(0).add(enemy2);
        previousTarget.get(0).add(enemy3);
        target.setEnemyPlayer(enemy);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertTrue(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException | InvalidTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * test if a series of constraints can be applied on the same effect
     */
    @Test
    void directionCheck() {
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        constraints.add(sameDirection);
        constrainPositivity.add(true);
        constraints.add(see);
        constrainPositivity.add(true);
        constraints.add(see2);
        constrainPositivity.add(true);
        constraints.add(samePlayer);
        constrainPositivity.add(false);
        constraints.add(sameSquare);
        constrainPositivity.add(true);
        constraints.add(sameSquare2);
        constrainPositivity.add(false);
        previousTarget.get(0).add(enemy2);
        previousTarget.get(0).add(enemy3);
        target.setEnemyPlayer(enemy);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertTrue(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException | InvalidTargetException e) {
            e.printStackTrace();
        }
    }
}