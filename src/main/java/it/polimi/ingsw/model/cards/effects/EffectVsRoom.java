package it.polimi.ingsw.model.cards.effects;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.model.cards.constraints.Constraint;
import it.polimi.ingsw.model.map.Square;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class EffectVsRoom extends Effect {

    private int damage,mark;

    public EffectVsRoom(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints,ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    @Override
    public void apply(TargetParameter target, ArrayList<ArrayList<Player>> previousTarget) throws InvalidTargetException {
        if(!constraintsCheck(target,previousTarget)){
            throw new InvalidTargetException();
        }
        else{
            for(Square s: target.getTargetRoom().getSquares()){
                for(Player p: s.getOnMe()){
                    if(p!=target.getOwner()){
                        if(this.damage!=0){
                            p.wound(this.damage,target.getOwner());
                        }
                        p.marked(this.mark,target.getOwner());
                    }
                }
            }
        }
    }
}
