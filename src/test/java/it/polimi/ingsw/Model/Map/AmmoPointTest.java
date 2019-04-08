package it.polimi.ingsw.Model.Map;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmmoPointTest {

    AmmoPoint test;

    @Before
    public void setUp(){
        test= new AmmoPoint(1,1,null,null);
    }

    @Test
    void generate() {
        test.generate();
        //TODO
    }

    @Test
    void require() {
        assertEquals(true,test.require());
    }
}