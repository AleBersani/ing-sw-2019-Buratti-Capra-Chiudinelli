package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeeTest {
    /*
    Player owner;
    Square target;
    Board board;
    See test;
    ArrayList<Square> targets;

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        owner = new Player(true,"red", "Bellocchio");
        test = new See();
        targets = new ArrayList<Square>();
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
        targets.add(target);
        assertEquals(true, test.canShoot(targets,owner));
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
        targets.add(target);
        assertEquals(false, test.canShoot(targets,owner));
    }
    */
}