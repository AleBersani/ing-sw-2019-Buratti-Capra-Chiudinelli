package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import java.util.ArrayList;

public class SamePlayer extends Constraint {

    //guarda se il bersaglio finale è già contenuto nel previous target
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<Player> previousTarget) {
        if(previousTarget.contains(target.getEnemyPlayer())==constraintPositivity){
            return true;
        }
        return false;
    }
}
