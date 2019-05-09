package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class EffectVsSquare extends Effect {

    private int damage,mark;

    public EffectVsSquare(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints,ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    @Override
    public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException, NoOwnerException {
        if(!constraintsCheck(target,previousTarget)){
            throw new InvalidTargetException();
        }
        else{
            for (Player p: target.getTargetSquare().getOnMe()){
                doRealDamage(target.getOwner(),p,this.damage,this.mark);
            }
        }
    }
}
