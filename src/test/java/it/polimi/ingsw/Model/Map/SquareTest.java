package it.polimi.ingsw.Model.Map;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Player;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SquareTest {
    Square destination;
    Square test;
    Room room;
    Player player;
    Board board;
    ArrayList<Player> testingMovement;

    @Before
    public void setup(){
        board = new Board(null,"Board1");
        try {
            destination= board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test= board.find(2,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        player= new Player(false,"yellow","Eustacchio");
        testingMovement.add(player);
        test.setOnMe(testingMovement);
    }

    @Test
    void calcDist() {
        assertEquals(2,test.calcDist(destination));

    }

    @Test
    void leaves() {
        test.leaves(player);
        assertEquals(false,testingMovement.remove(player));
    }

    @Test
    void arrives() {
        test.arrives(player);
        assertEquals(player , testingMovement.get(testingMovement.size()));
    }
}