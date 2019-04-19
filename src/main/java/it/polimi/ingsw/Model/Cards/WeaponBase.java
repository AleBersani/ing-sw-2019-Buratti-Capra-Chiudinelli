package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Model.Cards.Effects.Effect;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class WeaponBase extends Weapon {
    public WeaponBase(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect) {
        super(color, name, costBlue, costRed, costYellow, effect);
    }

    @Override
    public void fireOptional(TargetParameter target, int which) {

    }

    @Override
    public void fireAlternative(TargetParameter target) {

    }
}
