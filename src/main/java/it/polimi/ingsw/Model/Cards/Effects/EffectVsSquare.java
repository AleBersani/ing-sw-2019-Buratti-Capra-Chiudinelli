package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class EffectVsSquare extends Effect {

    private int damage,mark;

    public EffectVsSquare(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints);
        this.damage = damage;
        this.mark = mark;
    }

    @Override
    public void apply(TargetParameter target) throws InvalidTargetException {
        if(!constraintsCheck(target)){
            throw new InvalidTargetException();
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
