package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.map.Square;

import java.util.ArrayList;

public class MinimumDistance extends Constraint {

    private int distance;

    public MinimumDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget) {
        ArrayList<Square> allTarget = new ArrayList<Square>();
        for(Player previousPlayer: previousTarget){
            allTarget.add(previousPlayer.getPosition());
        }
        allTarget.add(target.getConstraintSquare());
        for(Square targetSquare: allTarget){
            if((target.getOwner().getPosition().calcDist(targetSquare)<this.distance)==constraintPositivity) {
                return false;
            }
        }
        return true;
    }
}
