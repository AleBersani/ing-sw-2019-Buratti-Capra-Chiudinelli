package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Match;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SameSquareTest {


    SameSquare test;
    Player owner;
    Square target, target2, target3;
    Match match;
    ArrayList<Player> playerlist;

    @BeforeEach
    public void setup(){
        match = new Match(playerlist,1,1,true,"Frenesia");
        playerlist = new ArrayList<Player>();
        owner = new Player(true,"red","Luciano");
        test = new SameSquare();
    }

    @Test
    public void sameSquareOneTarget(){

        try {
            owner.setPosition(match.getBoard().find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            target = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,owner));
    }

    @Test
    public void notSameSquareOneTarget(){
        try {
            owner.setPosition(match.getBoard().find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,owner));
    }

    @Test
    public void sameSquareTwoTarget(){
        try {
            owner.setPosition(match.getBoard().find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,target2,owner));
    }

    @Test
    public void notSameSquareTwoTarget(){
        try {
            owner.setPosition(match.getBoard().find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = match.getBoard().find(2,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,target2,owner));
    }

    @Test
    public void sameSquareThreeTarget(){
        try {
            owner.setPosition(match.getBoard().find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target3 = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,target2,target3,owner));
    }

    @Test
    public void notSameSquareThreeTarget(){
        try {
            owner.setPosition(match.getBoard().find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target2 = match.getBoard().find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target3 = match.getBoard().find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,target2,target3,owner));
    }
}