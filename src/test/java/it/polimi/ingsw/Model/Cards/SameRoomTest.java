package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.Constraints.SameRoom;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SameRoomTest {

    Room target;
    Player owner;
    Board board;
    SameRoom test;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board1.json");
        owner = new Player(true,"Yellow","Bruno");
        test = new SameRoom();
    }

    @Test
    public void sameRoomTest(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            target = board.find(1,1).getRoom();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true,test.canShoot(target,owner));
    }

    @Test
    public void notSameRoomTest(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(3,3).getRoom();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false,test.canShoot(target,owner));
    }
}