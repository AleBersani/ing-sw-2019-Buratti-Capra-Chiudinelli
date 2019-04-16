package it.polimi.ingsw.Model.Map;

import it.polimi.ingsw.Exception.ElementNotFoundException;
import it.polimi.ingsw.Model.Cards.Weapon;

public class AmmoPoint extends Square {

    private AmmoTile ammo;

    public AmmoPoint(int x, int y, String color, Room room) {
        super(x, y, color, room);
        ammo=null;
    }

    public AmmoTile getAmmo() {
        return ammo;
    }

    @Override
    public void generate(){
        this.ammo=super.getRoom().getBoard().nextAmmo();
        return;
    }

    @Override
    public AmmoTile grabAmmo() throws ElementNotFoundException {
        AmmoTile temp;
        temp=this.ammo;
        this.ammo=null;
        return temp;
    }

    @Override
    public Weapon grabWeapon(int position) throws ElementNotFoundException {
        throw new ElementNotFoundException();
    }

    @Override
    public boolean require() {
        return this.ammo==null;
    }

    public void setAmmo(AmmoTile ammo) {
        this.ammo = ammo;
    }
}
