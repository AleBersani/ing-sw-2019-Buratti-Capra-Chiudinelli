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

class SameRoomTest {
    Square enemySquare;
    Player owner;
    Board board;
    SameRoom test;
    TargetParameter target;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        owner = new Player(true,"Yellow","Bruno");
        test = new SameRoom();
        target = new TargetParameter(null,owner,null,null,null);
    }

    @Test
    public void sameRoomTest(){
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
        target.setTargetRoom(enemySquare.getRoom());
        assertEquals(true,test.canShoot(target));
    }

    @Test
    public void notSameRoomTest(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            enemySquare = board.find(3,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(enemySquare.getRoom());
        assertEquals(false,test.canShoot(target));
    }
}