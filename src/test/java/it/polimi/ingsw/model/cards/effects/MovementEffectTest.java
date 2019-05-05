package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MovementEffectTest {

    MovementEffect test, test2;
    Player enemy;
    Player owner;
    TargetParameter target;
    Board board;
    ArrayList<Constraint> constraints;
    ArrayList<Boolean> constrainPositivity;

    @BeforeEach
    public void setup() {
        enemy = new Player(true, "green", "Lucio");
        board = new Board(null, "./resources/Board/Board1.json");
        constraints = new ArrayList<Constraint>();
        constrainPositivity = new ArrayList<Boolean>();
        target = new TargetParameter(null, owner, enemy, null, null,null);
        test = new MovementEffect(0,0,0,"Lablo",constraints,constrainPositivity,3,true,false,false,false);
        test2 = new MovementEffect(0,0,0,"Lablo",constraints,constrainPositivity,2,false,false,false,false);
    }

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
        } catch (InvalidTargetException invalidTargetExcepion) {
            invalidTargetExcepion.printStackTrace();
        }
        assertEquals(enemy,target.getMovement().getOnMe().get(0));
    }

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
        } catch (InvalidTargetException invalidTargetExcepion) {
            invalidTargetExcepion.printStackTrace();
        }
        assertEquals(enemy,target.getMovement().getOnMe().get(0));
    }

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