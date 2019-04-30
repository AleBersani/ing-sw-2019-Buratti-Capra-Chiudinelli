package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

public class MinimumDistance extends Constraint {

    private int distance;

    public MinimumDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean canShoot(TargetParameter target) {
        for (Square s : target.getConstraintSquareList()) {
            if (target.getOwner().getPosition().calcDist(s) < this.distance) {
                return false;
            }
        }
        return true;
    }
}
