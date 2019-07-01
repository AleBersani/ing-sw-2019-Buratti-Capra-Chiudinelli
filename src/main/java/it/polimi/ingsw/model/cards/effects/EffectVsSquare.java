package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

/**
 * this class apply the effects to all players, except for the Owner, on the target square
 */
public class EffectVsSquare extends Effect {

    /**
     * is the number of damages this effect does
     */
    private int damage;
    /**
     * is the number of marks this effect does
     */
    private int mark;

    /**
     * constructor method of EffectVsSquare
     * @param costBlue blue cost of the effect
     * @param costRed red cost of the effect
     * @param costYellow yellow cost of the effect
     * @param name name of the effect
     * @param constraints all of the constraint that this method has
     * @param constraintPositivity constraint positivity of each constraint
     * @param damage is the number of damages this effect does
     * @param mark is the number of marks this effect does
     */
    public EffectVsSquare(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints,ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    /**
     * this method apply the effect after checking the constraint
     * @param target is a TargetParameter
     * @param previousTarget is the list of targets of the previous effects
     * @throws InvalidTargetException when not all of the constraints return true
     * @throws NoOwnerException when in target there isn't the owner or the owner position
     */
    @Override
    public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException, NoOwnerException {
        if(!constraintsCheck(target,previousTarget)){
            throw new InvalidTargetException();
        }
        else{
            for (Player p: target.getTargetSquare().getOnMe()){
                doRealDamage(target.getOwner(),p,this.damage,this.mark, previousTarget);
            }
        }
    }

    /**
     * generate the constraintSquare for the constraints
     * @param targetParameter is a target
     */
    @Override
    protected void constraintSquareGenerator(TargetParameter targetParameter) throws InvalidTargetException {
        if(targetParameter.getTargetSquare() == null){
            throw new InvalidTargetException();
        }
        targetParameter.setConstraintSquare(targetParameter.getTargetSquare());
    }
}
