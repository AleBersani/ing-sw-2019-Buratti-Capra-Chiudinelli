package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.cards.constraints.AdjacentRoom;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test class of effect vs room
 */
class EffectVsRoomTest {

    /**
     * effect to test
     */
    private EffectVsRoom test;
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
    void setup(){
        owner = new Player(true, "blue", "Franco");
        enemy = new Player(true, "green", "Lucio");
        enemy2 = new Player(true, "red", "Fabio");
        enemy3 = new Player(true, "yellow", "Gino");
        board = new Board(null, "/Board/Board1.json");
        AdjacentRoom adjacentRoom = new AdjacentRoom(0);
        ArrayList<Constraint> constraints = new ArrayList<>(Collections.singletonList(adjacentRoom));
        ArrayList<Boolean> constrainPositivity = new ArrayList<>(Collections.singletonList(true));
        target = new TargetParameter(null, owner, null, null, null,null);
        test = new EffectVsRoom(0,0,0,"Vulcanizzatore", constraints, constrainPositivity,1,0);

    }

    /**
     * test if this effect can be applied on a room adjacent to the player position
     */
    @Test
    void apply() {
        board.getRooms().get(0).getSquares().get(0).arrives(enemy);
        enemy.setPosition(board.getRooms().get(0).getSquares().get(0));
        board.getRooms().get(0).getSquares().get(1).arrives(enemy2);
        enemy2.setPosition(board.getRooms().get(0).getSquares().get(1));
        board.getRooms().get(0).getSquares().get(2).arrives(enemy3);
        enemy3.setPosition(board.getRooms().get(0).getSquares().get(2));
        target.setTargetRoom(board.getRooms().get(0));

        try {
            board.find(1,2).arrives(owner);
            owner.setPosition(board.find(1,2));
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
        } catch (InvalidTargetException | NoOwnerException invalidTargetException) {
            invalidTargetException.printStackTrace();
        }
        assertEquals(1, enemy.getDamageCounter());
        assertEquals(target.getOwner(),enemy.getDamage().get(0));
        assertEquals(1, enemy2.getDamageCounter());
        assertEquals(target.getOwner(),enemy2.getDamage().get(0));
        assertEquals(1, enemy3.getDamageCounter());
        assertEquals(target.getOwner(),enemy3.getDamage().get(0));
    }

    /**
     * test if this effect can't be applied on a distant room from the player position
     */
    @Test
    void applyNotNearRoom() {

        board.getRooms().get(0).getSquares().get(0).arrives(enemy);
        enemy.setPosition(board.getRooms().get(0).getSquares().get(0));
        board.getRooms().get(0).getSquares().get(1).arrives(enemy2);
        enemy2.setPosition(board.getRooms().get(0).getSquares().get(1));
        board.getRooms().get(0).getSquares().get(2).arrives(enemy3);
        enemy3.setPosition(board.getRooms().get(0).getSquares().get(2));
        target.setTargetRoom(board.getRooms().get(0));

        try {
            board.find(2,2).arrives(owner);
            owner.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->test.apply(target,null));
    }
}