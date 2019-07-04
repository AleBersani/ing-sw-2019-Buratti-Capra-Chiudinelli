package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.effects.Effect;
import java.util.ArrayList;

/**
 * class of alternative weapons
 */
public class WeaponAlternative extends Weapon {
    /**
     * list of the alternative effects
     */
    private ArrayList<Effect> alternativeEffect;

    /**
     * constructor method of alternative weapon
     * @param color color of the weapon
     * @param name name of the weapon
     * @param costBlue blue cost of the weapon
     * @param costRed red cost of the weapon
     * @param costYellow yellow cost of the weapon
     * @param effect list of weapon's effects
     * @param alternativeEffect list of weapon's alternative effects
     */
    public WeaponAlternative(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect, ArrayList<Effect> alternativeEffect) {
        super(color, name, costBlue, costRed, costYellow, effect);
        this.alternativeEffect=alternativeEffect;
    }

    /**
     * throw an exception because this is an alternative weapon
     * @param target are the targets of the fire
     * @param which which optional effect is going to be apply
     * @throws NotThisKindOfWeapon because this is an alternative weapon
     */
    @Override
    public void fireOptional(ArrayList<TargetParameter> target, int which) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

    /**
     * fire the alternative effects of this weapon
     * @param target are the targets of the fire
     * @throws InvalidTargetException if at least one of the targets is invalid
     * @throws NoOwnerException when in at least one of the targets there is not the owner or owner position
     */
    public void fireAlternative(ArrayList<TargetParameter> target) throws InvalidTargetException, NoOwnerException {
        int min = calcMinim(target,alternativeEffect.size());
        for(int i=0;i<min;i++){
            alternativeEffect.get(i).apply(target.get(i), this.getPreviousTarget());
        }
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
     * this is an alternative weapon
     * @return true, because this is an alternative weapon
     */
    @Override
    public boolean isAlternative() {
        return true;
    }

    /**
     * getter method of the alternative effects
     * @return the list of the alternative effects
     */
    public ArrayList<Effect> getAlternativeEffect() {
        return alternativeEffect;
    }
}
