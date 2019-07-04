package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test class of Alternative weapons
 */
class WeaponAlternativeTest {

    /**
     * weapon to test
     */
    private Weapon weapon;
    /**
     * owner of the weapon to test
     */
    private Player owner;
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
     * board of the game
     */
    private Board board;
    /**
     * parameters of weapon's targets
     */
    private ArrayList<TargetParameter> target;
    /**
     * previous targets of the weapon
     */
    private ArrayList<Player> prevPlayer;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setup(){
        owner = new Player(true,"red","Luciano");
        enemy = new Player(false,"blue", "Fabiano");
        enemy2 = new Player(false,"red", "Fabiolo");
        enemy3 = new Player(false,"red", "Fagiolo");
        board = new Board(null,"/Board/Board1.json");
        target = new ArrayList<>();
        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));
        prevPlayer = new ArrayList<>();
    }

    /**
     * this test verify the right and the wrong uses of Electroscythe
     */
    @Test
    void electroscythe() {
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Electroscythe")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,1));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setTargetSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy3.getDamage().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.setDamageCounter(0);
        try {
            enemy.setPosition(board.find(1,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,1));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,1));
            enemy3.getPosition().arrives(enemy3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(0,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy3.getDamage().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,1));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setTargetSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(2,enemy2.getDamageCounter());
        assertEquals(2,enemy3.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
            assertEquals(owner,enemy2.getDamage().get(i));
            assertEquals(owner,enemy3.getDamage().get(i));
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            target.get(0).setTargetSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireAlternative(target));

    }

    /**
     * this test verify the right and the wrong uses of Flamethrower
     */
    @Test
    void flamethrower(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Flamethrower")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(4,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);

        enemy.getDamage().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);
        try {
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(3,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setTargetSquare(board.find(3,2));
            target.get(1).setTargetSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(2,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
            assertEquals(owner,enemy2.getDamage().get(i));
        }
        assertEquals(owner,enemy3.getDamage().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);
        try {
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
    }

    /**
     * this test verify the right and the wrong uses of furnace
     */
    @Test
    void furnace(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Furnace")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        enemy.setPosition(board.getRooms().get(0).getSquares().get(0));
        enemy.getPosition().arrives(enemy);
        enemy2.setPosition(board.getRooms().get(0).getSquares().get(1));
        enemy2.getPosition().arrives(enemy2);
        enemy3.setPosition(board.getRooms().get(0).getSquares().get(2));
        enemy3.getPosition().arrives(enemy3);
        target.get(0).setTargetRoom(board.getRooms().get(0));
        try {
            owner.setPosition(board.find(1,2));
            owner.getPosition().arrives(owner);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy3.getDamage().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }

        try {
            enemy.setPosition(board.find(1,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,1));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(1,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setTargetSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy3.getDamage().get(0));
        assertEquals(owner,enemy.getMark().get(0));
        assertEquals(owner,enemy2.getMark().get(0));
        assertEquals(owner,enemy3.getMark().get(0));

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setTargetRoom(board.getRooms().get(1));
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }

        try {
            target.get(0).setTargetSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireAlternative(target));

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setTargetSquare(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireAlternative(target));
    }

    /**
     * this test verify the right and the wrong uses of Hellion
     */
    @Test
    void hellion(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Hellion")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        weapon.getPreviousTarget().add(prevPlayer);
        weapon.getPreviousTarget().add(prevPlayer);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,1));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setTargetSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getMark().get(0));
        assertEquals(owner,enemy2.getMark().get(0));
        assertEquals(owner,enemy3.getMark().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.getMark().clear();
        enemy3.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setTargetSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getMark().get(i));
            assertEquals(owner,enemy2.getMark().get(i));
            assertEquals(owner,enemy3.getMark().get(i));
        }

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.getMark().clear();
        enemy3.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        target.get(0).setEnemyPlayer(owner);
        try {
            target.get(1).setTargetSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }

        target.get(0).setEnemyPlayer(enemy);
        try {
            target.get(1).setTargetSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));

        target.get(0).setOwner(null);
        target.get(0).setEnemyPlayer(enemy);
        try {
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            target.get(1).setTargetSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(NoOwnerException.class,()->weapon.fire(target));
    }

    /**
     * this test verify the right and the wrong uses of Power Glove
     */
    @Test
    void PowerGlove(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Power Glove")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        weapon.getPreviousTarget().add(prevPlayer);
        weapon.getPreviousTarget().add(prevPlayer);
        try {
            owner.setPosition(board.find(4,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setMovement(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getMark().get(i));
        }

        owner.getPosition().leaves(owner);
        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            owner.setPosition(board.find(4,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setMovement(board.find(3,2));
            target.get(2).setEnemyPlayer(enemy2);
            target.get(3).setMovement(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(2,enemy2.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
            assertEquals(owner,enemy2.getDamage().get(i));
        }

        owner.getPosition().leaves(owner);
        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<2;i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);


    }

    /**
     * this test verify the right and the wrong uses of Railgun
     */
    @Test
    void railgun(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Railgun")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(3,enemy.getDamageCounter());
        for(int i=0;i<3;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }

        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,3));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(2,enemy2.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
            assertEquals(owner,enemy2.getDamage().get(i));
        }

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,3));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }

        assertEquals(2,enemy.getDamageCounter());
        assertEquals(2,enemy2.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
            assertEquals(owner,enemy2.getDamage().get(i));
        }

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            enemy.setPosition(board.find(1,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(3,1));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireAlternative(target));
    }

    /**
     * this test verify the right and the wrong uses of Shockwave
     */
    @Test
    void shockwave(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Shockwave")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,3));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
            target.get(2).setEnemyPlayer(enemy3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(owner,enemy3.getDamage().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.getMark().clear();
        enemy3.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            enemy.setPosition(board.find(2,3));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }

        assertEquals(1,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(owner,enemy3.getDamage().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.getMark().clear();
        enemy3.setDamageCounter(0);

        for(int i=0;!target.isEmpty();){
            target.remove(i);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));

        try {
            enemy.setPosition(board.find(2,3));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy2.getDamage().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.getMark().clear();
        enemy3.setDamageCounter(0);

        for(int i=0;!target.isEmpty();){
            target.remove(i);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));

        try {
            enemy.setPosition(board.find(2,3));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
    }

    /**
     * this test verify the right and the wrong uses of Shotgun
     */
    @Test
    void Shotgun(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Shotgun")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setMovement(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(3,enemy.getDamageCounter());
        for(int i=0;i<3;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }
        try {
            assertEquals(enemy,board.find(3,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            enemy.setPosition(board.find(2,3));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }

        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        try {
            enemy.setPosition(board.find(2,3));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setMovement(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
    }

    /**
     * this test verify the right and the wrong uses of Sledgehammer
     */
    @Test
    void sledgehammer(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Sledgehammer")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setMovement(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }

        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            owner.setPosition(board.find(2,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setMovement(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(3,enemy.getDamageCounter());
        for(int i=0;i<3;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }
        try {
            assertEquals(enemy,board.find(3,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            owner.setPosition(board.find(2,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setMovement(board.find(4,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(3,enemy.getDamageCounter());
        for(int i=0;i<3;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }
        try {
            assertEquals(enemy,board.find(4,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            owner.setPosition(board.find(2,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setMovement(board.find(3,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireAlternative(target));
    }

    /**
     * this test verify the right use of Tractor Beam
     */
    @Test
    void tractorBeam(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Tractor Beam")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(4,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setMovement(board.find(3,2));
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(board.find(3,2).getOnMe().get(0),enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        for(int i=0;i<1;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }

        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }
    }

    /**
     * this test verify the right and the wrong uses of ZX-2
     */
    @Test
    void ZX2(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("ZX2")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setMovement(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException | NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getMark().get(i));
        }

        enemy.getPosition().leaves(enemy);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);

        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        try {
            enemy.setPosition(board.find(1,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(3,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
            target.get(2).setEnemyPlayer(enemy3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(owner,enemy.getMark().get(0));
        assertEquals(owner,enemy2.getMark().get(0));
        assertEquals(owner,enemy3.getMark().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);
        enemy3.getPosition().leaves(enemy3);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);
        enemy3.getDamage().clear();
        enemy3.getMark().clear();
        enemy3.setDamageCounter(0);

        for(int i=0;!target.isEmpty();){
            target.remove(i);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));

        try {
            enemy.setPosition(board.find(1,1));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(1).setEnemyPlayer(enemy2);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(owner,enemy.getMark().get(0));
        assertEquals(owner,enemy2.getMark().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);

        enemy.getDamage().clear();
        enemy.getMark().clear();
        enemy.setDamageCounter(0);
        enemy2.getDamage().clear();
        enemy2.getMark().clear();
        enemy2.setDamageCounter(0);

        for(int i=0;!target.isEmpty();){
            target.remove(i);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        owner.setYellowAmmo(3);
        owner.setBlueAmmo(3);
        owner.setRedAmmo(3);

        target.add(new TargetParameter(null,owner,null,null,null,null));

        try {
            enemy.setPosition(board.find(1,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireAlternative(target);
        } catch (NotThisKindOfWeapon | InvalidTargetException | NoAmmoException | NoOwnerException notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        }
        assertEquals(owner,enemy.getMark().get(0));

        enemy.getPosition().leaves(enemy);
        enemy2.getPosition().leaves(enemy2);

        for(int i=0;!target.isEmpty();){
            target.remove(i);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }

        target.add(new TargetParameter(null,owner,null,null,null,null));

        try {
            enemy.setPosition(board.find(4,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireAlternative(target));
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
    }
}
