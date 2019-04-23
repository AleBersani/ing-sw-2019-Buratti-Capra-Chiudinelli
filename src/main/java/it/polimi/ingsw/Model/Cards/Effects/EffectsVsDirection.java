package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetExcepion;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class EffectsVsDirection extends Effect {

    private ArrayList<Integer> damageList;
    private ArrayList<Integer> markList;

    public EffectsVsDirection(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, ArrayList<Integer> damageList, ArrayList<Integer> markList) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damageList = damageList;
        this.markList = markList;
    }

    @Override
    public void apply(TargetParameter target) throws InvalidTargetExcepion {
        if(!constraintsCheck(target)){
            throw new InvalidTargetExcepion();
        }
        else{
            //TODO
        }
    }
}
