package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.TargetParameter;

import java.util.ArrayList;

public class WeaponBase extends Weapon {
    public WeaponBase(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect) {
        super(color, name, costBlue, costRed, costYellow, effect);
    }

    @Override
    public void fireOptional(ArrayList<TargetParameter> target, int which) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

    @Override
    public void fireAlternative(ArrayList<TargetParameter> target) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }
}
