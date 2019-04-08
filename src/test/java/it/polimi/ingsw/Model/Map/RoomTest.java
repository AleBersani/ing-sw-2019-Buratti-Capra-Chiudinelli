package it.polimi.ingsw.Model.Map;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    Room test;
    ArrayList<Integer> testX;
    ArrayList<Integer> testY;


    @Before
    public void setUp(){
        testX.add(1);
        testY.add(5);
        test= new Room(1,testX,testY,0,0,null,null);
    }

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