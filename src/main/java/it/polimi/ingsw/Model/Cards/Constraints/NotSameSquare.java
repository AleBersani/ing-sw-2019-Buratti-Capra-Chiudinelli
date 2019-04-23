package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.TargetParameter;

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
