package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test class of movement effect
 */
class MovementEffectTest {

    /**
     * movement effect to test
     */
    private MovementEffect test;
    /**
     * movement effect to test
     */
    private MovementEffect test2;
    /**
     * enemy of the owner
     */
    private Player enemy;
    /**
     * parameters of weapon's targets
     */
    private TargetParameter target;
    /**
     * board of the game
     */
    private Board board;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setup() {
        Player owner = new Player(true, "red", "Luciano");
        enemy = new Player(true, "green", "Lucio");
        board = new Board(null, "/Board/Board1.json");
        ArrayList<Constraint> constraints = new ArrayList<>();
        ArrayList<Boolean> constrainPositivity = new ArrayList<>();
        target = new TargetParameter(null, owner, enemy, null, null,null);
        test = new MovementEffect(0,0,0,"Lablo", constraints, constrainPositivity,3,true,false,false,false);
        test2 = new MovementEffect(0,0,0,"Lablo", constraints, constrainPositivity,2,false,false,false,false);
    }

    /**
     * this test verify if the enemy is moved in a direction
     */
    @Test
    void applyLinear() {
        try {
            enemy.setPosition(board.find(1,1));
            board.find(1,1).arrives(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setMovement(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.apply(target,null);
        } catch (InvalidTargetException | NoOwnerException invalidTargetExcepion) {
            invalidTargetExcepion.printStackTrace();
        }
        assertEquals(enemy,target.getMovement().getOnMe().get(0));
    }

    /**
     * this test verify if the enemy isn't moved in a direction
     */
    @Test
    void applyNotLinearEffect(){
        try {
            enemy.setPosition(board.find(1,1));
            board.find(1,1).arrives(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setMovement(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test2.apply(target,null);
        } catch (InvalidTargetException | NoOwnerException invalidTargetExcepion) {
            invalidTargetExcepion.printStackTrace();
        }
        assertEquals(enemy,target.getMovement().getOnMe().get(0));
    }

    /**
     * this test verify if the enemy isn't moved in a direction where there is a wall
     */
    @Test
    void applyLinearNotLinear(){
        try {
            enemy.setPosition(board.find(2,1));
            board.find(1,1).arrives(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setMovement(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->test.apply(target,null));
    }
}