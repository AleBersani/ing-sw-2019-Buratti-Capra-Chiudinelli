package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Match;
import it.polimi.ingsw.Model.Player;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MinimumDistanceTest {

    Player owner;
    ArrayList<Player> playerlist;
    Square target;
    Match match;
    MinimumDistance test;

    @Before
    public void setup() {
        owner = new Player(true,"blue", "Franco");
        playerlist = new ArrayList<Player>();
        match = new Match(playerlist,1,1,true,"Frenesia");
        test = new MinimumDistance(2);

    }

    @Test
    public void testCanShoot(){
        try {
            owner.setPosition(match.getBoard().find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(2,1);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true, test.canShoot(target,owner));
    }

    @Test
    public void testCanNotShoot(){
        try {
            owner.setPosition(match.getBoard().find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(3,2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,owner));
    }
}