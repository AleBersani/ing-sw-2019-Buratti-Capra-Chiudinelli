package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.map.AmmoTile;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    int i;
    Board board;
    Player target,test,loser,guest;
    Square location;
    Turn turn;
    Match testMatch;
    ArrayList<Player> testingMarks,testingDeads,playerList;
    ArrayList<PowerUp> testingPowerUp;
    PowerUp teleporter;
    TargetParameter targetParameterTeleporter,targetShoot;
    Weapon lockRifle;
    AmmoTile ammoTileTest;

    @BeforeEach
    public void setup() {
        test = new Player(true,"red", "France");
        target = new Player(false,"green", "Giuzeppi");
        testingMarks = new ArrayList<>();
        guest = new Player(true,"blue", "Franco");
        loser = new Player(false,"yellow", "Paola");
        guest.setMark(testingMarks);
        board = new Board(testMatch, "./resources/Board/Board1.json");
        testingDeads = new ArrayList<>();
        turn = new Turn(null,false,guest,null);
        turn.setDeads(testingDeads);
        teleporter = new PowerUp("red","teleport");
        testingPowerUp = new ArrayList<>(Arrays.asList(teleporter,teleporter));
        guest.setPowerUps(testingPowerUp);
        playerList = new ArrayList<>(Arrays.asList(guest,test,loser));
        ammoTileTest = new AmmoTile(2,1,0,0);
        testMatch = new Match(playerList,3,5,true,"normal");
        lockRifle = new Weapon("blue","Lock rifle",2,0,0,null) {
            @Override
            public void fireOptional(TargetParameter target, int which) throws NotThisKindOfWeapon, InvalidTargetException {
            }
            @Override
            public void fireAlternative(TargetParameter target) throws NotThisKindOfWeapon, InvalidTargetException {
            }
        };
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
        } catch (InvalidDestinationException | NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1,guest.getTurn().getActionCounter());
        try {
            assertEquals(guest.getPosition(),board.find(4,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.run(board.find(3,1));
        } catch (InvalidDestinationException | NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(2,guest.getTurn().getActionCounter());
    }
    //TESTED THE RIGHT MOVEMENT AND THE RIGHT GRAB
    @Test
    public void testGrab() {
        guest.setTurn(turn);
        guest.setDamageCounter(0);
        guest.setTurnedPlank(false);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        board.getAmmoList().clear();
        board.getAmmoList().add(ammoTileTest);
        try {
            guest.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.grab(board.find(3,3));
        } catch (MaxHandSizeException | InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            try {
                assertEquals(board.find(2,2),guest.getPosition());
            } catch (NotFoundException ex) {
                ex.printStackTrace();
            }
        }
        try {
            board.find(3,2).generate();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.grab(board.find(3,2));
        } catch (MaxHandSizeException | InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(board.find(3,2),guest.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(3,guest.getRedAmmo());
        assertEquals(2,guest.getBlueAmmo());
        assertEquals(1,guest.getYellowAmmo());
        assertEquals(1,guest.getTurn().getActionCounter());
        try {
            guest.grab(board.find(3,2));
        } catch (MaxHandSizeException | InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(NullAmmoException.class,()->guest.grab(board.find(3,2)));
        }
    }
    //TESTED THE FIRST ADRENALINE POWER UP MOVEMENT
    @Test
    public void testGrab2() {
        guest.setTurn(turn);
        guest.setDamageCounter(3);
        guest.setTurnedPlank(false);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        turn.endTurn();
        try {
            guest.setPosition(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.grab(board.find(1,1));
        } catch (MaxHandSizeException | InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            try {
                assertEquals(board.find(3,2),guest.getPosition());
            } catch (NotFoundException ex) {
                ex.printStackTrace();
            }
        }
        assertEquals(0,guest.getTurn().getActionCounter());
        try {
            guest.grab(board.find(2,1));
        } catch (MaxHandSizeException | InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(board.find(2,1),guest.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1,guest.getTurn().getActionCounter());
    }
    /*
    @Test
    public void testShoot() {
        guest.setTurn(turn);
        guest.setDamageCounter(6);
        try {
            guest.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        lockRifle.setLoad(true);
        try {
            targetShoot = new TargetParameter(null,guest,test,null,board.find(4,3),"effect");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.shoot(lockRifle,board.find(3,2),targetShoot);
        } catch (NotLoadedException | InvalidDestinationException | InvalidTargetException | NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(board.find(3,2),guest.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(2,test.getDamageCounter());
        assertEquals(1,test.getMark().size());
        assertEquals(guest,test.getMark().get(0));
        assertEquals(1,guest.getTurn().getActionCounter());

    }

    @Test
    public void testUsePowerUp() {
        guest.setTurn(turn);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        try {
            guest.setPosition(board.find(3,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            targetParameterTeleporter = new TargetParameter(board.find(1,1),guest,null,null,null,null);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.usePowerUp(teleporter,targetParameterTeleporter);
        } catch (InvalidTargetException invalidTargetException) {
            invalidTargetException.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
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
        assertTrue(guest.canSee(test));
    }

    @Test
    public void testReload() {
        lockRifle.setLoad(false);
        guest.setBlueAmmo(3);
        guest.setYellowAmmo(2);
        guest.setRedAmmo(0);
        try {
            guest.reload(lockRifle);
        } catch (LoadedException | NoAmmoException e) {
            e.printStackTrace();
        }
        assertEquals(1,guest.getBlueAmmo());
        assertEquals(2,guest.getYellowAmmo());
        assertEquals(0,guest.getRedAmmo());
        try {
            guest.reload(lockRifle);
        } catch (LoadedException e) {
            assertEquals(0,0);
        } catch (NoAmmoException e) {
            e.printStackTrace();
        }
        lockRifle.setLoad(false);
        try {
            guest.reload(lockRifle);
        } catch (LoadedException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            assertEquals(0,0);
        }
    }

    @Test
    public void testDraw() {
        guest.setTurn(turn);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        try {
            guest.draw();
            assertEquals(3,guest.getPowerUps().size());
        } catch (MaxHandSizeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDiscard() {
        guest.discard(teleporter);
        assertEquals(1,guest.getPowerUps().size());
    }

    @Test
    public void testSpawn() {
        guest.setTurn(turn);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        try {
            guest.spawn(teleporter);
            assertEquals(guest.getPosition(),board.find(1,2));
            assertEquals(1,guest.getPowerUps().size());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

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
        } catch (InvalidDestinationException | NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(4,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.runFrenzy(board.find(1,1));
        } catch (InvalidDestinationException | NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testOnlyFrenzyAction(){
        test.setLastKill(true);
        loser.setTurn(turn);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        turn.setFrenzy(true);
        assertEquals(0,loser.onlyFrenzyAction());
        guest.setTurn(turn);
        assertEquals(1,guest.onlyFrenzyAction());
        test.setTurn(turn);
        assertEquals(1,test.onlyFrenzyAction());
        test.setLastKill(false);
        guest.setLastKill(true);
        guest.setTurn(turn);
        assertEquals(1,guest.onlyFrenzyAction());
        test.setTurn(turn);
        assertEquals(0,test.onlyFrenzyAction());
        loser.setTurn(turn);
        assertEquals(0,loser.onlyFrenzyAction());
    }
}