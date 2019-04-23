package it.polimi.ingsw.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TurnTest {

    Player test,loser,guest;
    Turn turn;
    ArrayList<Player> damageList,damageList2,playerList;
    Match testMatch;

    @BeforeEach
    public void setup(){
        guest = new Player(true,"blue", "Franco");
        test = new Player(true,"red", "France");
        loser = new Player(false,"yellow", "Paola");
        playerList = new ArrayList<Player>(Arrays.asList(guest,test,loser));
        testMatch = new Match(playerList,3,5,true,"normal");
        turn = new Turn(null,false,test,null);
        damageList = new ArrayList<Player>(Arrays.asList(test,guest,guest,guest,guest,guest,guest,test,test,test,test,null));
        damageList2 = new ArrayList<Player>(Arrays.asList(guest,guest,guest,guest,guest,guest,guest,test,test,test,test,null));
    }

    @Test
    void testAddDead() {
        turn.addDead(loser);
        assertEquals(1,turn.getDeads().size());
        turn.addDead(guest);
        assertEquals(2,turn.getDeads().size());
    }
    /*
    @Test
    void testEndTurn() {
    }

    @Test
    void testSetPoints() {
        loser.setDamage(damageList);
        assertEquals(12,loser.getDamage().size());
        turn.addDead(loser);
        turn.setPoints();
        assertEquals(8,guest.getPoints());
        assertEquals(7,test.getPoints());
        assertEquals(1,loser.getSkull());
        assertEquals(0,turn.getDeads().size());
        assertEquals(0,loser.getDamage().size());
    }

    @Test
    void testSetPoints2() {
        testSetPoints();
        loser.setDamage(damageList2);
        assertEquals(12,loser.getDamage().size());
        turn.addDead(loser);
        turn.setPoints();
        assertEquals(15,guest.getPoints());
        assertEquals(11,test.getPoints());
        assertEquals(2,loser.getSkull());
        assertEquals(0,turn.getDeads().size());
        assertEquals(0,loser.getDamage().size());
    }
    */
}