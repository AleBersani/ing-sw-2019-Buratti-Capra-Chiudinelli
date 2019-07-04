package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * test class of SamePlayer
 */
class SamePlayerTest {

    /**
     * constraint to test
     */
    private SamePlayer test;
    /**
     * constraint to test
     */
    private SamePlayer test1;
    /**
     * enemy of the owner
     */
    private Player enemy;
    /**
     * enemy of the owner
     */
    private Player enemy2;
    /**
     * enemy of the owner
     */
    private Player enemy3;
    /**
     * parameters of weapon's targets
     */
    private TargetParameter target;
    /**
     * previous targets of the weapon
     */
    private ArrayList<ArrayList<Player>> previousTarget;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setup(){
        enemy = new Player(true,"blue", "Fabiano");
        enemy2 = new Player(false,"red", "Fabiolo");
        enemy3 = new Player(false,"red", "Fagiolo");
        target = new TargetParameter(null,null,enemy,null,null,null);
        previousTarget = new ArrayList<>();
        previousTarget.add(new ArrayList<>());
        previousTarget.add(new ArrayList<>());
        test = new SamePlayer(0);
        test1 = new SamePlayer(1);
    }

    /**
     * test the target enemy is in the previous target list
     */
    @Test
    void samePreviousPlayer() {
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        previousTarget.get(test.getLevel()).add(enemy3);
        assertTrue(test.canShoot(target,true,previousTarget));
    }

    /**
     * test the target enemy isn't in the previous target list
     */
    @Test
    void notSamePreviousPlayer(){
        previousTarget.get(test.getLevel()).add(enemy2);
        previousTarget.get(test.getLevel()).add(enemy3);
        assertFalse(test.canShoot(target,true,previousTarget));
    }

    /**
     * test the target enemy isn't in the previous target list
     */
    @Test
    void notSamePreviousPlayerNotCase(){
        previousTarget.get(test.getLevel()).add(enemy2);
        previousTarget.get(test.getLevel()).add(enemy3);
        assertTrue(test.canShoot(target,false,previousTarget));
    }

    /**
     * test the target enemy is in the previous target list
     */
    @Test
    void samePreviousPlayerNotCase() {
        previousTarget.get(test.getLevel()).add(enemy);
        previousTarget.get(test.getLevel()).add(enemy2);
        previousTarget.get(test.getLevel()).add(enemy3);
        assertFalse(test.canShoot(target,false,previousTarget));
    }

    /**
     * test the target enemy is in the previous target list at level one
     */
    @Test
    void samePreviousLevelOne(){
        previousTarget.get((test1.getLevel())).add(enemy);
        previousTarget.get(test1.getLevel()).add(enemy3);
        previousTarget.get(test.getLevel()).add(enemy2);
        assertTrue(test1.canShoot(target,true,previousTarget));
        assertFalse(test.canShoot(target,true,previousTarget));
    }
}