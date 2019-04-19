package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SameSquareTest {


    SameSquare test;
    Player owner;
    Square enemySquare, enemySquare2, enemySquare3;
    Board board;
    TargetParameter target;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        owner = new Player(true,"red","Luciano");
        test = new SameSquare();
        target = new TargetParameter(null,owner,null,null,null);
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
        target.getConstraintSquareList().add(enemySquare);
        assertEquals(true, test.canShoot(target));
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
        target.getConstraintSquareList().add(enemySquare);
        assertEquals(false, test.canShoot(target));
    }

    @Test
    public void sameSquareTwoTarget(){
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
        try {
            enemySquare2 = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.getConstraintSquareList().add(enemySquare);
        target.getConstraintSquareList().add(enemySquare2);
        assertEquals(true, test.canShoot(target));
    }

    @Test
    public void notSameSquareTwoTarget(){
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
        try {
            enemySquare2 = board.find(2,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.getConstraintSquareList().add(enemySquare);
        target.getConstraintSquareList().add(enemySquare2);
        assertEquals(false, test.canShoot(target));
    }

    @Test
    public void sameSquareThreeTarget(){
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
        try {
            enemySquare2 = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare3 = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.getConstraintSquareList().add(enemySquare);
        target.getConstraintSquareList().add(enemySquare2);
        target.getConstraintSquareList().add(enemySquare3);
        assertEquals(true, test.canShoot(target));
    }

    @Test
    public void notSameSquareThreeTarget(){
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
        try {
            enemySquare2 = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare3 = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.getConstraintSquareList().add(enemySquare);
        target.getConstraintSquareList().add(enemySquare2);
        target.getConstraintSquareList().add(enemySquare3);
        assertEquals(false, test.canShoot(target));
    }
}