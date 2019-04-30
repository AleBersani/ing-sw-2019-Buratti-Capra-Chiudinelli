package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.cards.constraints.MaximumDistance;
import it.polimi.ingsw.model.cards.constraints.NotSameSquare;
import it.polimi.ingsw.model.cards.constraints.SameDirection;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EffectsVsDirectionTest {

    EffectsVsDirection test;
    Player enemy, enemy2,enemy3;
    Player owner;
    TargetParameter target;
    Board board;
    ArrayList<Boolean> constrainPositivity;
    ArrayList<Constraint> constraints;
    ArrayList<Integer> damage, mark;
    SameDirection sameDirection;
    NotSameSquare notSameSquare;
    MaximumDistance maximumDistance;

    @BeforeEach
    public void setup(){
        owner = new Player(true, "blue", "Franco");
        enemy = new Player(true, "green", "Lucio");
        enemy2 = new Player(true, "red", "Fabio");
        enemy3 = new Player(true, "yellow", "Gino");
        board = new Board(null, "./resources/Board/Board1.json");
        sameDirection = new SameDirection();
        notSameSquare = new NotSameSquare();
        maximumDistance = new MaximumDistance(2);
        constrainPositivity = new ArrayList<Boolean>();
        constraints = new ArrayList<Constraint>();
        target = new TargetParameter(null, owner, null, null, null);
        damage = new ArrayList<Integer>(Arrays.asList(2,1));
        mark = new ArrayList<Integer>(Arrays.asList(0,0));
        test = new EffectsVsDirection(0,0,0,"sandro",constraints,constrainPositivity,damage,mark);
    }

    @Test
    void applyVsPlayer(){
        test.getConstraints().add(sameDirection);
        try {
            owner.setPosition(board.find(2,1));
            board.find(2,1).arrives(owner);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,2));
            board.find(2,2).arrives(enemy);
            target.getDirectionPlayer().add(enemy);
            target.getConstraintSquareList().add(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,3));
            board.find(2,3).arrives(enemy2);
            target.getDirectionPlayer().add(enemy2);
            target.getConstraintSquareList().add(enemy2.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.apply(target,null);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        for(Player player: enemy.getDamage()){
            assertEquals(target.getOwner(),player);
        }
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(target.getOwner(),enemy2.getDamage().get(0));
    }

    @Test
    void applyVsSquares() {
        int i;
        test.getConstraints().add(sameDirection);
        test.getConstraints().add(notSameSquare);
        test.getConstraints().add(maximumDistance);
        try {
            owner.setPosition(board.find(2,2));
            board.find(2,2).arrives(owner);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,2));
            enemy2.setPosition(board.find(3,2));
            board.find(3,2).arrives(enemy);
            board.find(3,2).arrives(enemy2);
            target.getDirectionSquare().add(enemy.getPosition());
            target.getConstraintSquareList().add(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(4,2));
            board.find(4,2).arrives(enemy3);
            target.getDirectionSquare().add(enemy3.getPosition());
            target.getConstraintSquareList().add(enemy3.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.apply(target,null);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        }
        for(i=0;i<target.getDirectionSquare().size();i++){
            for(Player player: target.getDirectionSquare().get(i).getOnMe()){
                for(Player owning: player.getDamage()){
                    assertEquals(owner,owning);
                }
                assertEquals(this.damage.get(i),player.getDamageCounter());
            }
        }
    }

    @Test
    void notApply(){
        test.getConstraints().add(sameDirection);
        try {
            owner.setPosition(board.find(2,2));
            board.find(2,2).arrives(owner);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,2));
            board.find(3,2).arrives(enemy);
            target.getDirectionSquare().add(enemy.getPosition());
            target.getConstraintSquareList().add(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(1,2));
            board.find(1,2).arrives(enemy3);
            target.getDirectionSquare().add(enemy3.getPosition());
            target.getConstraintSquareList().add(enemy3.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->test.apply(target,null));
    }
}