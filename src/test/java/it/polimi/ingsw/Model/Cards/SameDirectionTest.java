package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.Constraints.SameDirection;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Match;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SameDirectionTest {

    SameDirection test;
    Square target, target2;
    Player owner;
    Board board;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board1.json");
        owner = new Player(true,"red", "Fabiolo");
        test = new SameDirection();
    }

    @Test
    public void sameDirectionOneTarget(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(3,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,owner));
    }

    @Test
    public void notSameDirectionOneTarget(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(2,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false,test.canShoot(target,owner));
    }

    @Test
    public void sameDirectionTwoTarget(){
        try {
            owner.setPosition(board.find(3,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(3,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,target2,owner));
    }

    @Test
    public void notSameDirectionTwoTarget(){
        try {
            owner.setPosition(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(2,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false,test.canShoot(target,target2,owner));
    }

    @Test
    public void SameDirectionButNotSameVerseTwoTarget(){
        try {
            owner.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(1,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = board.find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false,test.canShoot(target,target2,owner));
    }
}