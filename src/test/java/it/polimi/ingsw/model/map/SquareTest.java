package it.polimi.ingsw.model.map;

import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * test class of Square
 */
class SquareTest {
    /**
     * a square of the board
     */
    private Square destination;
    /**
     * the tested square
     */
    private Square test;
    /**
     * a player to be place on the tested square
     */
    private Player player;
    /**
     * a list of player to place on the tested square
     */
    private ArrayList<Player> testingMovement;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setup(){
        Board board = new Board(null, "/Board/Board1.json");
        try {
            destination= board.find(1,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test= board.find(4,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        player= new Player(false,"yellow","Eustacchio");
        testingMovement= new ArrayList<>();
        testingMovement.add(player);
        test.setOnMe(testingMovement);
    }

    /**
     * test the right behaviour of calcDist
     */
    @Test
    void calcDist() {
        assertEquals(5,test.calcDist(destination));
    }

    /**
     * test if a player leave the tested square
     */
    @Test
    void leaves() {
        test.leaves(player);
        assertFalse(testingMovement.remove(player));
    }

    /**
     * test if a player arrives on the tested square
     */
    @Test
    void arrives() {
        test.arrives(player);
        assertEquals(player , testingMovement.get(testingMovement.size()-1));
    }
}