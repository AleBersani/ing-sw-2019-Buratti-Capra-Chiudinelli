package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Room;

import java.util.ArrayList;

public class EffectVsRoom extends Effect {

    private int damage,mark;

    public EffectVsRoom(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    public void apply(Room target, Player owner){
        int i,j;
        for(i=0;i < target.getSquares().size();i++){
            for(j=0;j < target.getSquares().get(i).getOnMe().size();j++){
                target.getSquares().get(i).getOnMe().get(i).wound(this.damage,owner);
                target.getSquares().get(i).getOnMe().get(i).marked(this.mark,owner);
            }
        }
    }
}
