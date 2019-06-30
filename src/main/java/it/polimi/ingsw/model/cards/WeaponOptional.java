package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.exception.InvalidTargetException;
import it.polimi.ingsw.exception.NoAmmoException;
import it.polimi.ingsw.exception.NoOwnerException;
import it.polimi.ingsw.exception.NotThisKindOfWeapon;
import it.polimi.ingsw.model.TargetParameter;
import it.polimi.ingsw.model.cards.effects.Effect;
import java.util.ArrayList;

/**
 * class of optional weapons
 */
public class WeaponOptional extends Weapon {
    /**
     * list of the optional effects
     */
    private ArrayList<ArrayList<Effect>> optionalEffect= new ArrayList<>();
    /**
     * the correct orders of the optional effects
     */
    private ArrayList<String> order;

    /**
     * constructor method of alternative weapon
     * @param color color of the weapon
     * @param name name of the weapon
     * @param costBlue blue cost of the weapon
     * @param costRed red cost of the weapon
     * @param costYellow yellow cost of the weapon
     * @param effect list of weapon's effects
     * @param optionalEffect list of weapon's optional effects
     * @param order the order of the effects
     */
    public WeaponOptional(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect, ArrayList<ArrayList<Effect>> optionalEffect, ArrayList<String> order) {
        super(color, name, costBlue, costRed, costYellow, effect);
        this.optionalEffect=optionalEffect;
        this.order=order;
    }

    /**
     * fire the optional effects of this weapon
     * @param target are the targets of the fire
     * @param which which optional effect is going to be apply
     * @throws InvalidTargetException if at least one of the targets is invalid
     * @throws NoOwnerException when in at least one of the targets there is not the owner or owner position
     */
    @Override
    public void fireOptional(ArrayList<TargetParameter> target, int which) throws InvalidTargetException, NoOwnerException {
        int min = calcMinim(target,optionalEffect.get(which).size());
        for(int i=0;i<min;i++){
            optionalEffect.get(which).get(i).apply(target.get(i),this.getPreviousTarget());
        }
    }

    /**
     * throw an exception because this isn't an alternative weapon
     * @param target are the targets of the fire
     * @throws NotThisKindOfWeapon because this isn't an alternative weapon
     */
    @Override
    public void fireAlternative(ArrayList<TargetParameter> target) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

    /**
     * this is an optional weapon
     * @return true, this is an optional weapon
     */
    @Override
    public boolean isOptional() {
        return true;
    }

    /**
     * this is not an alternative weapon
     * @return false, this is not an alternative weapon
     */
    @Override
    public boolean isAlternative() {
        return false;
    }

    /**
     * method to verify if the owner can pay the optional effects
     * @param payment the cost in the different ammo amounts
     * @param which which effect
     * @return true if the player can pay
     */
    @Override
    protected boolean canPay(ArrayList<Integer> payment, int which){
        return this.optionalEffect.get(which).get(0).getCostBlue()==payment.get(2) && this.optionalEffect.get(which).get(0).getCostRed()==payment.get(0) && this.optionalEffect.get(which).get(0).getCostYellow()==payment.get(1);

    }

    /**
     * getter method of the optional effects
     * @return the list of the optional effects
     */
    public ArrayList<ArrayList<Effect>> getOptionalEffect() {
        return optionalEffect;
    }

    /**
     * getter method of the order of usage of the optional effects
     * @return the order of use
     */
    public ArrayList<String> getOrder() {
        return order;
    }
}
