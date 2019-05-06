package it.polimi.ingsw.model.cards.constraints;

import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.map.Square;

import java.util.ArrayList;

public class MinimumDistance extends Constraint {

    private int distance;
    private boolean concatenate;

    public MinimumDistance(int distance, boolean concatenate, int level) {
        super(level);
        this.distance = distance;
        this.concatenate = concatenate;
    }

    //controlla che ci sia una distanza minima tra owner e i giocatori bersagliati uno ad uno, concatenato
    @Override
    public boolean canShoot(TargetParameter target, boolean constraintPositivity, ArrayList<ArrayList<Player>> previousTarget) throws NoOwnerException {
        ArrayList<Square> allTarget = new ArrayList<Square>();
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