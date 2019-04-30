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

class EffectVsPlayerTest {

    EffectVsPlayer test;
    Player enemy;
    Player owner;
    TargetParameter target;
    Board board;
    ArrayList<Constraint> constraints;
    ArrayList<Boolean> constrainPositivity;

    @BeforeEach
    void  setup(){
        owner = new Player(true, "blue", "Franco");
        enemy = new Player(true, "green", "Lucio");
        board = new Board(null, "./resources/Board/Board1.json");
        constraints = new ArrayList<Constraint>();
        constrainPositivity = new ArrayList<Boolean>();
        target = new TargetParameter(null, owner, null, null, null);
        test = new EffectVsPlayer(0,0,0,"lumbro",constraints,constrainPositivity,1,0,false,false);
    }

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
            target.getConstraintSquareList().add(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.apply(target,null);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
    }
}