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
 *
 */
class EffectVsSquareTest {
    /**
     * effect to test
     */
    private EffectVsSquare test;
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
     * board of the game
     */
    private Board board;
    /**
     * parameters of the target
     */
    private TargetParameter target;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setup() {
        owner = new Player(true, "blue", "Franco");
        enemy = new Player(true, "green", "Lucio");
        enemy2 = new Player(true, "red", "Fabio");
        enemy3 = new Player(true, "yellow", "Gino");
        board = new Board(null, "/Board/Board1.json");
        ArrayList<Constraint> constraints = new ArrayList<>();
        ArrayList<Boolean> constrainPositivity = new ArrayList<>();
        target = new TargetParameter(null, owner, null, null, null, null);
        test = new EffectVsSquare(0,0,0,"explosion", constraints, constrainPositivity,1,0);
    }


    @Test
    void simpleApply() {
        try {
            enemy.setPosition(board.find(1,1));
            board.find(1,1).arrives(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(1,1));
            board.find(1,1).arrives(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(1,1));
            board.find(1,1).arrives(enemy3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            owner.setPosition(board.find(1,1));
            board.find(1,1).arrives(owner);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setTargetSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.apply(target,new ArrayList<ArrayList<Player>>(){
                {
                    add(new ArrayList<>());
                    add(new ArrayList<>());
                    add(new ArrayList<>());
                }
            });
        } catch (InvalidTargetException | NoOwnerException invalidTargetExcepion) {
            invalidTargetExcepion.printStackTrace();
        }
        assertEquals(1, enemy.getDamageCounter());
        assertEquals(target.getOwner(),enemy.getDamage().get(0));
        assertEquals(1, enemy2.getDamageCounter());
        assertEquals(target.getOwner(),enemy2.getDamage().get(0));
        assertEquals(1, enemy3.getDamageCounter());
        assertEquals(target.getOwner(),enemy3.getDamage().get(0));
        assertTrue(owner.getDamage().isEmpty());
        assertEquals(0, owner.getDamageCounter());
    }
}