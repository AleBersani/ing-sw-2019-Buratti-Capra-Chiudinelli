package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Map.Room;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class EffectVsRoom extends Effect {

    private int damage,mark;

    public EffectVsRoom(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    @Override
    public void apply(TargetParameter target) throws InvalidTargetException {
        if(!constraintsCheck(target)){
            throw new InvalidTargetException();
        }
        else{
            for(Square s: target.getTargetRoom().getSquares()){
                for(Player p: s.getOnMe()){
                    p.wound(this.damage,target.getOwner());
                    p.marked(this.mark,target.getOwner());
                }
            }
        }
    }
}
