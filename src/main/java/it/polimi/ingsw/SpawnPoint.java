package it.polimi.ingsw;

import java.util.ArrayList;

public class SpawnPoint extends Square {

    private ArrayList<Weapon> weapons= new ArrayList<Weapon>();

    public SpawnPoint(int x, int y, ArrayList<Square> doors, String color, ArrayList<Weapon> weapons) {
        super(x, y, doors, color);
        this.weapons = weapons;
    }

    public void generateWeapon(){

        return;
    }
}
