package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class SameSquare extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget) {
        ArrayList<Square> allTarget = new ArrayList<Square>();
        for(Player previousPlayer: previousTarget){
            allTarget.add(previousPlayer.getPosition());
        }
        allTarget.add(target.getConstraintSquare());
        for (Square targetSquare : allTarget) {
            if((targetSquare != target.getOwner().getPosition())==constraintPositivity){
                return false;
            }
        }
        return true;
    }
}