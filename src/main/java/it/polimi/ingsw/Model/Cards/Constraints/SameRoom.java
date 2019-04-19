package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class SameRoom extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target) {
        if(target.getTargetRoom() == target.getOwner().getPosition().getRoom()){
            return true;
        }
        return false;
    }
}
