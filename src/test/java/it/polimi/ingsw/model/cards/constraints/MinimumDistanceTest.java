package it.polimi.ingsw.model.cards.constraints;

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
    Board board;
    Square enemySquare;
    MinimumDistance test, test1;
    TargetParameter target;
    ArrayList<Player> previousTarget;

    @BeforeEach
    public void setup() {
        owner = new Player(true,"blue", "Franco");
        board = new Board(null,"./resources/Board/Board1.json");
        target = new TargetParameter(null,owner,null,null,null,null);
        test = new MinimumDistance(2);
        test1 = new MinimumDistance(3);
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
            enemySquare = board.find(2,1);
            target.setConstraintSquare(enemySquare);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,true,previousTarget));
    }

    @Test
    public void distantEnemyMinimum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
            target.setConstraintSquare(enemySquare);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,true,previousTarget));
    }

    @Test
    public void distantEnemyMaximum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
            target.setConstraintSquare(enemySquare);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test1.canShoot(target,false,previousTarget));
    }

    @Test
    public void nearEnemyMaximum(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,2);
            target.setConstraintSquare(enemySquare);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test1.canShoot(target,false,previousTarget));
    }

}