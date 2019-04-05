package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Room;

public class SameRoom extends Constraint {

    public boolean canShoot(Room target, Player owner){

        return false;
    }
}
