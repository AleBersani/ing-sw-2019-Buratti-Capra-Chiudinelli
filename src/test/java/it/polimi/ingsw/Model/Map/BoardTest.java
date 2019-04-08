package it.polimi.ingsw.Model.Map;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board test;

    @Before
    public void setUp(){
        test= new Board(null, "Board1");
    }

    @Test
    void find() {
        try {
            assertEquals(test.getRooms().get(1).find(1,1),test.find(1,1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}