package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

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

