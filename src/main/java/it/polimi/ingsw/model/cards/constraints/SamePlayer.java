package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import java.util.ArrayList;

/**
 * this class check if the targeted enemy are the same player of the previous effect
 */
public class SamePlayer extends Constraint {
    /**
     * constructor class of SamePlayer
     * @param level of the previousTarget control
     */
    public SamePlayer(int level) {
        super(level);
    }

    /**
     * this method evaluates if the targeted enemy are the same player of the previous effect
     * @param target contain a player
     * @param constraintPositivity true if both players need to be the same player, false if they have to be different players
     * @param previousTarget is the list of targets of the previous effects
     * @return true if the player can shoot, false if the player can't shoot
     */
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) {
        if(previousTarget.get(getLevel()).contains(target.getEnemyPlayer())==constraintPositivity){
            return true;
        }
        return false;
    }
}
