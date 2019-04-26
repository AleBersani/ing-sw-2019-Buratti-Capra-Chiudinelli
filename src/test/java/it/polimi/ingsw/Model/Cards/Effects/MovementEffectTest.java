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

import static org.junit.jupiter.api.Assertions.*;

class MovementEffectTest {

    MovementEffect test, test2;
    Player enemy;
    Player owner;
    TargetParameter target;
    Board board;
    ArrayList<Constraint> constraints;

    @BeforeEach
    public void setup() {
        enemy = new Player(true, "green", "Lucio");
        board = new Board(null, "./resources/Board/Board1.json");
        constraints = new ArrayList<Constraint>();
        target = new TargetParameter(null, owner, enemy, null, null);
        test = new MovementEffect(0,0,0,"Lablo",constraints,3,true);
        test2 = new MovementEffect(0,0,0,"Lablo",constraints,2,false);
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
            test.apply(target);
        } catch (InvalidTargetException invalidTargetExcepion) {
            invalidTargetExcepion.printStackTrace();
        }
        assertTrue(target.getMovement().getOnMe().contains(enemy));
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
            test2.apply(target);
        } catch (InvalidTargetException invalidTargetExcepion) {
            invalidTargetExcepion.printStackTrace();
        }
        assertTrue(target.getMovement().getOnMe().contains(enemy));
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
        assertThrows(InvalidTargetException.class,()->test.apply(target));
    }
}