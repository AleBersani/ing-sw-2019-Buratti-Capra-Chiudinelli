package it.polimi.ingsw;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    int i;
    Player target,test,loser,guest;
    Square location;
    Turn turn;
    ArrayList<Player> testingMarks;

    @BeforeEach
    public void setup() {
        test = new Player(true,"red", "France");
        target = new Player(false,"green", "Giuzeppi");
        testingMarks = new ArrayList<>();
        guest = new Player(true,"blue", "Franco");
        loser = new Player(false,"yellow", "Paola");
        guest.setMark(testingMarks);
    }
    /*
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
    */

    @Test
    public void testWound() {
        test.wound(1, target);
        assertEquals(1, test.getDamageCounter());
        for (i=0;i<test.getDamage().size();i++)
            assertEquals(target, test.getDamage().get(i));
        testMarked();
        guest.wound(1,loser);
        assertEquals(2,guest.getDamageCounter());
        for(i=0;i<guest.getDamage().size();i++)
            assertEquals(loser,guest.getDamage().get(i));
    }

    @Test
    public void testMarked() {
        guest.marked(1,loser);
        for(i=0;i<guest.getMark().size();i++)
            assertEquals(loser,guest.getMark().get(i));
    }
}