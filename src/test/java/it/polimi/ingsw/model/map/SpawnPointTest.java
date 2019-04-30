package it.polimi.ingsw.model.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpawnPointTest {

    SpawnPoint test;

    @BeforeEach
    public void setUp(){
        test= new SpawnPoint(1,1,null,null);
    }

    @Test
    void require() {
        assertEquals(true,test.require());
    }
}