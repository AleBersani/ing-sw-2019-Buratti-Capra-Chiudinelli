package it.polimi.ingsw.model.map;

import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
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

    @Test
    void calcDist() {
        assertEquals(5,test.calcDist(destination));

    }

    @Test
    void leaves() {
        test.leaves(player);
        assertEquals(false,testingMovement.remove(player));
    }

    @Test
    void arrives() {
        test.arrives(player);
        assertEquals(player , testingMovement.get(testingMovement.size()-1));
    }
}