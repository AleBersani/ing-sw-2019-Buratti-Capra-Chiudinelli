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
        board = new Board(null, "/Board/Board1.json");
        constraints = new ArrayList<Constraint>();
        constrainPositivity = new ArrayList<Boolean>();
        target = new TargetParameter(null, owner, null, null, null,null);
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
            target.setConstraintSquare(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.apply(target,null);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
    }
}