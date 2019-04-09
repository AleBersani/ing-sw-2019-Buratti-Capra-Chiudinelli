package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Player;

import java.util.ArrayList;

public class EffectVsPlayer extends Effect {

    private int damage,mark;
    private boolean addToList,removeFromList;

    public EffectVsPlayer(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int damage, int mark, boolean addToList, boolean removeFromList) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.damage = damage;
        this.mark = mark;
        this.addToList = addToList;
        this.removeFromList = removeFromList;
    }

    public void apply(Player target, Player owner){
        target.wound(this.damage,owner);
        target.marked(this.mark,owner);
    }

    public boolean isAddToList() {
        return addToList;
    }

    public boolean isRemoveFromList() {
        return removeFromList;
    }
}
