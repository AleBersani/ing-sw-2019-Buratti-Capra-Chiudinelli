package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class EffectVsPlayer extends Effect {

    private int damage,mark;
    private boolean addToList,removeFromList;

    public EffectVsPlayer(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints,ArrayList<Boolean> constraintPositivity, int damage, int mark, boolean addToList, boolean removeFromList) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
        this.addToList = addToList;
        this.removeFromList = removeFromList;
    }

    public boolean isAddToList() {
        return addToList;
    }

    public boolean isRemoveFromList() {
        return removeFromList;
    }


    @Override
    public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException, NoOwnerException {
        if(!constraintsCheck(target,previousTarget)){
            throw new InvalidTargetException();
        }
        else{
            if(target.getEnemyPlayer()==target.getOwner()){
                throw new InvalidTargetException();
            }
            doRealDamage(target.getOwner(),target.getEnemyPlayer(),this.damage,this.mark);
            previousMan(previousTarget,target.getEnemyPlayer(),this.addToList,this.removeFromList);
        }
    }
}
