package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;

import java.util.ArrayList;

public class EffectVsSquare extends Effect {

    private int damage,mark;

    public EffectVsSquare(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    public void apply(Square target, Player owner){
        int i;
        for (i=0; i< target.getOnMe().size();i++){
            target.getOnMe().get(i).wound(this.damage,owner);
            target.getOnMe().get(i).marked(this.mark,owner);
        }
    }
}
