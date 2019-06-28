package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.map.Square;

import java.util.ArrayList;

/**
 * this class check the distance between the Owner and the Target or between the Target and the previous target
 */
public class MinimumDistance extends Constraint {
    /**
     * is the distance of shooting
     */
    private int distance;
    /**
     * true if is needed to verify the distance between the Target and the previous Targets, false if is needed to verify the distance between the Owner and the Target
     */
    private boolean concatenate;

    /**
     * constructor of AdjacentRoom
     * @param distance is the distance of shooting
     * @param concatenate true if is needed to verify the distance between the Target and the previous Targets, false if is needed to verify the distance between the Owner and the Target
     * @param level of the previousTarget control
     */
    public MinimumDistance(int distance, boolean concatenate, int level) {
        super(level);
        this.distance = distance;
        this.concatenate = concatenate;
    }

    /**
     * this method evaluates if the player is far or close enough from another player or square
     * @param target contain a player o a square
     * @param constraintPositivity true if is needed to control a minimum distance, false if is needed to control a maximum distance
     * @param previousTarget is the list of targets of the previous effects
     * @return true if the player can shoot, false if the player can't shoot
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException {
        ArrayList<Square> allTarget = new ArrayList<Square>();
        if(target.getConstraintSquare() == null){
            return false;
        }
        allTarget.add(target.getConstraintSquare());
        if(!concatenate){
            if((target.getOwner()==null)||(target.getOwner().getPosition()==null)){
                throw new NoOwnerException();
            }
            allTarget.add(target.getOwner().getPosition());
        }
        else {
            for(int j=previousTarget.get(getLevel()).size();j>0;j--){
                allTarget.add(previousTarget.get(getLevel()).get(j-1).getPosition());
            }
        }
        for(int i=1;i<allTarget.size();i++){
            if((allTarget.get(i).calcDist(allTarget.get(i-1))<this.distance)==constraintPositivity) {
                return false;
            }
        }
        return true;
    }
}