package it.polimi.ingsw;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.SameRoom;
import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Match;
import it.polimi.ingsw.Model.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SameRoomTest {
    Room target;
    Player owner;
    Match match;
    SameRoom test;
    ArrayList<Player> playerlist;

    @Before
    public void setup(){
        match = new Match(playerlist,1,1,true,"Frenesia");
        playerlist = new ArrayList<Player>();
        owner = new Player(true,"Yellow","Bruno");
        test = new SameRoom();
    }

    @Test
    public void sameRoomTest(){
        try {
            owner.setPosition(match.getBoard().find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(1,1).getRoom();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true,test.canShoot(target,owner));
    }

    @Test
    public void notSameRoomTest(){
        try {
            owner.setPosition(match.getBoard().find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            target = match.getBoard().find(3,3).getRoom();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false,test.canShoot(target,owner));
    }
}
