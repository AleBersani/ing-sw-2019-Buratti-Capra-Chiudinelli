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

class MinimumDistanceTest {

    Player owner;
    Player enemy, enemy2;
    Board board;
    MinimumDistance test, test1, test2;
    TargetParameter target;
    ArrayList<Player> previousTarget;

    @BeforeEach
    public void setup() {
        owner = new Player(true,"blue", "Franco");
        enemy = new Player(false,"red", "Fabiano");
        enemy2 = new Player(false,"green", "Fazzio");
        board = new Board(null,"./resources/Board/Board1.json");
        target = new TargetParameter(null,owner,null,null,null,null);
        test = new MinimumDistance(2,false);
        test1 = new MinimumDistance(3,false);
        test2 = new MinimumDistance(2,true);
        previousTarget = new ArrayList<Player>();

    }

    @Test
    public void nearEnemyMinimum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(false, test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void distantEnemyMinimum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(true, test.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void distantEnemyMaximum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(false, test1.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nearEnemyMaximum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(true, test1.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nearConcatenateMaximum(){
        try {
            enemy.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        try {
            assertTrue(test2.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void distantConcatenateMaximum(){
        try {
            enemy.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        try {
            assertFalse(test2.canShoot(target,false,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void nearConcatenateMinimum(){
        try {
            enemy.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        try {
            assertFalse(test2.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void distantConcatenateMinimum(){
        try {
            enemy.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemy2.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        try {
            assertTrue(test2.canShoot(target,true,previousTarget));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }
}