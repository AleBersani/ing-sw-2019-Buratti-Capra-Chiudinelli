package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Map.Room;

public class SameRoom extends Constraint {

    public boolean canShoot(Room target, Player owner){

        return false;
    }
}
