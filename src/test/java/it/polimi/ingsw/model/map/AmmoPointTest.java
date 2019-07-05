package it.polimi.ingsw.model.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * test class of AmmoPoint
 */
class AmmoPointTest {

    /**
     * the AmmoPoint to test
     */
    private AmmoPoint test;

    /**
     * set the ammoPoint
     */
    @BeforeEach
    void setUp(){
        test= new AmmoPoint(1,1,null,null);
    }

    /**
     * test if the AmmoPoint hasn't AmmoTile on it
     */
    @Test
    void require() {
        assertTrue(test.require());
    }
}