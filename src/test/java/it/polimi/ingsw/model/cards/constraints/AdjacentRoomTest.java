package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AdjacentRoomTest {
    AdjacentRoom test;
    Player owner;
    Board board;
    TargetParameter target;
    ArrayList<Player> previousTarget;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        owner = new Player(true,"Yellow","Bruno");
        test = new AdjacentRoom();
        previousTarget = new ArrayList<Player>();
        target = new TargetParameter(null,owner,null,null,null);
    }

    @Test
    void nearRoom(){
        try {
            owner.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(board.getRooms().get(0));
        assertEquals(true,test.canShoot(target,true,previousTarget));
    }

    @Test
    void notNearRoom(){
        try {
            owner.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(board.getRooms().get(0));
        assertEquals(false,test.canShoot(target,true,previousTarget));
    }

    @Test
    void inMyRoom(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        target.setTargetRoom(board.getRooms().get(0));
        assertEquals(false,test.canShoot(target,true,previousTarget));
    }
}