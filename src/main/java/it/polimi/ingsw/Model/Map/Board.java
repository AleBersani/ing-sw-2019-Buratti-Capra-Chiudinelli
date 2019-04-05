package it.polimi.ingsw.Model.Map;

import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.PowerUp;
import it.polimi.ingsw.Model.Cards.Weapon;
import it.polimi.ingsw.Model.Match;

import java.util.ArrayList;

public class Board {
    private ArrayList<Room> rooms= new ArrayList<Room>();
    private ArrayList<AmmoTile> ammoList= new ArrayList<AmmoTile>();
    private ArrayList<PowerUp> powerUpList= new ArrayList<PowerUp>();
    private ArrayList<Weapon> weaponsList= new ArrayList<Weapon>();
    private Match match;

    public Board(Match match){
        this.match=match;

        //TODO
    }

    public Square find(int x, int y) throws NotFoundException {

        for (Room r : rooms){
            try {
                return r.find(x,y);
            } catch (Exception e) {}
        }
        throw (new NotFoundException());
    }

    public Weapon nextWeapon(){
        Weapon w;
        if (weaponsList.size()>=0) {
            w = weaponsList.get(weaponsList.size());
            weaponsList.remove(weaponsList.size());
            return w;
        }
        return null;
    }


    public AmmoTile nextAmmo(){
        AmmoTile a;
        a= ammoList.get(ammoList.size());
        ammoList.remove(ammoList.size());
        return a;
    }


    public PowerUp nextPowerUp(){
        PowerUp p;
        p= powerUpList.get(powerUpList.size());
        powerUpList.remove(powerUpList.size());
        return p;
    }
    private void reShuffleAmmo(){
        //TODO
    }

    private void ReShufflePuwerUps(){
        //TODO
    }

    private void addDoors(){
        //TODO
    }

}