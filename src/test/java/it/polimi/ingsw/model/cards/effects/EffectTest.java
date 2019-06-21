package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.constraints.*;
import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EffectTest {

    See see,see2;
    SameSquare sameSquare,sameSquare2;
    SamePlayer samePlayer;
    SameDirection sameDirection;
    MinimumDistance minimumDistanceConcatenate,minimumDistanceConcatenate2,minimumDistance2,minimumDistance3;
    AdjacentRoom adjacentRoom;
    Player owner,enemy,enemy2,enemy3;
    Board board;
    TargetParameter target;
    ArrayList<ArrayList<Player>> previousTarget;
    Effect effect;
    ArrayList<Constraint> constraints;
    ArrayList<Boolean> constrainPositivity;

    @BeforeEach
    void setup(){
        see = new See(false,0);
        see2 = new See(true,0);
        sameSquare = new SameSquare(false,0);
        sameSquare2 = new SameSquare(true,0);
        samePlayer = new SamePlayer(0);
        sameDirection = new SameDirection(0);
        minimumDistanceConcatenate = new MinimumDistance(1,true,0);
        minimumDistanceConcatenate2 = new MinimumDistance(2,true,0);
        minimumDistance2 = new MinimumDistance(2,false,0);
        minimumDistance3 = new MinimumDistance(3,false,0);
        adjacentRoom = new AdjacentRoom(0);
        board = new Board(null,"/Board/Board1.json");
        owner = new Player(true,"blue", "Bellocchio");
        enemy = new Player(false, "green", "Lucio");
        enemy2 = new Player(false, "red", "Fabio");
        enemy3 = new Player(false, "yellow", "Ciccio");
        target = new TargetParameter(null,owner,null,null,null,null);
        previousTarget = new ArrayList<ArrayList<Player>>();
        previousTarget.add(new ArrayList<Player>());
        previousTarget.add(new ArrayList<Player>());
        constraints = new ArrayList<Constraint>();
        constrainPositivity = new ArrayList<Boolean>();
        effect = new Effect(0,0,0,"elio",constraints,constrainPositivity) {
            @Override
            public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException {

            }

            @Override
            protected void constraintSquareGenerator(TargetParameter targetParameter) {

            }
        };
    }

    @Test
    void adjacentRoomCheck(){
        try {
            owner.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(board.getRooms().get(0));
        constraints.add(adjacentRoom);
        constrainPositivity.add(true);
        previousTarget.get(0).add(enemy);
        try {
            assertTrue(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void doubleDistanceCheck() {
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        constraints.add(minimumDistance2);
        constrainPositivity.add(true);
        constraints.add(minimumDistance3);
        constrainPositivity.add(false);
        target.setEnemyPlayer(enemy);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertTrue(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void doubleDistanceTooLongDistanceCheck() {
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        constraints.add(minimumDistance2);
        constrainPositivity.add(true);
        constraints.add(minimumDistance3);
        constrainPositivity.add(false);
        target.setEnemyPlayer(enemy);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertFalse(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void doubleDistanceConcatenateCheck() {
        try {
            enemy2.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        constraints.add(minimumDistanceConcatenate);
        constrainPositivity.add(true);
        constraints.add(minimumDistanceConcatenate2);
        constrainPositivity.add(false);
        previousTarget.get(0).add(enemy2);
        previousTarget.get(0).add(enemy3);
        target.setEnemyPlayer(enemy);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertTrue(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    void directionCheck() {
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        constraints.add(sameDirection);
        constrainPositivity.add(true);
        constraints.add(see);
        constrainPositivity.add(true);
        constraints.add(see2);
        constrainPositivity.add(true);
        constraints.add(samePlayer);
        constrainPositivity.add(false);
        constraints.add(sameSquare);
        constrainPositivity.add(true);
        constraints.add(sameSquare2);
        constrainPositivity.add(false);
        previousTarget.get(0).add(enemy2);
        previousTarget.get(0).add(enemy3);
        target.setEnemyPlayer(enemy);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertTrue(effect.constraintsCheck(target,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }
}