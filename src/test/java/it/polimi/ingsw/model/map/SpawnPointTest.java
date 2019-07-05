package it.polimi.ingsw.model.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * tester class of SpawnPoint
 */
class SpawnPointTest {

    /**
     * the tested SpawnPoint
     */
    private SpawnPoint test;

    /**
     * set the spawnPoint to test
     */
    @BeforeEach
    void setUp(){
        test= new SpawnPoint(1,1,null,null);
    }

    /**
     * test if the spawn point has no weapon on it
     */
    @Test
    void require() {
        assertTrue(test.require());
    }
}