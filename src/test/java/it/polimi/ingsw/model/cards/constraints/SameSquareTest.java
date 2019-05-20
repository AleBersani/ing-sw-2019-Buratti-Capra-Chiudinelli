package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SameSquareTest {

    SameSquare test,test2;
    Player owner,enemy,enemy2,enemy3;
    Square enemySquare;
    Board board;
    TargetParameter target;
    ArrayList<ArrayList<Player>> previousTarget;

    @BeforeEach
    public void setup(){
        board = new Board(null,"/Board/Board1.json");
        owner = new Player(true,"red","Luciano");
        enemy = new Player(false,"blue", "Fabiano");
        enemy2 = new Player(false,"red", "Fabiolo");
        enemy3 = new Player(false,"red", "Fagiolo");
        test = new SameSquare(false,0);
        test2= new SameSquare(true,0);
        target = new TargetParameter(null,owner,null,null,null,null, null);
        previousTarget = new ArrayList<ArrayList<Player>>();
        previousTarget.add(new ArrayList<Player>());
        previousTarget.add(new ArrayList<Player>());
    }

    @Test
    public void sameSquareOneTarget(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        try {
            assertTrue(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notSameSquareOneTarget(){
        try {
            owner.setPosition(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        try {
            assertFalse(test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notSameSquareNotCase(){
        try {
            owner.setPosition(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        try {
            assertTrue(test.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sameSquareNotCase(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setConstraintSquare(enemySquare);
        try {
            assertFalse(test.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notSameSquareNotCaseEnemy(){
        try {
            enemy.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test2.getLevel()).add(enemy2);
        previousTarget.get(test2.getLevel()).add(enemy3);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertTrue(test2.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sameSquareNotCaseEnemy(){
        try {
            enemy.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test2.getLevel()).add(enemy2);
        previousTarget.get(test2.getLevel()).add(enemy3);
        target.setConstraintSquare(enemy.getPosition());
        try {
            assertFalse(test2.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void noOwner(){
        try {
            enemy.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy3.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.get(test2.getLevel()).add(enemy2);
        previousTarget.get(test2.getLevel()).add(enemy3);
        target.setConstraintSquare(enemy.getPosition());
        assertThrows(NoOwnerException.class,()->test.canShoot(target,false,previousTarget));
    }
}