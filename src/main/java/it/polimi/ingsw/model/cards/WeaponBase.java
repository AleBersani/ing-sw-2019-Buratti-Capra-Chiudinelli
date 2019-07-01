package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.cards.effects.Effect;
import it.polimi.ingsw.model.TargetParameter;
import java.util.ArrayList;

/**
 * class of weapon
 */
public class WeaponBase extends Weapon {
    /**
     * constructor method of base weapons
     * @param color color of the weapon
     * @param name name of the weapon
     * @param costBlue blue cost of the weapon
     * @param costRed red cost of the weapon
     * @param costYellow yellow cost of the weapon
     * @param effect list of weapon's effects
     */
    public WeaponBase(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect) {
        super(color, name, costBlue, costRed, costYellow, effect);
    }

    /**
     * throw an exception because this isn't an optional weapon
     * @param target are the targets of the fire
     * @param which which optional effect is going to be apply
     * @throws NotThisKindOfWeapon because this isn't an optional weapon
     */
    @Override
    public void fireOptional(ArrayList<TargetParameter> target, int which) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

    /**
     * throw an exception because this isn't an optional weapon
     * @param target are the targets of the fire
     * @throws NotThisKindOfWeapon because this isn't an optional weapon
     */
    @Override
    public void fireAlternative(ArrayList<TargetParameter> target) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

    /**
     * this is not an optional weapon
     * @return false, this is not an optional weapon
     */
    @Override
    public boolean isOptional() {
        return false;
    }

    /**
     * this is not an alternative weapon
     * @return false, this is not an alternative weapon
     */
    @Override
    public boolean isAlternative() {
        return false;
    }


}
