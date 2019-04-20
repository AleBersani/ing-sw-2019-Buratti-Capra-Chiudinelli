package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.InvalidDestinationException;
import it.polimi.ingsw.Exception.MaxHandSizeException;
import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.PowerUp;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    int i;
    Board board;
    Player target,test,loser,guest;
    Square location;
    Turn turn;
    ArrayList<Player> testingMarks,testingDeads;
    ArrayList<PowerUp> testingPowerUp;

    @BeforeEach
    public void setup() {
        test = new Player(true,"red", "France");
        target = new Player(false,"green", "Giuzeppi");
        testingMarks = new ArrayList<>();
        guest = new Player(true,"blue", "Franco");
        loser = new Player(false,"yellow", "Paola");
        guest.setMark(testingMarks);
        board = new Board(null, "./resources/Board/Board1.json");
        testingDeads = new ArrayList<>();
        turn = new Turn(null,false,guest,null);
        turn.setDeads(testingDeads);
        testingPowerUp = new ArrayList<>();
        target.setPowerUps(testingPowerUp);
    }

    @Test
    public void testRun(){
        guest.setTurn(turn);
        try {
            guest.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.run(board.find(4,3));
        } catch (InvalidDestinationException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(4,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.run(board.find(3,1));
        } catch (InvalidDestinationException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
    /*
    @Test
    public void testGrab() {

    }

    @Test
    public void testShoot() {

    }

    @Test
    public void testUsePowerUp() {

    }
    */
    @Test
    public void testCanSee() {
        guest.setTurn(turn);
        try {
            guest.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.setPosition(board.find(3,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(true,guest.canSee(test));
    }
    /*
    @Test
    public void testReload() {

    }

    @Test
    public void testDraw() {
        try {
            target.draw();
            assertEquals(1,target.getPowerUps().size());
        } catch (MaxHandSizeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDiscard() {

    }

    @Test
    public void testSpawn() {

    }
    */
    @Test
    public void testDead() {
        guest.setTurn(turn);
        guest.dead();
        assertEquals(guest,turn.getDeads().get(0));
    }

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

    @Test
    public void testRunFrenzy(){
        guest.setTurn(turn);
        try {
            guest.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.runFrenzy(board.find(4,2));
        } catch (InvalidDestinationException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(4,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.runFrenzy(board.find(1,1));
        } catch (InvalidDestinationException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }
}