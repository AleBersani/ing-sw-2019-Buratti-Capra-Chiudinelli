package it.polimi.ingsw.Map;

import it.polimi.ingsw.Cards.Weapon;

import java.util.ArrayList;

public class SpawnPoint extends Square {

    private ArrayList<Weapon> weapons= new ArrayList<Weapon>();

    public SpawnPoint(int x, int y, ArrayList<Square> doors, String color, Room room, ArrayList<Weapon> weapons) {
        super(x, y, doors, color, room);
        this.weapons = weapons;
    }

    public void generateWeapon(){

        return;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
