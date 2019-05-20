package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SamePlayerTest {

    Player enemy,enemy2,enemy3;
    Board board;
    TargetParameter target;
    ArrayList<ArrayList<Player>> previousTarget;
    SamePlayer test,test1;

    @BeforeEach
    void setup(){
        board = new Board(null,"/Board/Board1.json");
        enemy = new Player(true,"blue", "Fabiano");
        enemy2 = new Player(false,"red", "Fabiolo");
        enemy3 = new Player(false,"red", "Fagiolo");
        target = new TargetParameter(null,null,enemy,null,null,null,null);
        previousTarget = new ArrayList<ArrayList<Player>>();
        previousTarget.add(new ArrayList<Player>());
        previousTarget.add(new ArrayList<Player>());
        test = new SamePlayer(0);
        test1 = new SamePlayer(1);
    }

    @Test
    void samePreviousPlayer() {
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        previousTarget.get(test.getLevel()).add(enemy3);
        assertTrue(test.canShoot(target,true,previousTarget));
    }

    @Test
    void notSamePreviousPlayer(){
        previousTarget.get(test.getLevel()).add(enemy2);
        previousTarget.get(test.getLevel()).add(enemy3);
        assertFalse(test.canShoot(target,true,previousTarget));
    }

    @Test
    void notSamePreviousPlayerNotCase(){
        previousTarget.get(test.getLevel()).add(enemy2);
        previousTarget.get(test.getLevel()).add(enemy3);
        assertTrue(test.canShoot(target,false,previousTarget));
    }

    @Test
    void samePreviousPlayerNotCase() {
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        previousTarget.get(test.getLevel()).add(enemy3);
        assertFalse(test.canShoot(target,false,previousTarget));
    }

    @Test
    void samePreviousLevelOne(){
        previousTarget.get((test1.getLevel())).add(enemy);
        previousTarget.get(test1.getLevel()).add(enemy3);
        previousTarget.get(test.getLevel()).add(enemy2);
        assertTrue(test1.canShoot(target,true,previousTarget));
        assertFalse(test.canShoot(target,true,previousTarget));
    }
}