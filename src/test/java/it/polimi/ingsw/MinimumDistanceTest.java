package it.polimi.ingsw;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.MinimumDistance;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Match;
import it.polimi.ingsw.Model.Player;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

public class MinimumDistanceTest {
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
    }

     @Test
    public void testCanShoot(){
         assertEquals(true, test.canShoot(target,owner));
    }
}
