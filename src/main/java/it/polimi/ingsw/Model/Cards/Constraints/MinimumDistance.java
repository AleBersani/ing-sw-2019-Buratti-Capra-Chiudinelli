package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

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
