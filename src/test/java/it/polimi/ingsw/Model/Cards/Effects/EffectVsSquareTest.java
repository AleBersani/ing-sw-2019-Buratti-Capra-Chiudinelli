package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EffectVsSquareTest {

    EffectVsSquare test;
    Player enemy, enemy2, enemy3;
    Player owner;
    TargetParameter target;
    Board board;
    ArrayList<Constraint> constraints;
    ArrayList<Boolean> constrainPositivity;

    @BeforeEach
    public void setup() {
        owner = new Player(true, "blue", "Franco");
        enemy = new Player(true, "green", "Lucio");
        enemy2 = new Player(true, "red", "Fabio");
        enemy3 = new Player(true, "yellow", "Gino");
        board = new Board(null, "./resources/Board/Board1.json");
        constraints = new ArrayList<Constraint>();
        constrainPositivity = new ArrayList<Boolean>();
        target = new TargetParameter(null, owner, null, null, null);
        test = new EffectVsSquare(0,0,0,"explosion",constraints,constrainPositivity,1,0);
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
            test.apply(target,null);
        } catch (InvalidTargetException invalidTargetExcepion) {
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