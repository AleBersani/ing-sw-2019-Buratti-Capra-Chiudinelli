package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.Constraints.SameSquare;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SameSquareTest {


    SameSquare test;
    Player owner;
    Square target, target2, target3;
    Board board;
    ArrayList<Square> targets;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board1.json");
        owner = new Player(true,"red","Luciano");
        test = new SameSquare();
        targets = new ArrayList<Square>();
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
        targets.add(target);
        assertEquals(true, test.canShoot(targets,owner));
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
        targets.add(target);
        assertEquals(false, test.canShoot(targets,owner));
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
        targets.add(target);
        targets.add(target2);
        assertEquals(true, test.canShoot(targets,owner));
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
        targets.add(target);
        targets.add(target2);
        assertEquals(false, test.canShoot(targets,owner));
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
        targets.add(target);
        targets.add(target2);
        targets.add(target3);
        assertEquals(true, test.canShoot(targets,owner));
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
        targets.add(target);
        targets.add(target2);
        targets.add(target3);
        assertEquals(false, test.canShoot(targets,owner));
    }

}