package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Player;

public class SameRoom extends Constraint {

    public boolean canShoot(Room target, Player owner) {
        if (target == owner.getPosition().getRoom()){
            return true;
     }else {
            return false;
        }
    }
}
