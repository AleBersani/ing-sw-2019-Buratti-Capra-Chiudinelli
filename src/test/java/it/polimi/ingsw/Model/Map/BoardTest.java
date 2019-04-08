package it.polimi.ingsw.Model.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {
    Board test;

    @BeforeEach
    public void setUp(){
        test= new Board(null, "./resources/Board1.json");
    }

    @Test
    void find() {
        try {
            assertEquals(test.getRooms().get(0).find(1,1),test.find(1,1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}