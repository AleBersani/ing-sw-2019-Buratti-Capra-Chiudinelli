package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Match;
import it.polimi.ingsw.Model.Player;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SeeTest {
    Player owner;
    ArrayList<Player> playerlist;
    Square target;
    Match match;
    See test;

    @Before
    public void setup(){
        match = new Match(playerlist,1,1,true,"Frenesia");
        playerlist = new ArrayList<Player>();
        owner = new Player(true,"red", "Bellocchio");
        test = new See();
    }

    @Test
    public void testCanSee(){
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
    public void testCanNotSee(){
        try {
            owner.setPosition(match.getBoard().find(1,1 ));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(4,3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, test.canShoot(target,owner));
    }
}