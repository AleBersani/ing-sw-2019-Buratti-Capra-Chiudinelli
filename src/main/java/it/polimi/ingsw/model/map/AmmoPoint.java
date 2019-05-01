package it.polimi.ingsw.model.map;

import it.polimi.ingsw.exception.ElementNotFoundException;
import it.polimi.ingsw.model.cards.Weapon;

/**
 * This class represent a square that is not a spawnPoint and with AmmoTile on it
 */
public class AmmoPoint extends Square {

    private AmmoTile ammo;

    /**
     * this method generate a AmmoPoint
     * @param x is the x coordinate of the square
     * @param y is the y coordinate of the square
     * @param color is the color of the square
     * @param room is the room in which is positioned this square
     */
    public AmmoPoint(int x, int y, String color, Room room) {
        super(x, y, color, room);
        ammo=null;
    }

    public AmmoTile getAmmo() {
        return ammo;
    }

    /**
     * this method add to this an AmmoTile
     */
    @Override
    public void generate(){
        this.ammo=super.getRoom().getBoard().nextAmmo();
        return;
    }

    /**
     * this method return the ammoTile on this
     * @return the ammoTile on this, if there are not ammoTile on this return null
     */
    @Override
    public AmmoTile grabAmmo(){
        AmmoTile temp;
        temp=this.ammo;
        this.ammo=null;
        return temp;
    }

    /**
     * this method throws an ElementNotFoundException
     * @param position
     * @return
     * @throws ElementNotFoundException
     */
    @Override
    public Weapon grabWeapon(int position) throws ElementNotFoundException {
        throw new ElementNotFoundException();
    }

    /**
     * this method return if this square need a new AmmoTile
     * @return true if this square need a new AmmoTile
     */
    @Override
    public boolean require() {
        return this.ammo==null;
    }

    public void setAmmo(AmmoTile ammo) {
        this.ammo = ammo;
    }
}
