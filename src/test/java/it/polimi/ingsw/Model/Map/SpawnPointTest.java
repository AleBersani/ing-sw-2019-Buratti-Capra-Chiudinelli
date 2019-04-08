package it.polimi.ingsw.Model.Map;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpawnPointTest {

    SpawnPoint test;

    @Before
    public void setUp(){
        test= new SpawnPoint(1,1,null,null);
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