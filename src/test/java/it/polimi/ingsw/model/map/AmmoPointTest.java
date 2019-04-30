package it.polimi.ingsw.model.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AmmoPointTest {

    AmmoPoint test;

    @BeforeEach
    public void setUp(){
        test= new AmmoPoint(1,1,null,null);
    }


    @Test
    void require() {
        assertTrue(test.require());
    }
}