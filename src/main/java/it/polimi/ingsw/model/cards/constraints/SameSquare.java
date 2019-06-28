package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;
import java.util.ArrayList;

/**
 * this class check if the targeted square is the same square of another square
 */
public class SameSquare extends Constraint {
    /**
     * true if is needed to be check the position of previous targets and the actual target, false if is needed to be check the position of the Owner and the actual target
     */
    private boolean controlEnemy;

    /**
     * constructor of SameSquare
     * @param controlEnemy true if is needed to be check the position of previous targets and the actual target, false if is needed to be check the position of the Owner and the actual target
     * @param level of the previousTarget control
     */
    public SameSquare(boolean controlEnemy, int level) {
        super(level);
        this.controlEnemy = controlEnemy;
    }

    /**
     * this method evaluates if the targeted square is the same square of another square
     * @param target contains a enemy or a square
     * @param constraintPositivity true if the squares must be the same square, false if the squares must be different squares
     * @param previousTarget is the list of targets of the previous effects
     * @return true if the player can shoot, false if the player can't shoot
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException {
        if(target.getConstraintSquare() == null){
            return false;
        }
        ArrayList<Square> allTarget = new ArrayList<Square>();
        allTarget.add(target.getConstraintSquare());
        if(controlEnemy){
            for(int j=previousTarget.get(getLevel()).size();j>0;j--){
                allTarget.add(previousTarget.get(getLevel()).get(j-1).getPosition());
            }
        }
        else {
            if((target.getOwner()==null)||(target.getOwner().getPosition()==null)){
            throw new NoOwnerException();
            }
            allTarget.add(target.getOwner().getPosition());
        }
        for(int i=1;i<allTarget.size();i++){
            if((allTarget.get(0)!=allTarget.get(i))==constraintPositivity){
                return false;
            }
        }
        return true;
    }
}