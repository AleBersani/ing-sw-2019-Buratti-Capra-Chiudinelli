package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotFoundException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.map.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test class of base weapons
 */
class WeaponBaseTest {
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
     * board of the game
     */
    private Board board;
    /**
     * parameters of weapon's targets
     */
    private ArrayList<TargetParameter> target;

    /**
     * builder method of the parameters needed for every tests
     */
    @BeforeEach
    void setup(){
        owner = new Player(true,"red","Luciano");
        enemy = new Player(false,"blue", "Fabiano");
        board = new Board(null,"/Board/Board1.json");
        target = new ArrayList<>();
        target.add(new TargetParameter(null,owner,enemy,null,null,null));
    }

    /**
     * this test verify the right and the wrong uses of Heatseeker
     */
    @Test
    void heatseeker() {
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Heatseeker")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(2,1));
            board.find(2,1).arrives(owner);
            enemy.setPosition(board.find(2,2));
            board.find(2,2).arrives(enemy);
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
        try {
            enemy.setPosition(board.find(1,1));
            enemy.getPosition().arrives(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
    }

    /**
     * this test verify the right and the wrong uses of Whisperer
     */
    @Test
    void whisper(){
        for(Weapon w: board.getWeaponsListCopy()){
            if(w.getName().equals("Whisper")){
                weapon = w;
            }
        }
        weapon.setOwner(owner);
        try {
            owner.setPosition(board.find(1,1));
            owner.getPosition().arrives(owner);
            enemy.setPosition(board.find(2,2));
            enemy.getPosition().arrives(enemy);
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
        int i=0;
        for(Player player: enemy.getMark()){
            if(player==owner){
                i++;
            }
        }
        assertEquals(1,i);
        assertEquals(owner,enemy.getMark().get(0));
        enemy.getPosition().leaves(enemy);
        try {
            enemy.setPosition(board.find(2,1));
            enemy.getPosition().arrives(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
        enemy.getPosition().leaves(enemy);
        try {
            enemy.setPosition(board.find(4,2));
            enemy.getPosition().arrives(enemy);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->weapon.fire(target));
    }
}