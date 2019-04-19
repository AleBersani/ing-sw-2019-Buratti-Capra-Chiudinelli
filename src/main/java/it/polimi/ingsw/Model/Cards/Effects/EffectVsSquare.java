package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetExcepion;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class EffectVsSquare extends Effect {

    private int damage,mark;

    public EffectVsSquare(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    @Override
    public void apply(TargetParameter target) throws InvalidTargetExcepion {
        if(!constraintsCheck(target)){
            throw new InvalidTargetExcepion();
        }
        else{
            for (Player p: target.getTargetSquare().getOnMe()){
                if(p != target.getOwner()){
                    p.wound(this.damage,target.getOwner());
                    p.marked(this.mark,target.getOwner());
                }
            }
        }
    }
}
