package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;

public class MaxDistance extends Constraint {

    private int distance;

    public MaxDistance(int distance){ this.distance = distance; }

    public boolean canShoot(Square target, Player owner){
        if(owner.getPosition().calcDist(target) <= this.distance){
            return true;
        }
        return false;
    }
}
