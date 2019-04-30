package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

public class NotSameSquare extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target) {
        for (Square s : target.getConstraintSquareList()) {
            if(s == target.getOwner().getPosition()){
                return false;
            }
        }
        return true;
    }
}
