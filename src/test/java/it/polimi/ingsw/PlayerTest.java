package it.polimi.ingsw;

import it.polimi.ingsw.Map.Square;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void testRun(){

    }

    @Test
    public void testGrab() {

    }

    @Test
    public void testShoot() {

    }

    @Test
    public void testUsePowerUp() {

    }

    @Test
    public void testCanSee() {

    }

    @Test
    public void testReload() {

    }

    @Test
    public void testDraw() {

    }

    @Test
    public void testDiscard() {

    }

    @Test
    public void testSpawn() {

    }

    @Test
    public void testDead() {

    }

    @Test
    public void testWound() {
        test.wound(1, target);
        assertEquals(1, test.getDamageCounter());
        for (index=0; index<test.getDamage().size(); index++ ) {
            assertEquals(target, test.getDamage().get(index));
        }
    }


    @Test
    public void testMarked() {

    }
}