package it.polimi.ingsw.model.map;

import it.polimi.ingsw.exception.ElementNotFoundException;
import it.polimi.ingsw.model.cards.Weapon;

import java.util.ArrayList;

/**
 * This class represent a SpawnPoint
 */
public class SpawnPoint extends Square {
    /**
     * list of weapon in the store
     */
    private ArrayList<Weapon> weapons;

    /**
     * getter method of the list of weapons in the store of this square
     * @return list of weapons in the store
     */
    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * this method generate a AmmoPoint
     * @param x is the x coordinate of the square
     * @param y is the y coordinate of the square
     * @param color is the color of the square
     * @param room is the room in which is positioned this square
     */
    public SpawnPoint(int x, int y, String color, Room room) {
        super(x, y, color, room);
        weapons= new ArrayList<>();
    }

    /**
     * this method add to this a weapon
     */
    @Override
    public void generate(){
        weapons.add(super.getRoom().getBoard().nextWeapon());
    }

    /**
     * this method throws an ElementNotFoundException
     * @return an exception because in this type of square there aren't ammoTile
     * @throws ElementNotFoundException because in this type of square there aren't ammoTile
     */
    @Override
    public AmmoTile grabAmmo() throws ElementNotFoundException {
        throw new ElementNotFoundException ();
    }

    /**
     * this method return a weapon on this
     * @param position is the index of what weapon needs to be returned
     * @return the weapon on this at the index position, if there are not weapon on that index return null
     */
    @Override
    public Weapon grabWeapon( int position) {
        Weapon temp;
        temp=this.weapons.get(position);
        this.weapons.remove(position);
        return temp;
    }

    /**
     * this method return if this square need a new weapon
     * @return true if this square need a new weapon
     */
    @Override
    public boolean require() {
        return weapons.size()<3;
    }
}
