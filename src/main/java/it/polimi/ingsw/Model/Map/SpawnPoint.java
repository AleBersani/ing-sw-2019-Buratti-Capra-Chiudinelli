package it.polimi.ingsw.Model.Map;

import it.polimi.ingsw.Model.Cards.Weapon;

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
    public boolean require() {
        return weapons.size()<3;
    }
}
