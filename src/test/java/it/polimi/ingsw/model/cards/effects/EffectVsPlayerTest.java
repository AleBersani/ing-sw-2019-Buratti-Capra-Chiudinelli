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
 * test class of effect vs room
 */
class EffectVsPlayerTest {

    /**
     * effect to test
     */
    private EffectVsPlayer test;
    /**
     * enemy of the owner
     */
    private Player enemy;
    /**
     * owner of the weapon to test
     */
    private Player owner;
    /**
     * parameters of the target
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
    void  setup(){
        owner = new Player(true, "blue", "Franco");
        enemy = new Player(true, "green", "Lucio");
        board = new Board(null, "/Board/Board1.json");
        ArrayList<Constraint> constraints = new ArrayList<>();
        ArrayList<Boolean> constrainPositivity = new ArrayList<>();
        target = new TargetParameter(null, owner, null, null, null,null);
        test = new EffectVsPlayer(0,0,0,"lumbro", constraints, constrainPositivity,1,0,false,false);
    }

    /**
     * test if this effect can be applied on an enemy
     */
    @Test
    void apply() {
        try {
            owner.setPosition(board.find(1,1));
            board.find(1,1).arrives(owner);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,1));
            board.find(2,1).arrives(enemy);
            target.setEnemyPlayer(enemy);
            target.setConstraintSquare(enemy.getPosition());
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
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
    }
}