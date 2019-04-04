package it.polimi.ingsw.Map;

import java.util.ArrayList;

public class AmmoPoint extends Square {

    private AmmoTile ammo;

    public AmmoPoint(int x, int y, ArrayList<Square> doors, String color, Room room, AmmoTile ammo) {
        super(x, y, doors, color, room);
        this.ammo = ammo;
    }

    public void generateAmmo(){

        return;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
