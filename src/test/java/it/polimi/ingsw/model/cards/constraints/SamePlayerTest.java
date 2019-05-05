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
    ArrayList<Player> previousTarget;
    SamePlayer test;

    @BeforeEach
    void setup(){
        board = new Board(null,"./resources/Board/Board1.json");
        enemy = new Player(true,"blue", "Fabiano");
        enemy2 = new Player(false,"red", "Fabiolo");
        enemy3 = new Player(false,"red", "Fagiolo");
        target = new TargetParameter(null,null,enemy,null,null,null);
        previousTarget = new ArrayList<Player>();
        test = new SamePlayer();
    }

    @Test
    void samePreviousPlayer() {
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        previousTarget.add(enemy3);
        assertTrue(test.canShoot(target,true,previousTarget));
    }

    @Test
    void notSamePreviousPlayer(){
        previousTarget.add(enemy2);
        previousTarget.add(enemy3);
        assertFalse(test.canShoot(target,true,previousTarget));
    }

    @Test
    void notSamePreviousPlayerNotCase(){
        previousTarget.add(enemy2);
        previousTarget.add(enemy3);
        assertTrue(test.canShoot(target,false,previousTarget));
    }

    @Test
    void samePreviousPlayerNotCase() {
        previousTarget.add(enemy);
        previousTarget.add(enemy2);
        previousTarget.add(enemy3);
        assertFalse(test.canShoot(target,false,previousTarget));
    }
}