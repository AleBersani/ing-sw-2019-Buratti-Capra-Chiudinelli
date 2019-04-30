package it.polimi.ingsw.model.map;

import it.polimi.ingsw.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {
    Board test;
    AmmoTile ammoForReshuffleTest;

    @BeforeEach
    public void setUp(){
        test= new Board(null, "./resources/Board/Board1.json");
        ammoForReshuffleTest= new AmmoTile(0,2,1,0);
    }

    @Test
    void find() {
        try {
            assertEquals(test.getRooms().get(0).find(1,1),test.find(1,1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void reShuffleAmmoNoRepetitions(){
        Square s;
        AmmoPoint a;
        try {
            s=test.find(1,1);
            a =s instanceof AmmoPoint ? ((AmmoPoint) s) : null;
            a.setAmmo(ammoForReshuffleTest);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        test.getAmmoList().clear();
        test.reShuffleAmmo();
        assertEquals(35,test.getAmmoList().size());

    }


}