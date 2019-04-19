package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;

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

    public boolean isAddToList() {
        return addToList;
    }

    public boolean isRemoveFromList() {
        return removeFromList;
    }


    @Override
    public void apply(TargetParameter target) {
        target.getEnemyPlayer().wound(this.damage,target.getOwner());
        target.getEnemyPlayer().marked(this.mark,target.getOwner());
    }
}
