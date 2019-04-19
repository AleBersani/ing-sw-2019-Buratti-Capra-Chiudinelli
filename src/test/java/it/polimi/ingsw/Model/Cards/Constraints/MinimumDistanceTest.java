package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MinimumDistanceTest {

    Player owner;
    Board board;
    Square enemySquare;
    MinimumDistance test;
    TargetParameter target;

    @BeforeEach
    public void setup() {
        owner = new Player(true,"blue", "Franco");
        board = new Board(null,"./resources/Board/Board1.json");
        target = new TargetParameter(null,owner,null,null,null);
        test = new MinimumDistance(2);

    }

    @Test
    public void testCanNotShoot(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(2,1);
            target.getConstraintSquareList().add(enemySquare);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target));
    }

    @Test
    public void CanShoot(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,2);
            target.getConstraintSquareList().add(enemySquare);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target));
    }
}