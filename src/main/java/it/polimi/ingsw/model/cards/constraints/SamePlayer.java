package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import java.util.ArrayList;

public class SamePlayer extends Constraint {

    public SamePlayer(int level) {
        super(level);
    }

    //guarda se il bersaglio finale è già contenuto nel previous target
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) {
        if(previousTarget.get(getLevel()).contains(target.getEnemyPlayer())==constraintPositivity){
            return true;
        }
        return false;
    }
}
