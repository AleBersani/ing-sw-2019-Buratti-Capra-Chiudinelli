package it.polimi.ingsw.Map;

import it.polimi.ingsw.AmmoTile;

import java.util.ArrayList;

public class AmmoPoint extends Square {

    private AmmoTile ammo;

    public AmmoPoint(int x, int y, ArrayList<Square> doors, String color) {
        super(x, y, doors, color);
    }

    public void generateAmmo(){

        return;
    }
}
