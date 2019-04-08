package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeeTest {
    Player owner;
    Square target;
    Board board;
    See test;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board1.json");
        owner = new Player(true,"red", "Bellocchio");
        test = new See();
    }

    @Test
    public void testCanSee(){
        try {
            owner.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(2,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,owner));
    }

    @Test
    public void testCanNotSee(){
        try {
            owner.setPosition(board.find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = board.find(4,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,owner));
    }
}