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

class SeeTest {
    /*
    Player owner;
    Square enemySquare,enemySquare2,enemySquare3;
    Board board;
    See test;
    ArrayList<Square> targets;
    TargetParameter target;
    ArrayList<Player> previousTarget;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        owner = new Player(true,"red", "Bellocchio");
        test = new See();
        target = new TargetParameter(null,owner,null,null,null);
        previousTarget = new ArrayList<Player>();
    }

    @Test
    public void testCanSee(){
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
        assertEquals(true, test.canShoot(target,true,previousTarget));
    }

    @Test
    public void testCanNotSee(){
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
        assertEquals(false, test.canShoot(target,true,previousTarget));
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
        assertEquals(true, test.canShoot(target,true,previousTarget));
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
        assertEquals(false, test.canShoot(target,true,previousTarget));
    }
    */
}