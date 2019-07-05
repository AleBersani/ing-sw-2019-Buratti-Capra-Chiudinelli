package it.polimi.ingsw.model.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * tester class of Room
 */
class RoomTest {
    /**
     * the room to test
     */
    private Room test;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setUp(){
        ArrayList<Integer> testX = new ArrayList<>();
        ArrayList<Integer> testY = new ArrayList<>();
        ArrayList<String> testColor = new ArrayList<>();
        testColor.add("Green");
        testX.add(1);
        testY.add(5);
        test= new Room(1, testX, testY,0,0, testColor,null);
    }

    /**
     * test the find method
     */
    @Test
    void find() {
        try {
            assertEquals( test.find(1,5),
            test.getSquares().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}