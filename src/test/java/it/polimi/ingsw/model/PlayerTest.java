package it.polimi.ingsw.model;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.cards.PowerUp;
import it.polimi.ingsw.model.cards.Weapon;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.cards.effects.EffectVsPlayer;
import it.polimi.ingsw.model.cards.effects.MovementEffect;
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
    PowerUp teleporter,tagbackGrenade,newton,targetingScope;
    TargetParameter targetParameterTeleporter,targetParameterNewton,targetParameterTagbackGrenade,targetParameterTargetingScope,targetShoot;
    Weapon lockRifle;
    AmmoTile ammoTileTest;
    ArrayList<Constraint> list = new ArrayList<>();
    ArrayList<Boolean> list2 = new ArrayList<>();

    @BeforeEach
    public void setup() {
        test = new Player(false,"red", "France");
        target = new Player(false,"green", "Giuzeppi");
        testingMarks = new ArrayList<>();
        guest = new Player(true,"blue", "Franco");
        loser = new Player(false,"yellow", "Paola");
        guest.setMark(testingMarks);
        board = new Board(testMatch, "/Board/Board1.json");
        testingDeads = new ArrayList<>();
        turn = new Turn(false,guest,null);
        turn.setDeads(testingDeads);
        teleporter = new PowerUp("red","teleport");
        teleporter.setEffect(new MovementEffect(0,0,0,"teleport",list,list2,2147483647,false,false,false,true));
        tagbackGrenade= new PowerUp("blue", "tagback grenade");
        tagbackGrenade.setEffect(new EffectVsPlayer(0,0,0,"tagback grenade",list,list2,0,1,false,false));
        newton=new PowerUp("yellow", "newton");
        newton.setEffect(new MovementEffect(0,0,0,"newton",list,list2,2,true,false,false,false));
        targetingScope = new PowerUp("blue","targeting scope");
        targetingScope.setEffect(new EffectVsPlayer(0,0,0,"targeting scope",list,list2,1,0,false,false));
        testingPowerUp = new ArrayList<>(Arrays.asList(teleporter,teleporter));
        guest.setPowerUps(testingPowerUp);
        playerList = new ArrayList<>(Arrays.asList(guest,test,loser));
        ammoTileTest = new AmmoTile(2,1,0,0);
        testMatch = new Match(playerList,3,5,true,"normal","/Board/Board1.json" );
        lockRifle = new Weapon("blue", "Lock rifle", 2, 0, 0, null) {

            @Override
            public void fireOptional(ArrayList<TargetParameter> target, int which) throws NotThisKindOfWeapon, InvalidTargetException, NoAmmoException {

            }

            @Override
            public void fireAlternative(ArrayList<TargetParameter> target) throws NotThisKindOfWeapon, InvalidTargetException, NoAmmoException {

            }

            @Override
            public boolean isOptional() {
                return false;
            }

            @Override
            public boolean isAlternative() {
                return false;
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
        } catch ( InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(InvalidDestinationException.class,()->guest.grab(board.find(3,3)));
        }
        try {
            board.find(3,2).generate();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.grab(board.find(3,2));
        } catch ( InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
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
        } catch ( InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(NullAmmoException.class,()->guest.grab(board.find(3,2)));
        }
    }
    //TESTED THE FIRST ADRENALINE POWER UP MOVEMENT AND THE ELEMENT NOT FOUND
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
        } catch ( InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(InvalidDestinationException.class,()->guest.grab(board.find(1,1)));
        }
        assertEquals(0,guest.getTurn().getActionCounter());
        try {
            guest.grab(board.find(2,1));
        } catch (InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(board.find(2,1),guest.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1,guest.getTurn().getActionCounter());
        try {
            guest.grab(board.find(3,1));
        } catch (InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(ElementNotFoundException.class,()->guest.grab(board.find(3,1)));
        }
        assertEquals(1,guest.getTurn().getActionCounter());
    }
    //TESTED THE RIGHT MOVEMENT ON A FRENZY TURN
    @Test
    public void testGrab3() {
        guest.setTurn(turn);
        guest.setDamageCounter(0);
        guest.setTurnedPlank(false);
        turn.setMatch(testMatch);
        turn.setFrenzy(true);
        testMatch.setBoard(board);
        turn.endTurn();
        try {
            guest.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.grab(board.find(3,2));
        } catch (InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            e.printStackTrace();
        }
        test.setTurn(turn);
        test.setLastKill(true);
        try {
            test.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.grab(board.find(2,3));
        } catch (InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            e.printStackTrace();
        }
        loser.setTurn(turn);
        try {
            loser.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            loser.grab(board.find(2,2));
        } catch (InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            e.printStackTrace();
        }
    }
    /*
    //TESTED THE WEAPON GRAB WITHOUT AMMO
    @Test
    public void testGrab4(){
        guest.setTurn(turn);
        guest.setDamageCounter(0);
        guest.setRedAmmo(0);
        guest.setBlueAmmo(0);
        guest.setYellowAmmo(0);
        guest.setTurnedPlank(false);
        turn.setMatch(testMatch);
        turn.setFrenzy(true);
        testMatch.setBoard(board);
        turn.endTurn();
        try {
            guest.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.grab(board.find(3,1));
        } catch (InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(ElementNotFoundException.class,()->guest.grab(board.find(3,1)));
            assertEquals(0,guest.getTurn().getActionCounter());
        }
        try {
            guest.grabWeapon(board.find(3,1),1);
        } catch (ElementNotFoundException | MaxHandWeaponSizeException | NoAmmoException | NotFoundException e) {
            assertThrows(NoAmmoException.class,()->guest.grabWeapon(board.find(3,1),1));
            assertEquals(0,guest.getTurn().getActionCounter());
            assertEquals(0,guest.getWeapons().size());
        }

    }
    //TESTED THE WEAPON GRAB WITH AMMO
    @Test
    public void testGrab5(){
        guest.setTurn(turn);
        guest.setDamageCounter(0);
        guest.setRedAmmo(3);
        guest.setBlueAmmo(3);
        guest.setYellowAmmo(3);
        guest.setTurnedPlank(false);
        turn.setMatch(testMatch);
        turn.setFrenzy(true);
        testMatch.setBoard(board);
        turn.endTurn();
        try {
            guest.setPosition(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.grab(board.find(3,1));
        } catch (InvalidDestinationException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(ElementNotFoundException.class,()->guest.grab(board.find(3,1)));
        }
        try {
            guest.grabWeapon(board.find(3,1),1);
        } catch (ElementNotFoundException | MaxHandWeaponSizeException | NoAmmoException | NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1,guest.getTurn().getActionCounter());
        assertEquals(1,guest.getWeapons().size());
    }
    */
    @Test
    public void testShoot() {
        ArrayList<TargetParameter> parameterList = new ArrayList<>();
        guest.setTurn(turn);
        try {
            guest.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            test.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        targetShoot = new TargetParameter(null,guest,test,null,null,"Base");

        parameterList.add(targetShoot);
        ArrayList<Weapon> weaponList = board.getWeaponsListCopy();
        for(Weapon i : weaponList)
            if(i.getName().equals("Lock rifle"))
                lockRifle = i;
        try {
            guest.shoot(lockRifle,board.find(2,2),parameterList);
        } catch (NotLoadedException | InvalidDestinationException | InvalidTargetException | NotThisKindOfWeapon | NoAmmoException | NotFoundException ex) {
            ex.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,test.getDamageCounter());
        assertEquals(1,test.getMark().size());
    }

    @Test
    public void testEndShoot(){
        guest.setTurn(turn);
        turn.setActionCounter(1);
        guest.endShoot(lockRifle);
        for(ArrayList<Player> previosTargets: lockRifle.getPreviousTarget()){
            assertEquals(0,previosTargets.size());
        }
        assertFalse(lockRifle.isLoad());
        assertEquals(2,turn.getActionCounter());
    }
    //TESTED THE TELEPORTER POWER UP
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
            targetParameterTeleporter.setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.usePowerUp(teleporter,targetParameterTeleporter);
        } catch (InvalidTargetException invalidTargetException) {
            invalidTargetException.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        } catch (OnResponseException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(guest.getPosition(),board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1,guest.getPowerUps().size());
    }
    //TESTED THE NEWTON POWER UP (LINEAR MOVEMENT)
    @Test
    public void testUsePowerUp2() {
        guest.setTurn(turn);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        testingPowerUp.clear();
        testingPowerUp.add(newton);
        guest.setPowerUps(testingPowerUp);
        try {
            test.setPosition(board.find(4,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            targetParameterNewton = new TargetParameter(board.find(2,2),guest,test,null,null,null);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            targetParameterNewton.setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.usePowerUp(newton,targetParameterNewton);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        } catch (OnResponseException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(test.getPosition(),board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(0,guest.getPowerUps().size());
    }
    //TESTED THE NEWTON POWER UP (NOT LINEAR MOVEMENT)
    @Test
    public void testUsePowerUp3() {
        guest.setTurn(turn);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        testingPowerUp.clear();
        testingPowerUp.add(newton);
        guest.setPowerUps(testingPowerUp);
        try {
            test.setPosition(board.find(4,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            targetParameterNewton = new TargetParameter(board.find(3,1),guest,test,null,null,null);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            targetParameterNewton.setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            guest.usePowerUp(newton,targetParameterNewton);
        } catch (InvalidTargetException e) {
            assertThrows(InvalidTargetException.class,()->guest.usePowerUp(newton,targetParameterNewton));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        } catch (OnResponseException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(test.getPosition(),board.find(4,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1,guest.getPowerUps().size());
    }

    //TODO rifare
    //TESTED THE TARGETING SCOPE POWER UP
    @Test
    public void testUsePowerUp4(){
        guest.setTurn(turn);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        testingPowerUp.clear();
        testingPowerUp.add(targetingScope);
        guest.setPowerUps(testingPowerUp);
        for(Weapon i : board.getWeaponsListCopy()) {
            if (i.getName().equals("Lock rifle")) {
                lockRifle = i;
            }
        }
        guest.getWeapons().add(lockRifle);
        lockRifle.getPreviousTarget().get(2).add(test);
        targetParameterTargetingScope = new TargetParameter(null,guest,test,null,null,null);
        try {
            targetingScope.useEffect(targetParameterTargetingScope,lockRifle.getPreviousTarget());
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,test.getDamageCounter());
        assertEquals(guest,test.getDamage().get(0));
        targetParameterTargetingScope = new TargetParameter(null,guest,guest,null,null,null);
        try {
            targetingScope.useEffect(targetParameterTargetingScope,lockRifle.getPreviousTarget());
        } catch (InvalidTargetException e) {
            assertThrows(InvalidTargetException.class,()->guest.usePowerUp(targetingScope,targetParameterTargetingScope));
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
    }

/*
    //TESTED THE TAGBACK GRENADE POWER UP
    @Test
    public void testUsePowerUp5(){
        guest.setTurn(turn);
        turn.setMatch(testMatch);
        testMatch.setBoard(board);
        testingPowerUp.clear();
        testingPowerUp.add(tagbackGrenade);
        guest.setPowerUps(testingPowerUp);
        targetParameterTagbackGrenade = new TargetParameter(null,guest,test,null,null,null);
        try {
            guest.usePowerUp(tagbackGrenade,targetParameterTagbackGrenade);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(guest,test.getMark().get(0));
        assertEquals(0,guest.getPowerUps().size());
        targetParameterTagbackGrenade = new TargetParameter(null,guest,guest,null,null,null);
        try {
            guest.usePowerUp(tagbackGrenade,targetParameterTagbackGrenade);
        } catch (InvalidTargetException e) {
            assertThrows(InvalidTargetException.class,()->guest.usePowerUp(tagbackGrenade,targetParameterTagbackGrenade));
        } catch (NoOwnerException e) {
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
    /*
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
            assertThrows(LoadedException.class,()->guest.reload(lockRifle));
        } catch (NoAmmoException e) {
            e.printStackTrace();
        }
        lockRifle.setLoad(false);
        try {
            guest.reload(lockRifle);
        } catch (LoadedException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            assertThrows(NoAmmoException.class,()->guest.reload(lockRifle));
        }
    }
    */
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
        try {
            guest.draw();
        } catch (MaxHandSizeException e) {
            assertThrows(MaxHandSizeException.class,()->guest.draw());
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
        test.wound(1, target, true);
        assertEquals(1, test.getDamageCounter());
        for (i=0;i<test.getDamage().size();i++)
            assertEquals(target, test.getDamage().get(i));
        testMarked();
        guest.wound(1,loser, true);
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
    public void testGrabFrenzy() {
        loser.setTurn(turn);
        test.setLastKill(true);
        guest.setDamageCounter(6);
        loser.setTurnedPlank(true);
        turn.setMatch(testMatch);
        turn.setFrenzy(true);
        testMatch.setBoard(board);
        turn.endTurn();
        try {
            loser.setPosition(board.find(1, 1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            loser.grabFrenzy(board.find(3, 2));
        } catch (InvalidDestinationException | MaxHandSizeException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(InvalidDestinationException.class, () -> loser.grabFrenzy(board.find(3, 2)));
        }
        try {
            assertEquals(board.find(1, 1), loser.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            loser.grabFrenzy(board.find(1, 2));
        } catch (InvalidDestinationException | MaxHandSizeException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            assertThrows(ElementNotFoundException.class, () -> loser.grabFrenzy(board.find(1, 2)));
        }
        try {
            assertEquals(board.find(1, 1), loser.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            loser.grabFrenzy(board.find(2, 2));
        } catch (InvalidDestinationException | MaxHandSizeException | NullAmmoException | ElementNotFoundException | NotFoundException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(board.find(2, 2), loser.getPosition());
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