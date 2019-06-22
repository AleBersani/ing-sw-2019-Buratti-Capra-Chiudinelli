package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WeaponOptionalTest {

    Weapon weapon;
    Player owner;
    Player enemy,enemy2,enemy3;
    Board board;
    ArrayList<TargetParameter> target;
    ArrayList<Player> prevPlayer;

    @BeforeEach
    void setup(){
        owner = new Player(true,"red","Luciano");
        enemy = new Player(false,"blue", "Fabiano");
        enemy2 = new Player(false,"red", "Fabiolo");
        enemy3 = new Player(false,"red", "Fagiolo");
        board = new Board(null,"/Board/Board1.json");
        target = new ArrayList<TargetParameter>();
        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));
        target.add(new TargetParameter(null,owner,null,null,null,null));
        prevPlayer = new ArrayList<Player>();
    }

    @Test
    void cyberblade_Base_Movement_Optional_1() {
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Cyberblade")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setMovement(board.find(3,1));
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(owner,board.find(3,1).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(3,1));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setConstraintSquare(enemy2.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy2.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy2.getDamage().get(i));
        }

    }

    @Test
    void cyberblade_Movement_Base_Optional_1() {
        for (Weapon w : board.getWeaponsListCopy()) {
            if (w.getName().equals("Cyberblade")) {
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2, 1));
            owner.getPosition().arrives(owner);
            target.get(0).setMovement(board.find(3,1));
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(owner,board.find(3,1).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy.setPosition(board.find(3,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(3,1));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setConstraintSquare(enemy2.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy2.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy2.getDamage().get(i));
        }
    }

    @Test
    void cyberblade_Base_Optional_1_Movement(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Cyberblade")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(2,1));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setConstraintSquare(enemy2.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy2.getDamageCounter());
        for(int i=0;i<2;i++){
            assertEquals(owner,enemy2.getDamage().get(i));
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setMovement(board.find(3,1));
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(owner,board.find(3,1).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void cyberblade_InvalidTarget(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Cyberblade")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setMovement(board.find(3,2));
            target.get(0).setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
    }

    @Test
    void grenadeLauncher_Base_Movement_Optional_0(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Grenade Launcher")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(3,1));
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(enemy,board.find(3,1).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(3,1));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(3,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setTargetSquare(enemy2.getPosition());
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(owner,enemy3.getDamage().get(0));
    }

    @Test
    void grenadeLauncher_Base_Optional_0_Movement(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Grenade Launcher")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(2,1));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setTargetSquare(enemy2.getPosition());
            target.get(0).setConstraintSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(0,owner.getDamageCounter());
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(owner,enemy3.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(3,1));
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(enemy,board.find(3,1).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    void grenadeLauncher_Separate_Base_Optional(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Grenade Launcher")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(2,1));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setTargetSquare(enemy2.getPosition());
            target.get(0).setConstraintSquare(board.find(2,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(0,owner.getDamageCounter());
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(owner,enemy3.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(3,2));
            target.get(0).setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(enemy,board.find(3,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void grenadeLauncher_InvalidTarget(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Grenade Launcher")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
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
        try {
            enemy2.setPosition(board.find(2,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(2,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setTargetSquare(enemy2.getPosition());
            target.get(0).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy.setPosition(board.find(3,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(1,1));
            target.get(0).setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,1));
    }

    @Test
    void lock_rifle(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Lock rifle")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        assertEquals(owner,enemy.getMark().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(2,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(owner,enemy2.getMark().get(0));
    }

    @Test
    void lock_rifle_invalid(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Lock rifle")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(4,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(4,2));
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
        try {
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        assertEquals(owner,enemy.getMark().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(2,3));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setConstraintSquare(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
    }

    @Test
    void machine_Gun_2target_firstTarget_secondTarget_thirdTarget(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Machine Gun")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy2.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy);
        target.get(0).setConstraintSquare(enemy.getPosition());
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setConstraintSquare(enemy2.getPosition());
            target.get(1).setEnemyPlayer(enemy3);
            target.get(1).setConstraintSquare(enemy3.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,3);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(1));
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy3.getDamage().get(0));
    }

    @Test
    void machine_Gun_2target_secondTarget_firstTarget_thirdTarget(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Machine Gun")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy2.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy2);
        target.get(0).setConstraintSquare(enemy2.getPosition());
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(enemy.getPosition());
            target.get(1).setEnemyPlayer(enemy3);
            target.get(1).setConstraintSquare(enemy3.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,3);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(1));
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy3.getDamage().get(0));
    }

    @Test
    void machine_Gun_2target_secondTarget_thirdTarget(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Machine Gun")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy2.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy2);
        target.get(0).setConstraintSquare(enemy2.getPosition());
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(1));
        for(int i=0;!target.isEmpty();){
            target.remove(i);
        }
        for(int i=0;i<weapon.getPreviousTarget().size();i++){
            weapon.getPreviousTarget().get(i).clear();
        }
        target.add(new TargetParameter(null,owner,null,null,null,null));

        try {
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy3);
            target.get(0).setConstraintSquare(enemy3.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,2);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy3.getDamage().get(0));


    }

    @Test
    void machine_Gun_InvalidTarget(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Machine Gun")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,1));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(1,1));
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
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy2.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy3);
            target.get(0).setConstraintSquare(enemy3.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy);
        target.get(0).setConstraintSquare(enemy.getPosition());
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy);
        target.get(0).setConstraintSquare(enemy.getPosition());
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,1));
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,2));

    }

    @Test
    void plasma_Gun(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Plasma Gun")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setMovement(board.find(1,2));
            target.get(0).setConstraintSquare(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(owner,board.find(1,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy);
        target.get(0).setConstraintSquare(enemy.getPosition());
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(3,enemy.getDamageCounter());
        for(int i=0;i<3;i++){
            assertEquals(owner,enemy.getDamage().get(i));
        }
    }

    @Test
    void plasma_Gun_InvalidTarget(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Plasma Gun")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
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
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,1));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setMovement(board.find(1,2));
            target.get(0).setConstraintSquare(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(owner,board.find(1,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(owner);
        target.get(0).setConstraintSquare(owner.getPosition());
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,1));
    }

    @Test
    void rocket_Launcher_First_SquareDamage_FirstMovement_OwnerMovement(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Rocket Launcher")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(3,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setTargetSquare(enemy.getPosition());
        target.get(0).setConstraintSquare(enemy.getPosition());
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(3,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        assertEquals(owner,enemy.getDamage().get(2));
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(owner,enemy3.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(4,2));
            target.get(0).setConstraintSquare(board.find(4,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,2);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(enemy,board.find(4,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(owner,board.find(2,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void rocket_Launcher_OwnerMovement_First_SquareDamage_FirstMovement(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Rocket Launcher")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            target.get(0).setMovement(board.find(1,2));
            target.get(0).setConstraintSquare(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(owner,board.find(1,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(3,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setTargetSquare(enemy.getPosition());
        target.get(0).setConstraintSquare(enemy.getPosition());
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(3,enemy.getDamageCounter());
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        assertEquals(owner,enemy.getDamage().get(2));
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(owner,enemy3.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(4,2));
            target.get(0).setConstraintSquare(board.find(4,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,2);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(enemy,board.find(4,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void rocket_Launcher_Invalid(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Rocket Launcher")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(3,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(3,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(board.find(3,2));
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
        try {
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setMovement(board.find(1,1));
            target.get(0).setConstraintSquare(board.find(1,1));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(owner,board.find(1,1).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy);
        target.get(0).setConstraintSquare(enemy.getPosition());
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setTargetSquare(owner.getPosition());
        target.get(0).setConstraintSquare(owner.getPosition());
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(4,3));
            target.get(0).setConstraintSquare(board.find(4,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,2));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setMovement(board.find(4,3));
            target.get(0).setConstraintSquare(board.find(4,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,2));

    }

    @Test
    void thor_First_Second_Third(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("THOR")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(3,3));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(4,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(3,1));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy2);
        target.get(0).setConstraintSquare(enemy2.getPosition());
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy3);
        target.get(0).setConstraintSquare(enemy3.getPosition());
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
    }

    @Test
    void thor_InvalidTarget(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("THOR")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(3,3));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(enemy.getPosition());
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
        try {
            enemy.setPosition(board.find(4,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(1,1));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setConstraintSquare(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        target.get(0).setEnemyPlayer(enemy2);
        target.get(0).setConstraintSquare(enemy2.getPosition());
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.setPosition(board.find(1,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setConstraintSquare(enemy2.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy3.setPosition(board.find(3,3));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy3);
            target.get(0).setConstraintSquare(enemy3.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy3.setPosition(board.find(1,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy3);
            target.get(0).setConstraintSquare(enemy3.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,1);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
    }

    @Test
    void Vortex_Cannon(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Vortex Cannon")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,3));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(1,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(enemy,board.find(2,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setMovement(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(2).setEnemyPlayer(enemy3);
            target.get(2).setMovement(board.find(2,2));
            target.get(3).setEnemyPlayer(enemy3);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fireOptional(target,0);
        } catch (NotThisKindOfWeapon notThisKindOfWeapon) {
            notThisKindOfWeapon.printStackTrace();
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoAmmoException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertTrue(board.find(2,2).getOnMe().contains(enemy2));
            assertTrue(board.find(2,2).getOnMe().contains(enemy3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(1,enemy2.getDamageCounter());
        assertEquals(1,enemy3.getDamageCounter());
        assertEquals(owner,enemy2.getDamage().get(0));
        assertEquals(owner,enemy3.getDamage().get(0));
    }

    @Test
    void Vortex_Cannon_InvalidTarget_BaseEffect(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Vortex Cannon")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(2,1));
            target.get(1).setEnemyPlayer(enemy);
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
        try {
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
    }

    @Test
    void Vortex_Cannon_InvalidTarget_OptionalEffect(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Vortex Cannon")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,2));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(3,2));
            enemy.getPosition().arrives(enemy);
            enemy2.setPosition(board.find(2,3));
            enemy2.getPosition().arrives(enemy2);
            enemy3.setPosition(board.find(1,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setConstraintSquare(enemy.getPosition());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        try {
            weapon.fire(target);
        } catch (InvalidTargetException e) {
            e.printStackTrace();
        } catch (NoOwnerException e) {
            e.printStackTrace();
        }
        try {
            assertEquals(enemy,board.find(2,2).getOnMe().get(0));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(2,enemy.getDamageCounter());
        assertEquals(owner,enemy.getDamage().get(0));
        assertEquals(owner,enemy.getDamage().get(1));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy);
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy);
            target.get(1).setConstraintSquare(board.find(2,2));
            target.get(2).setEnemyPlayer(enemy3);
            target.get(2).setMovement(board.find(2,2));
            target.get(2).setConstraintSquare(board.find(2,2));
            target.get(3).setEnemyPlayer(enemy3);
            target.get(3).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(2,2));
            target.get(2).setEnemyPlayer(enemy);
            target.get(2).setMovement(board.find(2,2));
            target.get(2).setConstraintSquare(board.find(2,2));
            target.get(3).setEnemyPlayer(enemy);
            target.get(3).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setMovement(board.find(2,3));
            target.get(0).setConstraintSquare(board.find(2,3));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(2,3));
            target.get(2).setEnemyPlayer(enemy3);
            target.get(2).setMovement(board.find(2,2));
            target.get(2).setConstraintSquare(board.find(2,2));
            target.get(3).setEnemyPlayer(enemy3);
            target.get(3).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.getPosition().leaves(enemy2);
            enemy2.setPosition(board.find(4,2));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(2,2));
            target.get(2).setEnemyPlayer(enemy3);
            target.get(2).setMovement(board.find(2,2));
            target.get(2).setConstraintSquare(board.find(2,2));
            target.get(3).setEnemyPlayer(enemy3);
            target.get(3).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.getPosition().leaves(enemy2);
            enemy2.setPosition(board.find(2,3));
            enemy2.getPosition().arrives(enemy2);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(2,2));
            target.get(2).setEnemyPlayer(enemy3);
            target.get(2).setMovement(board.find(2,3));
            target.get(2).setConstraintSquare(board.find(2,3));
            target.get(3).setEnemyPlayer(enemy3);
            target.get(3).setConstraintSquare(board.find(2,3));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
        for(TargetParameter targetParameter: target){
            targetParameter.setConstraintSquare(null);
            targetParameter.setEnemyPlayer(null);
            targetParameter.setTargetSquare(null);
            targetParameter.setTargetRoom(null);
            targetParameter.setMovement(null);
        }
        try {
            enemy2.getPosition().leaves(enemy2);
            enemy2.setPosition(board.find(2,3));
            enemy2.getPosition().arrives(enemy2);
            enemy3.getPosition().leaves(enemy3);
            enemy3.setPosition(board.find(4,2));
            enemy3.getPosition().arrives(enemy3);
            target.get(0).setEnemyPlayer(enemy2);
            target.get(0).setMovement(board.find(2,2));
            target.get(0).setConstraintSquare(board.find(2,2));
            target.get(1).setEnemyPlayer(enemy2);
            target.get(1).setConstraintSquare(board.find(2,2));
            target.get(2).setEnemyPlayer(enemy3);
            target.get(2).setMovement(board.find(2,2));
            target.get(2).setConstraintSquare(board.find(2,2));
            target.get(3).setEnemyPlayer(enemy3);
            target.get(3).setConstraintSquare(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fireOptional(target,0));
    }
}