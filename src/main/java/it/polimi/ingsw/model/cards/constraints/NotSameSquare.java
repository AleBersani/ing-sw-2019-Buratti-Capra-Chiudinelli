package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class NotSameSquare extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget) {

        return true;
    }
}
