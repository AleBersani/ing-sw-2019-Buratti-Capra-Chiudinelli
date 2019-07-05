package it.polimi.ingsw.model.map;

import it.polimi.ingsw.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * tester class of board
 */
class BoardTest {
    /**
     * board to test
     */
    private Board test;
    /**
     * an ammo tile for the list of ammo
     */
    private AmmoTile ammoForReshuffleTest;

    /**
     * set the board and the ammoTile for the tests
     */
    @BeforeEach
    void setUp(){
        test= new Board(null, "/Board/Board1.json");
        ammoForReshuffleTest= new AmmoTile(0,2,1,0);
    }

    /**
     * test the find method
     */
    @Test
    void find() {
        try {
            assertEquals(test.getRooms().get(0).find(1,1),test.find(1,1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * test the reshuffle of the AmmoTile list
     */
    @Test
    void reShuffleAmmoNoRepetitions(){
        Square s;
        AmmoPoint a;
        try {
            s=test.find(1,1);
            a =s instanceof AmmoPoint ? ((AmmoPoint) s) : null;
            if (a != null) {
                a.setAmmo(ammoForReshuffleTest);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        test.getAmmoList().clear();
        test.reShuffleAmmo();
        assertEquals(35,test.getAmmoList().size());

    }


}