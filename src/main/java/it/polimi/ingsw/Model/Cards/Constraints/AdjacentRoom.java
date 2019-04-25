package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.TargetParameter;

public class AdjacentRoom extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target) {
        if(target.getTargetRoom() == target.getOwner().getPosition().getRoom()){
            return false;
        }
        for(Square square: target.getOwner().getPosition().getDoors()){
            if(square.getRoom() == target.getTargetRoom()){
                return true;
            }
        }
        return false;
    }
}
