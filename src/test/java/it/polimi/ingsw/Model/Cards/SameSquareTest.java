package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SameSquareTest {


    SameSquare test;
    Player owner;
    Square target, target2, target3;
    Board board;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board1.json");
        owner = new Player(true,"red","Luciano");
        test = new SameSquare();
    }

    @Test
    public void sameSquareOneTarget(){

        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            target = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,owner));
    }

    @Test
    public void notSameSquareOneTarget(){
        try {
            owner.setPosition(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,owner));
    }

    @Test
    public void sameSquareTwoTarget(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,target2,owner));
    }

    @Test
    public void notSameSquareTwoTarget(){
        try {
            owner.setPosition(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = board.find(2,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,target2,owner));
    }

    @Test
    public void sameSquareThreeTarget(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target3 = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,target2,target3,owner));
    }

    @Test
    public void notSameSquareThreeTarget(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target3 = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,target2,target3,owner));
    }
}