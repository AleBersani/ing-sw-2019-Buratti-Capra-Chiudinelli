package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Map.Square;

public class MinimumDistance extends Constraint {

    private int distance;

    public MinimumDistance(int distance) {
        this.distance = distance;
    }

    public boolean canShoot(Square target, Player owner){

        return false;
    }
}
