package it.polimi.ingsw.Map;

import it.polimi.ingsw.Cards.Weapon;

import java.util.ArrayList;

public class SpawnPoint extends Square {

    private ArrayList<Weapon> weapons= new ArrayList<Weapon>();

    public SpawnPoint(int x, int y, ArrayList<Square> doors, String color, Room room) {
        super(x, y, doors, color, room);
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
