package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class SameSquare extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget) {
        /*
        for (Square s : target.getConstraintSquareList()) {
            if(s != target.getOwner().getPosition()){
                return false;
            }
        }
        */
        return true;
    }
}