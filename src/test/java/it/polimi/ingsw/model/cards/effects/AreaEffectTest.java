package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AreaEffectTest {

    AreaEffect testDistance2;
    Player enemy,enemy2,enemy3,enemy4;
    Player owner;
    TargetParameter target;
    Board board;
    ArrayList<Constraint> constraints;
    ArrayList<Boolean> constrainPositivity;

    @BeforeEach
    void  setup(){
        owner = new Player(true, "blue", "Franco");
        enemy = new Player(true, "green", "Lucio");
        enemy2 = new Player(true, "red", "Fabio");
        enemy3 = new Player(true, "yellow", "Gino");
        enemy4 = new Player(true, "gray", "Lino");
        board = new Board(null, "./resources/Board/Board1.json");
        constraints = new ArrayList<Constraint>();
        constrainPositivity = new ArrayList<Boolean>();
        target = new TargetParameter(null, owner, null, null, null,null, null);
        testDistance2 = new AreaEffect(0,0,0,"lumbro",constraints,constrainPositivity,1,0,2);
    }

    @Test
    void applyDistance2() {
        try {
            owner.setPosition(board.find(2,2));
            board.find(2,2).arrives(owner);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,3));
            board.find(2,3).arrives(enemy);
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
            enemy3.setPosition(board.find(2,2));
            board.find(2,2).arrives(enemy3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy4.setPosition(board.find(2,1));
            board.find(2,1).arrives(enemy4);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            testDistance2.apply(target,null);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1, enemy.getDamageCounter());
        assertEquals(target.getOwner(),enemy.getDamage().get(0));
        assertEquals(1, enemy2.getDamageCounter());
        assertEquals(target.getOwner(),enemy2.getDamage().get(0));
        assertEquals(0, enemy3.getDamageCounter());
        assertTrue(enemy3.getDamage().isEmpty());
        assertEquals(0, enemy4.getDamageCounter());
        assertTrue(enemy4.getDamage().isEmpty());
        assertEquals(0, owner.getDamageCounter());
        assertTrue(owner.getDamage().isEmpty());
    }
}