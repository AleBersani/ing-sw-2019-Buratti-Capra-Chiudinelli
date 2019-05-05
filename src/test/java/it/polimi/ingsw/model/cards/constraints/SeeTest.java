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

    Player owner;
    Player enemy,enemy2;
    Square enemySquare;
    Board board;
    See test;
    ArrayList<Square> targets;
    TargetParameter target;
    ArrayList<Player> previousTarget;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        owner = new Player(true,"blue", "Bellocchio");
        enemy = new Player(true, "green", "Lucio");
        enemy2 = new Player(true, "red", "Fabio");
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
        target.setConstraintSquare(enemySquare);
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
        target.setConstraintSquare(enemySquare);
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
            enemy.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        target.setConstraintSquare(enemySquare);
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
            enemy.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        target.setConstraintSquare(enemySquare);
        assertEquals(false, test.canShoot(target,true,previousTarget));
    }

    @Test
    public void testCanNotSeeNotCase(){
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
        target.setConstraintSquare(enemySquare);
        assertEquals(true, test.canShoot(target,false,previousTarget));
    }

    @Test
    public void testCanSeeNotCase(){
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
        target.setConstraintSquare(enemySquare);
        assertEquals(false, test.canShoot(target,false,previousTarget));
    }

    @Test
    public void canSeeOneOfThemNotCase(){
        try {
            owner.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        target.setConstraintSquare(enemySquare);
        assertEquals(false, test.canShoot(target,false,previousTarget));
    }

    @Test
    public void canNotSeeAllOfThemNotCase(){
        try {
            owner.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy.setPosition(board.find(3,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(1,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        target.setConstraintSquare(enemySquare);
        assertEquals(true, test.canShoot(target,false,previousTarget));
    }
}