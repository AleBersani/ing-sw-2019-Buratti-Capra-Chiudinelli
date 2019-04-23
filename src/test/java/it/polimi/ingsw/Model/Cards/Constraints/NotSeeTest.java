package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotSeeTest {

    NotSee test;
    Player owner;
    Square enemySquare, enemySquare2, enemySquare3;
    Board board;
    TargetParameter target;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        owner = new Player(true,"red","Luciano");
        test = new NotSee();
        target = new TargetParameter(null,owner,null,null,null);
    }

    @Test
    public void canSee(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.getConstraintSquareList().add(enemySquare);
        assertEquals(false, test.canShoot(target));
    }

    @Test
    public void canNotSee(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(4,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.getConstraintSquareList().add(enemySquare);
        assertEquals(true, test.canShoot(target));
    }

    @Test
    public void canSeeAll(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare2 = board.find(3,1);
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

    @Test
    public void canNotSeeOneOfThem(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare2 = board.find(3,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare3 = board.find(3,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.getConstraintSquareList().add(enemySquare);
        target.getConstraintSquareList().add(enemySquare2);
        target.getConstraintSquareList().add(enemySquare3);
        assertEquals(false, test.canShoot(target));
    }
}