package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SameDirectionTest {
    SameDirection test;
    Player enemy;
    Square enemySquare;
    Player owner;
    Board board;
    TargetParameter target;
    ArrayList<Player> previousTarget;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        enemy = new Player(true,"blue", "Fabiano");
        owner = new Player(true,"red", "Fabiolo");
        test = new SameDirection();
        target = new TargetParameter(null,owner,null,null,null);
        previousTarget = new ArrayList<Player>();
    }

    @Test
    public void sameDirectionOneTarget(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        assertEquals(true, test.canShoot(target,true,previousTarget));
    }

    @Test
    public void notSameDirectionOneTarget(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        assertEquals(false,test.canShoot(target,true,previousTarget));
    }

    @Test
    public void sameDirectionTwoTarget(){
        try {
            owner.setPosition(board.find(3,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        target.setConstraintSquare(enemySquare);
        assertEquals(true, test.canShoot(target,true,previousTarget));
    }

    @Test
    public void notSameDirectionTwoTarget(){
        try {
            owner.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        target.setConstraintSquare(enemySquare);
        assertEquals(false,test.canShoot(target,true,previousTarget));
    }

    @Test
    public void SameDirectionButNotSameVerseTwoTarget(){
        try {
            owner.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(1,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        target.setConstraintSquare(enemySquare);
        assertEquals(false,test.canShoot(target,true,previousTarget));
    }

}