package it.polimi.ingsw;

import java.util.ArrayList;

public class EffectVsPlayer extends Effect {

    public EffectVsPlayer(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark, boolean addToList, boolean removeFromList) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
        this.addToList = addToList;
        this.removeFromList = removeFromList;
    }

    private int damage,mark;
    private boolean addToList,removeFromList;

    public EffectVsCharacter(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
    }

    public void apply(Player target, Player owner){

    }
}
