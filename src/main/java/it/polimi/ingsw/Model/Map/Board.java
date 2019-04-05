package it.polimi.ingsw.Model.Map;

import it.polimi.ingsw.Model.Cards.PowerUp;
import it.polimi.ingsw.Model.Cards.Weapon;

import java.util.ArrayList;

public class Board {
    private ArrayList<Room> rooms= new ArrayList<Room>();
    private ArrayList<AmmoTile> ammoList= new ArrayList<AmmoTile>();
    private ArrayList<PowerUp> powerUpList= new ArrayList<PowerUp>();
    private ArrayList<Weapon> weaponsList= new ArrayList<Weapon>();

    public Square find(int x, int y){
        // TODO
        return null;
    }

    public Weapon nextWeapon(){
        // TODO
        return null;
    }
    public AmmoTile nextAmmo(){
        // TODO
        return null;
    }
    public PowerUp nextPowerUp(){
        // TODO
        return null;
    }
}
