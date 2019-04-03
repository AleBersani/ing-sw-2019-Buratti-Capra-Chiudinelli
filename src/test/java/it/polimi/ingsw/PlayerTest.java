package it.polimi.ingsw;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player x;
    Player p;
    int i;

    @Before
    public void setup() {
        p = new Player(true,"blue", "Franco");
        x = new Player(false,"green", "Giuzeppi");
    }

    @Test
    void testWound() {
        p.wound(1, x);
        assertEquals(1, p.getDamageCounter());
        for (i=0; i<p.getDamage().size(); i++ ) {
            assertEquals(x, p.getDamage().get(i));
        }
    }



}