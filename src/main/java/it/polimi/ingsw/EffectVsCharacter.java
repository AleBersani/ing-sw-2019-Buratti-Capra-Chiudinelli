package it.polimi.ingsw;

import java.util.ArrayList;

public class EffectVsCharacter extends Effect {

    private int damage,mark;
    private boolean addToList,removeFromList;

    public EffectVsCharacter(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    public void apply(Player target){

    }
}
