package it.polimi.ingsw.Model.Map;

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
    ArrayList<Player> testingMovement;

    @Before
    public void setup(){
        destination= new AmmoPoint(1,1,"blue", room);
        test= new AmmoPoint(1,3,"blue", room);
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