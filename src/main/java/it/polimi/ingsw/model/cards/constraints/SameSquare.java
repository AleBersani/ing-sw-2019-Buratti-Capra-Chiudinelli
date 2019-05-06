package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.TargetParameter;
import java.util.ArrayList;

public class SameSquare extends Constraint {

    private boolean controlEnemy;

    public SameSquare(boolean controlEnemy, int level) {
        super(level);
        this.controlEnemy = controlEnemy;
    }

    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException {
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