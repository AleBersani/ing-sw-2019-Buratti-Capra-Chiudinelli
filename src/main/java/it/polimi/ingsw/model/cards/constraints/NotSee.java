package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

public class NotSee extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target) {

        for (Square s : target.getConstraintSquareList()) {
            if(target.getOwner().getPosition().getRoom()!= s.getRoom()){
                for(Square d: target.getOwner().getPosition().getDoors()){
                    if(d.getRoom()== s.getRoom()){
                        return false;
                    }
                }
            }
            else{
                return false;
            }
        }
        return true;
    }
}
