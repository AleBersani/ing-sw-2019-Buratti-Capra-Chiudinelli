package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.TargetParameter;

public class MaximumDistance extends Constraint {

    private int distance;

    public MaximumDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean canShoot(TargetParameter target) {
        for (Square s : target.getConstraintSquareList()) {
            if (target.getOwner().getPosition().calcDist(s) > this.distance) {
                return false;
            }
        }
        return true;
    }
}

