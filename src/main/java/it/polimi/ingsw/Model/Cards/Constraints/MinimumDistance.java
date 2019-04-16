package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;

import java.util.ArrayList;

public class MinimumDistance extends Constraint {

    private int distance;

    public MinimumDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean canShoot(ArrayList<Square> targets, Player owner) {
        for (Square s : targets) {
            if (owner.getPosition().calcDist(s) < this.distance) {
                return false;
            }
        }
        return true;
    }
}
