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

class SameSquareTest {

    SameSquare test;
    Player owner;
    Square enemySquare;
    Board board;
    TargetParameter target;
    ArrayList<Player> previousTarget;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        owner = new Player(true,"red","Luciano");
        test = new SameSquare();
        target = new TargetParameter(null,owner,null,null,null,null);
        previousTarget = new ArrayList<Player>();
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
        assertEquals(true, test.canShoot(target,true,previousTarget));
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
        assertEquals(false, test.canShoot(target,true,previousTarget));
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
        assertEquals(true, test.canShoot(target,false,previousTarget));
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
        assertEquals(false, test.canShoot(target,false,previousTarget));
    }
}