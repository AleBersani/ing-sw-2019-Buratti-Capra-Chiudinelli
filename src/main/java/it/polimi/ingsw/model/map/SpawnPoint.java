package it.polimi.ingsw.model.map;

import it.polimi.ingsw.exception.ElementNotFoundException;
import it.polimi.ingsw.model.cards.Weapon;

import java.util.ArrayList;

public class SpawnPoint extends Square {

    private ArrayList<Weapon> weapons= new ArrayList<Weapon>();

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public SpawnPoint(int x, int y, String color, Room room) {
        super(x, y, color, room);
    }

    @Override
    public void generate(){
        weapons.add(super.getRoom().getBoard().nextWeapon());
        return;
    }

    @Override
    public AmmoTile grabAmmo() throws ElementNotFoundException {
        throw new ElementNotFoundException ();
    }

    @Override
    public Weapon grabWeapon( int position) throws ElementNotFoundException {
        Weapon temp;
        temp=this.weapons.get(position);
        this.weapons.remove(position);
        return temp;
    }

    @Override
    public boolean require() {
        return weapons.size()<3;
    }
}
