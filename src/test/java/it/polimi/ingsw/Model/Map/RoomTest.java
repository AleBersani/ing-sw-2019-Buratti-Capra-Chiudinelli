package it.polimi.ingsw.Model.Map;

import it.polimi.ingsw.Model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomTest {

    Room test;
    ArrayList<Integer> testX;
    ArrayList<Integer> testY;
    ArrayList<String> testColor;
    Board b;
    Match m;


    @BeforeEach
    public void setUp(){
        testX= new ArrayList<>();
        testY= new ArrayList<>();
        testColor=new ArrayList<>();
        testColor.add("Green");
        testX.add(1);
        testY.add(5);
        test= new Room(1,testX,testY,0,0,testColor,null);
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