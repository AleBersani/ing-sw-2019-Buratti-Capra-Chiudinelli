package it.polimi.ingsw;

import it.polimi.ingsw.Map.Square;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player target;
    Player test;
    int index;
    Square location;

    @Before
    public void setup() {
        test = new Player(true,"blue", "Franco");
        target = new Player(false,"green", "Giuzeppi");
    }

    @Test
    void testWound() {
        test.wound(1, target);
        assertEquals(1, test.getDamageCounter());
        for (index=0; index<test.getDamage().size(); index++ ) {
            assertEquals(target, test.getDamage().get(index));
        }
    }

    @Test
    void testRun(){


    }




}